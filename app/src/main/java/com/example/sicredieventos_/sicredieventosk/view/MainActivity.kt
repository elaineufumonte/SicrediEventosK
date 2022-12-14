package com.example.sicredieventos_.sicredieventosk.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.example.sicredieventos_.sicredieventosk.R
import com.example.sicredieventos_.sicredieventosk.data.LoadingDialog
import com.example.sicredieventos_.sicredieventosk.data.Event
import com.example.sicredieventos_.sicredieventosk.data.PeopleK
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var dialog: AlertDialog? = null
    private val lista_eventos: ArrayList<Event> = ArrayList<Event>()
    private var str_title: ArrayList<String> = ArrayList<String>()
    private var str_id: ArrayList<String> = ArrayList<String>()
    private var request: Request? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_listarEventos: Button =  findViewById(R.id.btn_listarEventos)

        btn_listarEventos.setOnClickListener(View.OnClickListener {
            val loading = LoadingDialog(this)
            loading.starLoading()
            val handles = android.os.Handler()
            handles.postDelayed(object :Runnable{
                override fun run() {
                    loading.isDismiss()
                }

            }, 5000)
            listarEventos()
        })
    }
    fun listarEventos() {

        if (this.application.isNetworkConnected) {
            val httpClient: OkHttpClient = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build()

            val url = "https://5f5a8f24d44d640016169133.mockapi.io/api/events"

            try {
                request = Request.Builder().url(url).build()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    this@MainActivity,
                    "Erro de conex??o! Tente novamente.",
                    Toast.LENGTH_LONG
                ).show()
            }
            httpClient.newCall(request!!).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (dialog!!.isShowing) dialog!!.dismiss()
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val myResponse = response.body.string()

                        runOnUiThread {
                            val gson = Gson()
                            try {
                                val listaEventoJson = JSONArray(myResponse)
                                var evento: JSONObject

                                lista_eventos.clear()
                                if (listaEventoJson.length() == 0) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "N??o possui nenhum evento no momento...",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {

                                    for (i in 0 until listaEventoJson.length()) {
                                        evento = JSONObject(listaEventoJson.getString(i))
                                        val listaPeople_Json = JSONArray(evento["people"].toString())

                                        val listPeople: java.util.ArrayList<PeopleK> = java.util.ArrayList<PeopleK>()
                                        if (listaPeople_Json.length() != 0) {
                                            for (ii in 0 until listaPeople_Json.length()) {
                                                val objPeople =
                                                    JSONObject(listaPeople_Json.getString(ii))
                                                val obj_people = PeopleK(objPeople.getString("eventId"), objPeople.getString("name"), objPeople.getString("email"))

                                                listPeople.add(obj_people)
                                            }
                                        }
                                        val event = Event(listPeople, evento.getLong("date"), evento.getString("description"), evento.getString("image"),
                                            evento.getDouble("longitude"), evento.getDouble("latitude"), evento.getDouble("price"), evento.getString("title"), evento.getString("id"))
                                        System.out.println("Title: ".plus(event.title))//.plus(evento?.getPeople()?.size) event.title
                                        str_title.add(event.title)
                                        str_id.add(event.id)


                                        lista_eventos.add(event)
                                    }


                                    val intent = Intent(applicationContext, EventosActivity::class.java)
                                    intent.putExtra("listaEventos_id", str_id as Serializable)
                                    intent.putExtra("listaEventos_title", str_title as Serializable)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                }

                            } catch (e: JSONException) {
                                Log.e("Erro", "Erro no parsing do JSON", e)

                                Toast.makeText(
                                    this@MainActivity,
                                    "Erro na requisi????o.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        if (response.code != 200) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Eventos n??o encontrados.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            })
        } else {
            if (dialog!!.isShowing) dialog!!.dismiss()
            Toast.makeText(
                this@MainActivity,
                "FALHA! SEM CONEX??O COM A INTERNET",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_sair) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    val Context.isNetworkConnected: Boolean
        get() {
            val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || it.hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR
                    ) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                            it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                } ?: false
            else
                @Suppress("DEPRECATION")
                manager.activeNetworkInfo?.isConnected == true
        }

}