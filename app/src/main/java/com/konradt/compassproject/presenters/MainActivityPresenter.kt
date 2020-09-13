package com.konradt.compassproject.presenters

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.konradt.compassproject.contracts.MainActivityContract
import com.konradt.compassproject.managers.getLatitudeLabel
import com.konradt.compassproject.managers.getLongitudeLabel
import com.konradt.compassproject.models.Compass
import com.konradt.compassproject.models.Detector
import com.konradt.compassproject.views.CoordinatesDialog

class MainActivityPresenter(private val view: MainActivityContract.View) :
    MainActivityContract.Presenter, SensorEventListener, LocationListener {

    private lateinit var compass: Compass
    private lateinit var detector: Detector

    override fun initCompass(context: Context) {
        compass = Compass(context)
        compass.registerListeners(this)
    }

    override fun registerCompassListeners() {
        compass.registerListeners(this)
    }

    override fun unregisterCompassListeners() {
        compass.unregisterListeners(this)
    }

    override fun initDestinationDetector(context: Context) {
        view.setEmptyCurrentText()
        detector = Detector(context)
        detector.registerListener(this)
        detector.setFirstLocation()
        if (detector.currentLocation != null) setCurrentText()
    }

    override fun showDialog(title: String, fragmentManager: FragmentManager) {
        val dialog = CoordinatesDialog.newInstance(title)
        dialog.show(fragmentManager, null)
    }

    override fun onSensorChanged(event: SensorEvent) {
        compass.calculateRotation(event)
        view.rotateCompass(-compass.rotation)
        if (detector.areCoordinatesAvailable() && detector.currentLocation != null) {
            view.showArrow()
            detector.initDestinationLocation()
            setDestinationText()
            detector.calculateRotation(compass.rotation)
            view.rotateArrow(-detector.rotation)
        } else {
            view.hideArrow()
            view.setEmptyDestinationText()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            detector.currentLocation = location
            setCurrentText()
        } else view.setEmptyCurrentText()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    private fun setCurrentText() {
        val location = detector.currentLocation!!
        view.setCurrentCoordinatesText(
            location.getLatitudeLabel(),
            location.getLongitudeLabel()
        )
        view.setCurrentAddressText(detector.getCurrentAddress())
    }

    private fun setDestinationText() {
        val location = detector.destinationLocation!!
        view.setDestinationCoordinatesText(
            location.getLatitudeLabel(),
            location.getLongitudeLabel()
        )
        view.setDestinationAddressText(detector.getDestinationAddress())
    }
}