package com.interfacessos.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.interfacessos.database.DBHelper
import com.interfacessos.databinding.ActivityTelaLocalizacaoBinding
import com.interfacessos.model.Contato
import com.interfacessos.services.ServicoLocalizacao
import com.interfacessos.services.ServicoSms

class TelaLocalizacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaLocalizacaoBinding
    private lateinit var dbHelper: DBHelper

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {

                val latitude = it.getStringExtra("latitude")
                val longitude = it.getStringExtra("longitude")
                val ultimaAtualizacao = it.getStringExtra("ultimaAtt")

                binding.txtLatAtual.setText(latitude)
                binding.txtLongAtual.setText(longitude)
                binding.txtUltimaLoc.setText(ultimaAtualizacao)

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
            registerReceiver(locationReceiver,filter, RECEIVER_EXPORTED)
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
        }
        return true
    }
    override fun onStart() {
        super.onStart()
        binding.btnDialogMenu.setOnClickListener{
            val dialog = AddDialogFragmentMenu()
            dialog.show(supportFragmentManager,dialog.tag)
        }
        binding.btnEnviarLocalizacao.setOnClickListener {
            startSendSms()
        }
        binding.btnListaUsuarios.setOnClickListener{
            val i = Intent(this, ListaUsuarios::class.java)
            startActivity(i)
        }
        binding.btnListaContatos.setOnClickListener{
            val i = Intent(this, ListaContatosActivity::class.java)
            startActivity(i)
        }
    }
    fun startSendSms(){
        //var numero1 = binding.txtContato1.text.toString()

        var numero1 = "34988751014"
        var numero2 = "34988433526"
        val listaContatos: ArrayList<Contato> = dbHelper.getContatos()

        Log.d("Lista contatos","${listaContatos.size}")

        listaContatos.forEach {
            contato ->
            ServicoSms().enviarMensagem(contato.telefone,"Teste mensagem SOS",this)
        }

    }
    override fun onResume() {
        super.onResume()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    fun startLocationService(){
        val serviceIntent = Intent(this, ServicoLocalizacao::class.java)
        if(permissaoLocalizacaoConcedida()){
            startService(serviceIntent)
        }else{
            startService(serviceIntent)
        }

    }
    fun buscarDados(){
        val i = intent
        val bundle = i.extras
        val nome = i.extras?.getString("nome_usuario_alterado").toString()
        Log.d("Valo nome","${nome}")

        val usuarioRecuperado = dbHelper.getUsuario(nome)

        if (usuarioRecuperado != null) {
            val nomeExibir = usuarioRecuperado.nome
            binding.txtOla.text = "Olá $nomeExibir"

            val contatos = dbHelper.getContatosDoUsuario(usuarioRecuperado.id)
            Log.d("Retorno", "${dbHelper.getContatosDoUsuario(usuarioRecuperado.id)}")
            Log.d("Lista contatos", "${contatos}")
            if (contatos.isNotEmpty()) {
                val contato1 = contatos[0]
                binding.txtContato1.text = contato1.nome
            } else {
                Log.d("lista contatos nula","nula")
            }
        } else {
            Log.d("usuario nulo","nulo")
        }

    }
}