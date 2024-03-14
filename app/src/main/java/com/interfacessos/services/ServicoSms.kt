package com.interfacessos.services

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ServicoSms{
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



}