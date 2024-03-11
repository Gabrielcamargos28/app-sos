package com.interfacessos.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.interfacessos.databinding.ActivityTelaLocalizacaoBinding
import com.interfacessos.services.ServicoLocalizacao
import com.interfacessos.services.ServicoSms

class TelaLocalizacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaLocalizacaoBinding

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

        val filter = IntentFilter(ServicoLocalizacao.LOCATION_UPDATE_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(locationReceiver,filter, RECEIVER_EXPORTED)
        }

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
    }
    fun startSendSms(){
        //var numero1 = binding.txtContato1.text.toString()
        var numero1 = "34988751014"
        var numero2 = binding.txtContato2.text

        ServicoSms().enviarMensagem(numero1,"Teste mensagem SOS",this)
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
        }
    }
}