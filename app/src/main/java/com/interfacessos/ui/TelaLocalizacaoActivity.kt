package com.interfacessos.ui

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.interfacessos.database.DBHelper
import com.interfacessos.databinding.ActivityTelaLocalizacaoBinding
import com.interfacessos.model.Contato
import com.interfacessos.model.Usuario
import com.interfacessos.services.ServicoLocalizacao
import com.interfacessos.services.ServicoSms

class TelaLocalizacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaLocalizacaoBinding
    private lateinit var dbHelper: DBHelper
    var latitude: String = ""
    var longitude: String = ""
    var ultimaAtualizacao: String = ""

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                latitude = it.getStringExtra("latitude").toString()
                longitude = it.getStringExtra("longitude").toString()
                ultimaAtualizacao = it.getStringExtra("ultimaAtt").toString()

                /*binding.txtLatAtual.setText(latitude)
                binding.txtLongAtual.setText(longitude)
                binding.txtUltimaLoc.setText(ultimaAtualizacao)*/
            }
        }
    }

    private val envioReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(context, "Mensagem enviada com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao enviar mensagem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val entregaReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(context, "Mensagem entregue com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao entregar mensagem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTelaLocalizacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        val filter = IntentFilter(ServicoLocalizacao.LOCATION_UPDATE_ACTION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(locationReceiver, filter, RECEIVER_EXPORTED)
            registerReceiver(envioReceiver, IntentFilter("SMS_ENVIADO"), RECEIVER_EXPORTED)
            registerReceiver(entregaReceiver, IntentFilter("SMS_ENTREGUE"), RECEIVER_EXPORTED)
        } else {
            registerReceiver(locationReceiver, filter)
            registerReceiver(envioReceiver, IntentFilter("SMS_ENVIADO"))
            registerReceiver(entregaReceiver, IntentFilter("SMS_ENTREGUE"))
        }

        buscarDados()
        startLocationService()
    }

    private fun permissaoLocalizacaoConcedida(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
            return false
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        binding.btnDialogMenu.setOnClickListener {
            val dialog = AddDialogFragmentMenu()
            dialog.show(supportFragmentManager, dialog.tag)
        }
        binding.btnEnviarLocalizacao.setOnClickListener {
            startSendSms()
        }
        binding.btnListaUsuarios.setOnClickListener {
            val i = Intent(this, ListaUsuarios::class.java)
            startActivity(i)
        }
        binding.btnListaContatos.setOnClickListener {
            val i = Intent(this, ListaContatosActivity::class.java)
            startActivity(i)
        }
    }

    fun startSendSms() {
        val listaContatos: ArrayList<Contato> = dbHelper.getContatos()

        Log.d("Lista contatos", "${listaContatos.size}")

        listaContatos.forEach { contato ->
            ServicoSms(dbHelper, this).enviarMensagem(
                contato.telefone,
                "http://maps.google.com/?q=${latitude},${longitude}",
                this
            )
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationService()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(locationReceiver)
        unregisterReceiver(envioReceiver)
        unregisterReceiver(entregaReceiver)
    }

    fun startLocationService() {
        val serviceIntent = Intent(this, ServicoLocalizacao::class.java)
        if (permissaoLocalizacaoConcedida()) {
            startService(serviceIntent)
        }
    }

    fun buscarDados() {
        val nome = intent.extras?.getString("nome_usuario_alterado").toString()
        Log.d("Valo nome", nome)

        val usuarioRecuperado: ArrayList<Usuario> = dbHelper.getUsuarios()

        if (usuarioRecuperado.isNotEmpty()) {
            usuarioRecuperado.forEach {
                val contatos: ArrayList<Contato> = dbHelper.getContatos()
                if (contatos.isNotEmpty()) {
                    val contato1 = contatos[0]
                    binding.txtContato1.setText(contato1.telefone)
                } else {
                    Log.d("lista contatos nula", "nula")
                }
            }
        } else {
            Log.d("usuario nulo", "nulo")
        }
    }
}