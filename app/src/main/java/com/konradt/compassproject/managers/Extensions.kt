package com.konradt.compassproject.managers

import android.location.Location

fun Float.toRoundDouble(): Double {
    return this.toString().toDouble()
}

fun Location.getLatitudeLabel(): String {
    return "lat: ${this.latitude}"
}

fun Location.getLongitudeLabel(): String {
    return "long: ${this.longitude}"
}