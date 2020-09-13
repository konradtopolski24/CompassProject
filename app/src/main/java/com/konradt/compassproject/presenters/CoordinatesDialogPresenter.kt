package com.konradt.compassproject.presenters

import android.content.Context
import android.content.DialogInterface
import com.konradt.compassproject.R
import com.konradt.compassproject.contracts.CoordinatesDialogContract
import com.konradt.compassproject.managers.PreferencesManager.getLatitude
import com.konradt.compassproject.managers.PreferencesManager.getLongitude
import com.konradt.compassproject.managers.PreferencesManager.isLatitudeAvailable
import com.konradt.compassproject.managers.PreferencesManager.isLongitudeAvailable
import com.konradt.compassproject.managers.PreferencesManager.removeLatitude
import com.konradt.compassproject.managers.PreferencesManager.removeLongitude
import com.konradt.compassproject.managers.PreferencesManager.setLatitude
import com.konradt.compassproject.managers.PreferencesManager.setLongitude

class CoordinatesDialogPresenter(private val view: CoordinatesDialogContract.View) :
    CoordinatesDialogContract.Presenter {

    override fun checkLatitude(context: Context) {
        if (isLatitudeAvailable(context)) {
            view.setLatitudeText(getLatitude(context).toString())
        }
    }

    override fun checkLongitude(context: Context) {
        if (isLongitudeAvailable(context)) {
            view.setLongitudeText(getLongitude(context).toString())
        }
    }

    override fun saveCoordinates(context: Context, latitudeText: String, longitudeText: String) {
        if (latitudeText.isEmpty() || longitudeText.isEmpty()) {
            removeLatitude(context)
            removeLongitude(context)
            view.showToast(R.string.bt_reset)
        } else if (areCoordinatesCorrect(latitudeText.toDouble(), longitudeText.toDouble())) {
            setLatitude(context, latitudeText.toDouble())
            setLongitude(context, longitudeText.toDouble())
            view.showToast(R.string.bt_values)
        } else view.showToast(R.string.er_values)
    }

    override fun closeDialog(dialog: DialogInterface) {
        dialog.dismiss()
    }

    private fun areCoordinatesCorrect(latitude: Double, longitude: Double): Boolean {
        return latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180
    }
}