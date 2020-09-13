package com.konradt.compassproject.managers

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREFERENCES_NAME = "CompassPreferences"
    private const val KEY_LATITUDE = "latitude"
    private const val KEY_LONGITUDE = "longitude"
    private const val DEFAULT_FLOAT = 0.0f

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getLatitude(context: Context): Double {
        return getFloat(context, KEY_LATITUDE).toRoundDouble()
    }

    fun setLatitude(context: Context, latitude: Double) {
        setFloat(context, KEY_LATITUDE, latitude.toFloat())
    }

    fun getLongitude(context: Context): Double {
        return getFloat(context, KEY_LONGITUDE).toRoundDouble()
    }

    fun setLongitude(context: Context, longitude: Double) {
        setFloat(context, KEY_LONGITUDE, longitude.toFloat())
    }

    private fun getFloat(context: Context, key: String): Float {
        val preferences = getPreferences(context)
        return preferences.getFloat(key, DEFAULT_FLOAT)
    }

    private fun setFloat(context: Context, key: String, value: Float) {
        val preferences = getPreferences(context)
        preferences.edit().putFloat(key, value).apply()
    }

    fun areCoordinatesAvailable(context: Context): Boolean {
        return isLatitudeAvailable(context) && isLongitudeAvailable(context)
    }

    fun isLatitudeAvailable(context: Context): Boolean {
        return isValueAvailable(context, KEY_LATITUDE)
    }

    fun isLongitudeAvailable(context: Context): Boolean {
        return isValueAvailable(context, KEY_LONGITUDE)
    }

    private fun isValueAvailable(context: Context, key: String): Boolean {
        val preferences = getPreferences(context)
        return preferences.contains(key)
    }

    fun removeLatitude(context: Context) {
        removeValue(context, KEY_LATITUDE)
    }

    fun removeLongitude(context: Context) {
        removeValue(context, KEY_LONGITUDE)
    }

    private fun removeValue(context: Context, key: String) {
        val preferences = getPreferences(context)
        preferences.edit().remove(key).apply()
    }
}