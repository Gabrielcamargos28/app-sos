package com.interfacessos.ui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.interfacessos.R
import com.interfacessos.database.DBHelper
import com.interfacessos.model.Contato
import com.interfacessos.services.ServiceSms
import com.interfacessos.services.ServicoLocalizacao

/**
 * Implementation of App Widget functionality.
 */
class SosWidget : AppWidgetProvider() {

    private lateinit var dbHelper: DBHelper
    var latitude: String = ""
    var longitude: String = ""
    var ultimaAtualizacao: String = ""
    private var handler: Handler? = null
    private lateinit var locationManager: LocationManager

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {

                latitude = it.getStringExtra("latitude").toString()
                longitude = it.getStringExtra("longitude").toString()
                ultimaAtualizacao = it.getStringExtra("ultimaAtt").toString()
            }
        }
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        dbHelper = DBHelper(context!!)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var iniciaSOS = sharedPreferences.getBoolean("iniciaSOS", false)
        Log.d("Declaracao", "sharedPreferences: ${iniciaSOS}")
        //var iniciaLocalizacao = sharedPreferences.getBoolean("iniciaLocalizacao", false)
        if(intent?.action == "CLICK_BTN") {
            fun startSendSms() {
                val listaContatos: ArrayList<Contato> = dbHelper.getContatos()

                Log.d("Lista contatos", "${listaContatos.size}")

                listaContatos.forEach { contato ->
                    val intentSms = Intent(context, ServiceSms::class.java).apply {
                        putExtra("telefone", contato.telefone)
                        putExtra("mensagem", "http://maps.google.com/?q=$latitude,$longitude")
                    }
                    ContextCompat.startForegroundService(context, intentSms)
                }
            }
            fun startSendSmsRepetidamente(context: Context?, iniciaSOS: Boolean) {
                Log.d("SOS", "valor do sos dentro do repetidamente ${iniciaSOS}")
                if (iniciaSOS) {
                    Log.d("SOS","entrou no loop")
                    Log.d("STARTSEND", "valor: ${iniciaSOS}")
                    startSendSms()
                    if (handler == null) {
                        handler = Handler()
                    }
                    handler!!.postDelayed({
                        startSendSmsRepetidamente(context, iniciaSOS)
                        //}, 15000)
                    }, 30000)

                }else{
                    Log.d("PAROU?","Parou?")
                    handler?.removeCallbacksAndMessages(null)
                }
            }
            if (iniciaSOS) {
                Log.d("SOS","entrou no parar teria que parar")
                Log.d("SOS","valor do sos ${iniciaSOS}")
                iniciaSOS = false
                startSendSmsRepetidamente(context, false)
                Toast.makeText(context, "SOS parou", Toast.LENGTH_SHORT).show()
            } else {
                //startLocationService(context)
                Log.d("SOS","entrou no iniciar teria que iniciar")
                Log.d("SOS","valor do sos ${iniciaSOS}")
                iniciaSOS = true
                Log.d("WIDGET", "SOS iniciou")
                Log.d("WIDGET INICIOU", "VALOR: ${iniciaSOS}")
                Toast.makeText(context, "SOS iniciou", Toast.LENGTH_SHORT).show()
                startSendSmsRepetidamente(context, iniciaSOS)
            }
            //sharedPreferences.edit().putBoolean("iniciaLocalizacao", iniciaLocalizacao).apply()
            Log.d("Acima", "sharedPreferences: ${iniciaSOS}")
            sharedPreferences.edit().putBoolean("iniciaSOS", iniciaSOS).apply()

            Toast.makeText(context, "WIDGET CLICK", Toast.LENGTH_SHORT).show()
        }
    }
    private fun startLocationService(context: Context) {
        val serviceIntent = Intent(context, ServicoLocalizacao::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }

    private fun stopLocationService(context: Context) {
        val serviceIntent = Intent(context, ServicoLocalizacao::class.java)
        context.stopService(serviceIntent)
    }
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.sos_widget)
        val intent = Intent(context, SosWidget::class.java)
        intent.action = "CLICK_BTN"
        val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId,intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.btn_widget, pendingIntent)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

