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
        getGpsAtivado()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d("Teste", "teste")
            if(getGpsAtivado()){
                getLocation()
            }else{
                /*Log.d("Get Gps","${getGpsAtivado()}")
                while(getGpsAtivado()){
                    Log.d("Get Gps","${getGpsAtivado()}")
                    getGpsAtivado()
                }*/
                do{
                    enableGpsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(enableGpsIntent)
                    //getGpsAtivado()
                }while (!isGpsEnabled)
            }
        }else{
            Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
        return START_STICKY
    }

    private fun getGpsAtivado(): Boolean{
        val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        Log.d("Get Gps ativado valor gps","${isGpsEnabled}")
        if(isGpsEnabled){
            getLocation()
            return true
        }else{
            enableGpsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(enableGpsIntent)
            getGpsAtivado()
        }
        return false
    }

    private fun getLocation() {
        /*val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled) {
            showEnableGpsDialog()
            Toast.makeText(applicationContext,"Por favor, habilite o GPS",Toast.LENGTH_SHORT).show()

            val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

            enableGpsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(enableGpsIntent)

            return
        }*/
        Log.d("Get Location","Get location")
            val Gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val Network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val locationListener: LocationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val latitude = location.latitude.toString()
                        val longitude = location.longitude.toString()

                        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                        val ultimaAtualizacaoFormat = dateFormat.format(Date(ultimaAtualizacaoDaLocalizacao)).toString()

                        val broadcastIntent = Intent(LOCATION_UPDATE_ACTION).apply {
                            putExtra("latitude",latitude)
                            putExtra("longitude",longitude)
                            putExtra("ultimaAtt",ultimaAtualizacaoFormat)
                        }
                        ultimaAtualizacaoDaLocalizacao = System.currentTimeMillis()
                        sendBroadcast(broadcastIntent)
                        /*Toast.makeText(
                            applicationContext,
                            "Latitude: $latitude, Longitude: $longitude",
                            Toast.LENGTH_SHORT
                        ).show()
                        */
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}
                }

                if (Gps) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,
                        0f,
                        locationListener
                    )
                    Toast.makeText(this, "Buscando pelo Gps", Toast.LENGTH_SHORT).show()
                }
                if (Network) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000,
                        0f,
                        locationListener
                    )
                    Toast.makeText(this, "Buscando pela Internet", Toast.LENGTH_SHORT).show()
                }

                val ultimaLocalizacaoGps: Location? =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (ultimaLocalizacaoGps != null) {
                    val latitude = ultimaLocalizacaoGps.latitude
                    val longitude = ultimaLocalizacaoGps.longitude
                    Toast.makeText(applicationContext,"Ultima localizacao Gps Latitude: $latitude, Longitude: $longitude conhecida",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Última localização GPS não disponível", Toast.LENGTH_SHORT).show() }
            }
            else{
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
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
