package com.example.sicredieventos_.sicredieventosk.view

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide

import com.example.sicredieventos_.sicredieventosk.R
import com.example.sicredieventos_.sicredieventosk.data.Event
import com.example.sicredieventos_.sicredieventosk.databinding.ActivityCheckinBinding
import com.example.sicredieventos_.sicredieventosk.model.Evento
import com.example.sicredieventos_.sicredieventosk.model.PeopleEventoDTO
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class CheckinActivity : AppCompatActivity() {
    private val lista_eventos: ArrayList<Event> = ArrayList<Event>()
    private var evento: Evento? = null
    private var descricao: TextView? = null
    private var desc: TextView? = null
    private var tv_qtd: TextView? = null
    private var tv_valor: TextView? = null
    private var mensagem: TextView? = null
    //private var imageView: ImageView? = null
    //private var pDialog: ProgressDialog? = null
    private var dialog: AlertDialog? = null
    private var request: Request? = null
    private val lista_eventos2: ArrayList<Evento> = ArrayList<Evento>()
    private var editText_nome: TextInputEditText? = null
    private var editText_email: TextInputEditText? = null
    private var btn_conclui: Button? = null
    private var btn_cad: Button? = null
    private var edtUrl: String? = null
    private var id: kotlin.String? = null
    private var date: Long = 0
    private var endereco: Address? = null

    private var str_title: ArrayList<String> = ArrayList<String>()
    private var str_id: ArrayList<String> = ArrayList<String>()

    private lateinit var binding: ActivityCheckinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_checkin)
        binding = ActivityCheckinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tv_qtd = findViewById<TextView>(R.id.tv_qtd_part)
        tv_valor = findViewById(R.id.tv_valor)
        descricao = findViewById(R.id.tv_mult_lin)
        desc = findViewById(R.id.tv_descri)

        mensagem = findViewById(R.id.tv_mensg)

        val dados = intent.extras

        id = dados!!.getString("id")

        binding.tvQtdPart.text =
            resources.getString(R.string.enrolled).plus(dados?.getInt("qtd"))
        binding.tvTitle.text = dados!!.getString("title")

        binding.tvDescri.text = dados!!.getString("description")

        val latitude = dados!!.getDouble("latitude")
        val longitude = dados!!.getDouble("longitude")

        binding.tvValor.setText(resources.getString(R.string.enrolled).plus(dados?.getString("price", "")))

        edtUrl = dados!!.getString("image", "")
        val imageView = findViewById<ImageView>(R.id.imageView);
        Glide.with(this).load(edtUrl).error(R.drawable.share).into(imageView)

        try {
            endereco = carregarEndereco(latitude, longitude)
            val end = endereco!!.locality
            val estado = endereco!!.adminArea
            val pais = endereco!!.countryName
            if (end != null) {
                binding.tvEnd.setText("Cidade: $end")
            }
            if (estado != null) {
                binding.tvEst.setText("Estado: $estado")
            }
            if (pais != null) {
                binding.tvPais.setText("País: $pais")
            }
        } catch (e: IOException) {
            Log.i("GPS", e.message!!)
        }
        //title.setText(evento.getTitle());

        //title.setText(evento.getTitle());
        binding.btnConfEvento.setOnClickListener(View.OnClickListener {
            binding.llayoutCad.setVisibility(View.VISIBLE)
            binding.llayoutCad.isEnabled = false
        })

        binding.btnConclui.setOnClickListener(View.OnClickListener {
            desabilita_btns()
            /*
            var dialog = setProgressDialog(this, "Carregando...")
            dialog.show()*/
            confereEntradas()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_voltar) {
            finish()
            //direcTelaAnt()
        }
        return super.onOptionsItemSelected(item)
    }
    fun direcTelaAnt(){
        finish()
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

    fun habilita_btns() {
        btn_cad?.isEnabled = true
        btn_cad?.isClickable = true
        btn_conclui?.isEnabled = true
        btn_conclui?.isClickable = true
        binding.editTextNome.isEnabled = true
        binding.editTextEmail.isEnabled = true
    }

    fun desabilita_btns() {
        btn_cad?.isEnabled = false
        btn_cad?.isClickable = false
        btn_conclui?.isEnabled = false
        btn_conclui?.isClickable = false
        binding.editTextNome.isEnabled = false
        binding.editTextEmail.isEnabled = false
    }

    fun confereEntradas() {
        mensagem!!.text = ""

        val nome = isCampoVazio(binding.editTextNome.text.toString())
        val email = isCampoVazio(binding.editTextEmail.text.toString())

        if (nome) {
            binding.editTextNome.requestFocus()

            Toast.makeText(
                this@CheckinActivity,
                "Atenção! O campo nome deve ser preenchido...",
                Toast.LENGTH_LONG
            ).show()
            habilita_btns()

        } else if (email) {

            binding.editTextEmail.requestFocus()
            Toast.makeText(
                this@CheckinActivity,
                "Atenção! O campo e-mail deve ser preenchido...",
                Toast.LENGTH_LONG
            ).show()
            habilita_btns()
        }else{
            if (this.application.isNetworkConnected) {
                insertCad()
            } else {

                mensagem!!.text = resources.getString(R.string.withoutNet)
                habilita_btns()
            }
        }
    }

    private fun isCampoVazio(dado: String): Boolean {
        return TextUtils.isEmpty(dado)// || dado.replace("\\s".toRegex(), "").isEmpty()
    }

    fun insertCad() {

        val objeto: PeopleEventoDTO? = null
        objeto?.PeopleEventoDTO(editText_nome!!.text.toString(), editText_email!!.text.toString(), id!!)

        val gson = Gson()
        val objJson = gson.toJson(objeto)
        val httpClient: OkHttpClient =
            OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build()
        val url = "https://5f5a8f24d44d640016169133.mockapi.io/api/checkin"
        try {
            val JSON: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull()!!
            val body: RequestBody = RequestBody.create(JSON, objJson)
            request = Request.Builder()
                .post(body)
                .url(url)
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            mensagem!!.text = "Erro de conexão! Tente novamente."
        }
        httpClient.newCall(request!!).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(
                        this@CheckinActivity,
                        "Falha ao realizar a inscrição... Favor entrar em contato via e-mail.$e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(
                            this@CheckinActivity,resources.getString(R.string.checkinSuc),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } else {

                    runOnUiThread {
                        try {
                            mensagem!!.text = resources.getString(R.string.checkinError).plus( response.code)
                            //if (dialog!!.isShowing) dialog!!.dismiss()
                            Toast.makeText(
                                this@CheckinActivity,resources.getString(R.string.checkinErrorMens),
                                Toast.LENGTH_SHORT
                            ).show()
                            habilita_btns()
                        }catch (e: IOException){

                        }

                    }
                }
            }
        })
    }

    fun carregarEndereco(latitude: Double?, longitude: Double?): Address? {
        val geocoder: Geocoder
        var address: Address? = null
        val addresses: List<Address>
        geocoder = Geocoder(applicationContext)
        addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)!!
        if (addresses.size > 0) {
            address = addresses[0]
        }
        return address
    }

    fun setProgressDialog(context: Context, message: String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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
