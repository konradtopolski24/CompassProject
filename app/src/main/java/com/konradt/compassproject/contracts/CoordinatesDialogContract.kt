package com.konradt.compassproject.contracts

import android.content.Context
import android.content.DialogInterface

interface CoordinatesDialogContract {

    interface View {
        fun setLatitudeText(text: String)
        fun setLongitudeText(text: String)
        fun showToast(stringId: Int)
    }

    interface Presenter {
        fun checkLatitude(context: Context)
        fun checkLongitude(context: Context)
        fun saveCoordinates(context: Context, latitudeText: String, longitudeText: String)
        fun closeDialog(dialog: DialogInterface)
    }
}