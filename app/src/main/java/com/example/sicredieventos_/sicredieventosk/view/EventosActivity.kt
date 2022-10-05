package com.example.sicredieventos_.sicredieventosk.view

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sicredieventos_.sicredieventosk.R
import com.example.sicredieventos_.sicredieventosk.adapter.EventosAdapter

class EventosActivity : AppCompatActivity() {
    private lateinit var rcView_eventos: RecyclerView
    private lateinit var eventosAdapter: EventosAdapter
    private lateinit var lista_id: ArrayList<String>
    private lateinit var lista_title: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventos)
        rcView_eventos = findViewById(R.id.rv_eventos)

        val dados: Bundle? = intent.extras

        lista_id = dados?.getStringArrayList("listaEventos_id") as ArrayList<String>
        lista_title = dados?.getStringArrayList("listaEventos_title") as ArrayList<String>
        System.out.println("Id: ".plus(lista_id.get(0)))
        System.out.println("Title: ".plus(lista_title.get(0)))


        rcView_eventos.setHasFixedSize(true)
        rcView_eventos.layoutManager= LinearLayoutManager(this)
        eventosAdapter = EventosAdapter(this@EventosActivity,lista_id, lista_title)
        rcView_eventos.adapter = eventosAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_voltar) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    open fun isConectado(application: Application): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw)
            actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(
                NetworkCapabilities.TRANSPORT_BLUETOOTH
            ))
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo
            nwInfo != null && nwInfo.isConnected
        }
    }


}