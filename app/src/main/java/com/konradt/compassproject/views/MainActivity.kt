package com.konradt.compassproject.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.konradt.compassproject.R
import com.konradt.compassproject.contracts.MainActivityContract
import com.konradt.compassproject.presenters.MainActivityPresenter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askPermission()
        presenter = MainActivityPresenter(this)
        presenter.initCompass(this)
        presenter.initDestinationDetector(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.registerCompassListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.registerCompassListeners()
    }

    override fun onPause() {
        super.onPause()
        presenter.unregisterCompassListeners()
    }

    override fun onStop() {
        super.onStop()
        presenter.unregisterCompassListeners()
    }

    override fun rotateCompass(rotation: Float) {
        ivCompass.rotation = rotation
    }

    override fun rotateArrow(rotation: Float) {
        ivArrow.rotation = rotation
    }

    override fun showArrow() {
        ivArrow.visibility = View.VISIBLE
    }

    override fun hideArrow() {
        ivArrow.visibility = View.INVISIBLE
    }

    override fun setCurrentCoordinatesText(latitudeText: String, longitudeText: String) {
        tvCurrentLat.text = latitudeText
        tvCurrentLong.text = longitudeText
    }

    override fun setDestinationCoordinatesText(latitudeText: String, longitudeText: String) {
        tvDestinationLat.text = latitudeText
        tvDestinationLong.text = longitudeText
    }

    override fun setCurrentAddressText(text: String) {
        tvCurrent.text = text
    }

    override fun setDestinationAddressText(text: String) {
        tvDestination.text = text
    }

    override fun setEmptyCurrentText() {
        tvCurrent.text = getString(R.string.er_current)
        tvCurrentLat.text = getString(R.string.er_coordinates)
        tvCurrentLong.text = getString(R.string.er_coordinates)
    }

    override fun setEmptyDestinationText() {
        tvDestination.text = getString(R.string.er_destination)
        tvDestinationLat.text = getString(R.string.er_coordinates)
        tvDestinationLong.text = getString(R.string.er_coordinates)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity, menu)
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.it_coordinates) {
            presenter.showDialog(getString(R.string.bt_coordinates), supportFragmentManager)
            return true
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun askPermission() {
        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}