package com.konradt.compassproject.models

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class Compass(private val context: Context) {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var magneticField: Sensor

    private var rotationMatrix = FloatArray(9)
    private var gravity = FloatArray(3)
    private var geoMagnetic = FloatArray(3)
    private var orientationValues = FloatArray(3)

    private val alpha = 0.99f

    var rotation = 0.0f

    init {
        initSensorManager()
        initSensors()
    }

    private fun initSensorManager() {
        sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private fun initSensors() {
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    fun registerListeners(listener: SensorEventListener) {
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(listener, magneticField, SensorManager.SENSOR_DELAY_GAME)
    }

    fun unregisterListeners(listener: SensorEventListener) {
        sensorManager.unregisterListener(listener)
    }

    fun calculateRotation(event: SensorEvent) {
        setSensorValues(event)
        calculateRotationMatrix()
        calculateOrientation()
        rotation = getRotationFromAzimuth()
    }

    private fun setSensorValues(event: SensorEvent) {
        if (event.sensor == accelerometer) filterAccelerometerNoise(event)
        else if (event.sensor == magneticField) geoMagnetic = event.values
    }

    private fun filterAccelerometerNoise(event: SensorEvent) {
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]
    }

    private fun calculateRotationMatrix() {
        SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geoMagnetic)
    }

    private fun calculateOrientation() {
        SensorManager.getOrientation(rotationMatrix, orientationValues)
    }

    private fun getRotationFromAzimuth(): Float {
        val azimuth = orientationValues[0]
        return Math.toDegrees(azimuth.toDouble()).toFloat()
    }
}