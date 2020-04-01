package com.minutecodes.satelspeed.activities.activities

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.minutecodes.satelspeed.R
import com.minutecodes.satelspeed.activities.PERMISSION_REQUEST_CODE
import com.minutecodes.satelspeed.activities.SPEEDOMETER_FRAG_NAME
import com.minutecodes.satelspeed.activities.fragments.HistoryFragment
import com.minutecodes.satelspeed.activities.fragments.MapViewFragment
import com.minutecodes.satelspeed.activities.fragments.SpeedometerFragment
import com.minutecodes.satelspeed.activities.interfaces.BottomNavigationListener
import com.minutecodes.satelspeed.activities.viewmodel.DataViewModel
import com.minutecodes.satelspeed.activities.viewmodel.LocationViewModel
import com.minutecodes.satelspeed.activities.viewmodel.SharedLocationViewModel
import com.minutecodes.satelspeed.activities.viewmodel.factory.DataViewModelFactory
import com.minutecodes.satelspeed.activities.viewmodel.factory.LocationViewModelFactory
import com.minutecodes.satelspeed.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationListener {
    private lateinit var dataViewModel: DataViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var sharedLocationViewModel: SharedLocationViewModel

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        performHapticFeedback()
        when (item.itemId) {
            R.id.settings -> openSettings()
            R.id.mapView -> openFragment(MapViewFragment(), "mapView")
            R.id.speedHistory -> openFragment(HistoryFragment(), "history")
            R.id.speedoMeter -> openFragment(SpeedometerFragment(), SPEEDOMETER_FRAG_NAME)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        initializeViewModels()

        with(activityMainBinding.bottomNavigation) {
            setOnNavigationItemSelectedListener(this@MainActivity)
            setOnNavigationItemReselectedListener(this@MainActivity)
        }

        requestHighAccuracyGps()

        if (!isLocationPermissionGranted()) {
            requestLocationPermission()
        }


        locationViewModel.locationAvailability().observe(this, Observer {
            sharedLocationViewModel.setLocationAvailability(it)
        })

        locationViewModel.locationData()?.observe(this, Observer {
            val speedUnit = dataViewModel.getSpeedUnit()
            val distanceUnit = dataViewModel.getDistanceUnit()
            sharedLocationViewModel.setSystemUnits(speedUnit, distanceUnit)
            sharedLocationViewModel.setLocation(it)
        })

    }

    override fun onResume() {
        super.onResume()

        applyImmersiveMode()

        locationViewModel.startLocationUpdates()
    }

    override fun onPostResume() {
        super.onPostResume()

        if (isFragmentContainerEmpty()) {
            openFragment(SpeedometerFragment(), SPEEDOMETER_FRAG_NAME)
        }

    }

    override fun onPause() {
        super.onPause()

        locationViewModel.stopLocationUpdates()
    }

    private fun isFragmentContainerEmpty(): Boolean {
        return supportFragmentManager.findFragmentById(activityMainBinding.fragmentContainer.id) == null
    }

    private fun performHapticFeedback() {
        activityMainBinding.bottomNavigation.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }

    private fun getDataViewModel(): DataViewModel {
        return ViewModelProvider(this, DataViewModelFactory(application))[DataViewModel::class.java]
    }

    private fun getSharedLocationViewModel(): SharedLocationViewModel {
        return ViewModelProvider(this)[(SharedLocationViewModel::class.java)]
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }

    private fun openSettings() {

    }

    private fun openFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment, tag).commit()
    }

    private fun isLocationPermissionGranted(): Boolean {
        val isGranted = PackageManager.PERMISSION_GRANTED
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        return ContextCompat.checkSelfPermission(this, locationPermission) == isGranted
    }

    private fun applyImmersiveMode() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun initializeViewModels() {
        dataViewModel = getDataViewModel()
        locationViewModel = getLocationViewModel()
        sharedLocationViewModel = getSharedLocationViewModel()
    }

    private fun getLocationViewModel(): LocationViewModel {
        return ViewModelProvider(
            this,
            LocationViewModelFactory(this)
        )[LocationViewModel::class.java]
    }

    private fun createHighAccuracyRequest(exception: ResolvableApiException) {
        try {
            exception.startResolutionForResult(this@MainActivity, 0x1)
        } catch (sendEx: IntentSender.SendIntentException) {
            // Ignore the error.
        }
    }

    private fun requestHighAccuracyGps() {
        val locationRequest = locationViewModel.getLocationRequest()
        val settingBuilder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(settingBuilder.build())

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                createHighAccuracyRequest(it)
            }
        }
    }

}
