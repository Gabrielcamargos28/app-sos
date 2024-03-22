package com.interfacessos.ui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.widget.RemoteViews
import com.interfacessos.R
import com.interfacessos.database.DBHelper

/**
 * Implementation of App Widget functionality.
 */
class SosWidget : AppWidgetProvider() {

    private lateinit var locationManager: LocationManager
    private lateinit var dbHelper: DBHelper

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

    /*override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        dbHelper = DBHelper(context!!)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var iniciaSOS = sharedPreferences.getBoolean("iniciaSOS", false)
        val serviceSms = ServicoSms(dbHelper,context)
        //var iniciaLocalizacao = sharedPreferences.getBoolean("iniciaLocalizacao", false)
        if(intent?.action == "CLICK_BTN"){
            if (iniciaSOS) {
                iniciaSOS = false
                serviceSms.startSendSmsRepetidamente(context, iniciaSOS)
                Toast.makeText(context, "SOS parou", Toast.LENGTH_SHORT).show()
                } else {
                    iniciaSOS = true
                    Toast.makeText(context, "SOS iniciou", Toast.LENGTH_SHORT).show()
                    serviceSms.startSendSmsRepetidamente(context, iniciaSOS)
                }

                //sharedPreferences.edit().putBoolean("iniciaLocalizacao", iniciaLocalizacao).apply()
                sharedPreferences.edit().putBoolean("iniciaSOS", iniciaSOS).apply()

                Toast.makeText(context, "WIDGET CLICK", Toast.LENGTH_SHORT).show()
        }
    }*/

    fun iniciarSOSRepetidamente(){

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
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