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
import androidx.fragment.app.FragmentActivity
import com.interfacessos.database.DBHelper
import com.interfacessos.databinding.ActivityTelaInformacoesBinding
import com.interfacessos.model.Contato
import com.interfacessos.model.Usuario
import com.interfacessos.services.ServicoLocalizacao

/**
 * Loads [MainFragment].
 */
class TelaInformacoes : AppCompatActivity() {
    private lateinit var binding: ActivityTelaInformacoesBinding
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

                binding.txtLatAtual.setText(latitude)
                binding.txtLongAtual.setText(longitude)
                //binding.txtUltimaLoc.setText(ultimaAtualizacao)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityTelaInformacoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        val filter = IntentFilter(ServicoLocalizacao.LOCATION_UPDATE_ACTION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(locationReceiver, filter, RECEIVER_EXPORTED)
            //registerReceiver(envioReceiver, IntentFilter("SMS_ENVIADO"), RECEIVER_EXPORTED)
            //registerReceiver(entregaReceiver, IntentFilter("SMS_ENTREGUE"), RECEIVER_EXPORTED)
        } else {
            registerReceiver(locationReceiver, filter)
            //registerReceiver(envioReceiver, IntentFilter("SMS_ENVIADO"))
            //registerReceiver(entregaReceiver, IntentFilter("SMS_ENTREGUE"))
        }

        buscarDados()
        startLocationService()
    }
    fun startLocationService() {
        val serviceIntent = Intent(this, ServicoLocalizacao::class.java)
        if (permissaoLocalizacaoConcedida()) {
            startService(serviceIntent)
        }
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

    fun buscarDados() {
        val nome = intent.extras?.getString("nome_usuario_alterado").toString()
        Log.d("Valo nome", nome)

        val usuarioRecuperado: ArrayList<Usuario> = dbHelper.getUsuarios()

        if (usuarioRecuperado.isNotEmpty()) {
            usuarioRecuperado.forEach {
                val contatos: ArrayList<Contato> = dbHelper.getContatos()
                if (contatos.isNotEmpty()) {
                    val contato1 = contatos[0]
                    //binding.txtContato1.setText(contato1.telefone)
                } else {
                    Log.d("lista contatos nula", "nula")
                }
            }
        } else {
            Log.d("usuario nulo", "nulo")
        }
    }
}