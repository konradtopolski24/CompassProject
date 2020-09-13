package com.konradt.compassproject.contracts

import android.content.Context
import androidx.fragment.app.FragmentManager

interface MainActivityContract {

    interface View {
        fun rotateCompass(rotation: Float)
        fun rotateArrow(rotation: Float)
        fun showArrow()
        fun hideArrow()
        fun setCurrentCoordinatesText(latitudeText: String, longitudeText: String)
        fun setDestinationCoordinatesText(latitudeText: String, longitudeText: String)
        fun setCurrentAddressText(text: String)
        fun setDestinationAddressText(text: String)
        fun setEmptyCurrentText()
        fun setEmptyDestinationText()
    }

    interface Presenter {
        fun initCompass(context: Context)
        fun registerCompassListeners()
        fun unregisterCompassListeners()
        fun initDestinationDetector(context: Context)
        fun showDialog(title: String, fragmentManager: FragmentManager)
    }
}