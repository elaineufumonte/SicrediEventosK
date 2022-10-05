package com.example.sicredieventos_.sicredieventosk.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sicredieventos_.sicredieventosk.R
import com.example.sicredieventos_.sicredieventosk.model.Evento
import com.example.sicredieventos_.sicredieventosk.view.CheckinActivity
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class EventosAdapter(private val acontext: Activity, private val alistaId: ArrayList<String>, private val alistaTitle: ArrayList<String>) : RecyclerView.Adapter<EventosAdapter.MyViewHolder>() {//private val acontext: Activity,

    var request: Request? = null
    val locale = Locale.getDefault()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val linhaView: View = inflater.inflate(R.layout.item_evento, parent, false)
        return MyViewHolder(linhaView, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.setText(alistaTitle?.get(position))
        holder.btnDirecionar.setOnClickListener {
            buscarEvento(alistaId?.get(position), acontext)
        }

    }

    val Context.isNetworkConnected: Boolean
        get() {
            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
    private fun buscarEvento(id: String?, acontext: Activity) {

        if (acontext.application.isNetworkConnected) {

            val httpClient: OkHttpClient = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build()
            val url =
                "https://5f5a8f24d44d640016169133.mockapi.io/api/events/" + id
            println(url)
            try {
                request = Request.Builder().url(url).build()
            } catch (e: Exception) {

                e.printStackTrace()
                acontext.runOnUiThread {
                    Toast.makeText(
                        acontext,
                        "Erro de conexão! Tente novamente.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            httpClient.newCall(request!!).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    acontext.runOnUiThread {
                        Toast.makeText(
                            acontext,
                            "Falha na requisição! ",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val myResponse = response.body.string()
                        acontext.runOnUiThread {
                            val gson = Gson()
                            try {
                                val evento: Evento = gson.fromJson(myResponse, Evento::class.java)
                                val formatted_valor = NumberFormat.getCurrencyInstance(locale).format(
                                    evento!!.getPrice()
                                )
                                val intent = Intent(
                                    acontext.applicationContext,
                                    CheckinActivity::class.java
                                )
                                intent.putExtra("id", evento.getId())
                                intent.putExtra("qtd", evento.getPeople()?.size)
                                intent.putExtra("title", evento.getTitle())
                                intent.putExtra("price", formatted_valor)
                                intent.putExtra("description", evento.getDescription())
                                intent.putExtra("latitude", evento.getLatitude())
                                intent.putExtra("longitude", evento.getLongitude())
                                intent.putExtra("image", evento.getImage())
                                intent.putExtra("date", evento.getDate())
                                (acontext as AppCompatActivity).startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    acontext,
                                    "Evento indisponível... Tente mais tarde.",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    } else {

                        //if(!response.isSuccessful()) { //throw new IOException("Unexpected code " + response);
                        if (response.code != 200) {
                            //tv_relatorio.setText("Erro! Número de matrícula inválida.");
                            acontext.runOnUiThread {
                                //println("response.code() != 200 -> " + response.code)
                                Toast.makeText(
                                    acontext,
                                    "Falha ao realizar uma requisição no servidor...",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            })
        } else {

            Toast.makeText(acontext, "FALHA! SEM CONEXÃO COM A INTERNET...", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return alistaTitle?.size ?: 0
    }
    class MyViewHolder(itemView: View, acontext: Context?) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var btnDirecionar: ImageView
        init {
            title = itemView.findViewById(R.id.tv_title)
            btnDirecionar = itemView.findViewById(R.id.img_ic_direcionar)

        }


    }



    fun setProgressDialog(context: Context, message:String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20.toFloat()
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }

}