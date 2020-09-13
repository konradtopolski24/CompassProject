package com.konradt.compassproject.models

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.hardware.GeomagneticField
import android.location.*
import androidx.core.app.ActivityCompat
import com.konradt.compassproject.R
import com.konradt.compassproject.managers.PreferencesManager
import java.util.*

class Detector(private val context: Context) {

    private lateinit var locationManager: LocationManager

    var currentLocation: Location? = null
    var destinationLocation: Location? = null

    var rotation = 0.0f

    init {
        initLocationManager()
    }

    private fun initLocationManager() {
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private fun isNetworkEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun initDestinationLocation() {
        destinationLocation = Location("")
        destinationLocation!!.latitude = PreferencesManager.getLatitude(context)
        destinationLocation!!.longitude = PreferencesManager.getLongitude(context)
    }

    fun registerListener(listener: LocationListener) {
        if (isNetworkEnabled() && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0f, listener
            )
        }
    }

    fun setFirstLocation() {
        if (isNetworkEnabled() && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location: Location? =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            currentLocation = location
        }
    }

    fun getCurrentAddress(): String {
        return getAddress(context, currentLocation!!, R.string.tv_current)
    }

    fun getDestinationAddress(): String {
        return getAddress(context, destinationLocation!!, R.string.tv_destination)
    }

    private fun getAddress(context: Context, location: Location, stringId: Int): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return if (addresses.isNotEmpty()) {
            val address: String = addresses[0].getAddressLine(0)
            "${context.getString(stringId)}: $address"
        } else "${context.getString(stringId)}: ${context.getString(R.string.er_address)}"
    }

    fun calculateRotation(compassRotation: Float) {
        val geoField = GeomagneticField(
            currentLocation!!.latitude.toFloat(),
            currentLocation!!.longitude.toFloat(),
            currentLocation!!.altitude.toFloat(),
            System.currentTimeMillis()
        )
        val trueNorth = compassRotation + geoField.declination
        val bearing = currentLocation!!.bearingTo(destinationLocation)
        rotation = trueNorth - bearing
    }

    fun areCoordinatesAvailable(): Boolean {
        return PreferencesManager.areCoordinatesAvailable(context)
    }
}