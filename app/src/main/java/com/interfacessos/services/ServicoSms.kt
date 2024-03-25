package com.interfacessos.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.interfacessos.R
import com.interfacessos.database.DBHelper
import com.interfacessos.ui.MainActivity

class ServiceSms : Service() {

    private lateinit var dbHelper: DBHelper

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        dbHelper = DBHelper(this)
        enviarMensagem(intent?.getStringExtra("telefone"), intent?.getStringExtra("mensagem"))

        val notificacao = criarNotificacao()

        startForeground(1,notificacao)

        return START_NOT_STICKY
    }

    private fun criarNotificacao(): Notification {
        val id= "Sms servico"
        val nome = "sms servico"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val notificationChannel = NotificationChannel(id,nome,NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, id)
            .setContentTitle("Serviço em execução")
            .setContentText("Servico de envio de sms em execucao")
            .setSmallIcon(R.drawable.ic_email)
            .setContentIntent(pendingIntent)
            .build()

    }

    private fun enviarMensagem(telefone: String?, mensagem: String?) {
        try {
            val permissao = Manifest.permission.SEND_SMS
            if (ContextCompat.checkSelfPermission(this, permissao) == PackageManager.PERMISSION_GRANTED) {
                val smsManager: SmsManager = SmsManager.getDefault()
                val envioIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_ENVIADO"),
                    PendingIntent.FLAG_IMMUTABLE)
                val entregueIntent = PendingIntent.getBroadcast(this, 0, Intent("SMS_ENTREGUE"),
                    PendingIntent.FLAG_IMMUTABLE)

                smsManager.sendTextMessage(telefone, null, mensagem, envioIntent, entregueIntent)
                Log.d("Mensagem enviada", "Mensagem enviada")
                Toast.makeText(this, "Mensagem enviada com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Erro ao enviar mensagem: permissão não concedida", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao enviar mensagem: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
