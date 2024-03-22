package com.interfacessos.services

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.interfacessos.database.DBHelper
import com.interfacessos.model.Contato

class ServicoSms(private val dbHelper: DBHelper,private val context: Context?): Service(){


    var latitude: String = ""
    var longitude: String = ""
    var ultimaAtualizacao: String = ""
    private lateinit var locationManager: LocationManager
    //private val ACTION_CLICK = "com.interfacesos.ui.ACTION_CLICK"


    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        startSendSms()
    }

    fun enviarMensagem(telefone: String, mensagem: String, context: Context){
        try{
            val permissao = Manifest.permission.SEND_SMS
            if(ContextCompat.checkSelfPermission(context, permissao) == PackageManager.PERMISSION_GRANTED){
                val smsManager: SmsManager = SmsManager.getDefault()
                val envioIntent = PendingIntent.getBroadcast(context,0, Intent("SMS_ENVIADO"),
                    PendingIntent.FLAG_IMMUTABLE)
                val entregueIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_ENTREGUE"),
                    PendingIntent.FLAG_IMMUTABLE)

                smsManager.sendTextMessage(telefone, null, mensagem,envioIntent,entregueIntent)
                Log.d("Mensagem enviada","Mensagem enviada")
                Toast.makeText(context, "Mensagem enviada com sucesso", Toast.LENGTH_SHORT).show()
            }else{
                ActivityCompat.requestPermissions(context as Activity, arrayOf(permissao), 0)
                Toast.makeText(context, "Erro ao enviar mensagem", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            ActivityCompat.requestPermissions(context as Activity, arrayOf(), 0)
            Toast.makeText(context, "Erro ao enviar mensagem", Toast.LENGTH_SHORT).show()
        }
    }

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                Log.d("WIDGET LOCALIZACAO","${latitude},${longitude}")
                latitude = it.getStringExtra("latitude").toString()
                longitude = it.getStringExtra("longitude").toString()
                ultimaAtualizacao = it.getStringExtra("ultimaAtt").toString()
            }
        }
    }

    fun startSendSms(){
        val listaContatos: ArrayList<Contato> = dbHelper.getContatos()
        listaContatos.forEach {
                contato ->
            if (context != null) {
                Log.d("Numero","${contato.telefone}")
                Log.d("Latitude","${latitude}")
                Log.d("Longitude", "${ longitude }")
                enviarMensagem(contato.telefone,"http://maps.google.com/?q=${latitude},${longitude}", context)
            }
        }
    }

    fun startSendSmsRepetidamente(context: Context?, iniciaSOS: Boolean){
        if(iniciaSOS){
            startSendSms()
            val handler = Handler()
            handler.postDelayed({
                    startSendSmsRepetidamente(context, iniciaSOS)
            //}, 15000)
            }, 30000)
        }
    }
}