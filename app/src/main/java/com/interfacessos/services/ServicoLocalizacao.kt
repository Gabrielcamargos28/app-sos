package com.interfacessos.services

import android.Manifest
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.Date

class ServicoLocalizacao: Service(){

    private lateinit var locationManager: LocationManager
    private var ultimaAtualizacaoDaLocalizacao: Long = 0


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        getLocation()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SeuServico", "O serviço foi iniciado")
        getLocation()
        return START_STICKY
    }
    private fun getLocation() {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            showEnableGpsDialog()
            Toast.makeText(
                applicationContext,
                "Por favor, habilite o GPS",
                Toast.LENGTH_SHORT
            ).show()
            val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            enableGpsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(enableGpsIntent)
            return
        }

        val Gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val Network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()

                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val ultimaAtualizacaoFormat = dateFormat.format(Date(ultimaAtualizacaoDaLocalizacao)).toString()

                val broadcastIntent = Intent(LOCATION_UPDATE_ACTION).apply {
                    putExtra("latitude",latitude)
                    putExtra("longitude",longitude)
                    Log.d("BroadcastIntent","intent broadcast")
                    putExtra("ultimaAtt",ultimaAtualizacaoFormat)
                    Log.d("ultimaAtualizacaoDaLocalizacao", "${ultimaAtualizacaoDaLocalizacao}")
                    Log.d("ultimaAtualizacaoFormat","${ultimaAtualizacaoFormat}")
                }
                sendBroadcast(broadcastIntent)

                Toast.makeText(
                    applicationContext,
                    "Latitude: $latitude, Longitude: $longitude",
                    Toast.LENGTH_SHORT
                ).show()
                ultimaAtualizacaoDaLocalizacao = System.currentTimeMillis()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (Gps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0f,
                    locationListener
                )
            }
            if (Network) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0f,
                    locationListener
                )
            }

            val ultimaLocalizacao: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (ultimaLocalizacao != null) {
                val latitude = ultimaLocalizacao.latitude
                val longitude = ultimaLocalizacao.longitude
                val horarioUltimaLocalizacao = ultimaAtualizacaoDaLocalizacao

                Toast.makeText(
                    applicationContext,
                    "Ultima Latitude: $latitude, Longitude: $longitude conhecida",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Última localização GPS não disponível",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showEnableGpsDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder
            .setMessage("O GPS está desligado. Deseja ativá-lo?")
            .setCancelable(false)
            .setPositiveButton("Sim") { dialog, which ->
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Não") { dialog, which ->
                dialog.cancel()
            }
        val alert = alertDialogBuilder.create()
        alert.show()
    }
    companion object{
        const val LOCATION_UPDATE_ACTION = "com.interfacessos.services.LOCATION_UPDATE"
    }
}
