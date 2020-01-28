package com.minutecodes.satelspeed.activities.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.minutecodes.satelspeed.R
import com.minutecodes.satelspeed.activities.BAD_GPS
import com.minutecodes.satelspeed.activities.GPS_UNAVAILABLE
import com.minutecodes.satelspeed.activities.PERMISSION_REQUEST_CODE
import com.minutecodes.satelspeed.activities.fragments.GeoDataUnitEditor
import com.minutecodes.satelspeed.activities.viewmodel.LocationViewModel
import com.minutecodes.satelspeed.databinding.TestBinding
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.trip_details.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: TestBinding
    private lateinit var exitDialog: AlertDialog.Builder
    private lateinit var snackbar: Snackbar
    private val alerter by lazy { Alerter.create(this) }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.test)

        val view = binding.tripDetailsInclude.tripResetImgButton
        TooltipCompat.setTooltipText(view, "Reset")

        val context = this
        snackbar = Snackbar.make(binding.tripCardView, "", Snackbar.LENGTH_INDEFINITE)
        var viewModel: LocationViewModel
        exitDialog = makeExitDialog()
        //alerter = createDefaultAlerter()


        binding.drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }
        })

        ViewModelProviders.of(context)[LocationViewModel(application)::class.java].apply {
            binding.geoDataCardView.setOnLongClickListener {
                it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
//                supportFragmentManager.beginTransaction().add(GeoDataUnitEditor(), "geoEditor")
//                    .commit()
                val view = layoutInflater.inflate(R.layout.geodata_unit_editor, null, false)
                AlertDialog.Builder(this@MainActivity, R.style.AlertDialogTheme).setView(view).apply {
                    setPositiveButton("Set", { dialog, which ->  }) //TODO For test purpose only
                    setNegativeButton("Close", { dialog, which -> })
                    create()
                    show()
                }
                true
            }
            lifecycle.addObserver(this)
            locationAvailability().observe(
                context,
                Observer { handleGpsError(it, GPS_UNAVAILABLE) })
            gpsQualityAvailability().observe(context, Observer { handleGpsError(it, BAD_GPS) })
        }.also {
            binding.apply {
                viewmodel = it
                lifecycleOwner = context
            }
            viewModel = it
        }

        if (!locationPermissionAvailable()) {
            askLocationPermission()
        }

        val locationRequest = viewModel.getLocationRequest()
        val settingBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(settingBuilder.build())

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                requestHighAccuracyGps(it)
            }
        }
        holdWakeLock()
    }

    override fun onBackPressed() {
        exitDialog.show()
    }

    private fun locationPermissionAvailable(): Boolean {
        val isGranted = PackageManager.PERMISSION_GRANTED
        val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        return ContextCompat.checkSelfPermission(this, locationPermission) == isGranted
    }

    private fun askLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }

    private fun requestHighAccuracyGps(exception: ResolvableApiException) {
        try {
            exception.startResolutionForResult(this@MainActivity, 0x1)
        } catch (sendEx: IntentSender.SendIntentException) {
            // Ignore the error.
        }
    }

    private fun makeExitDialog(): AlertDialog.Builder {
        return AlertDialog.Builder(this).apply {
            setTitle("Exit app?")
            setPositiveButton("EXIT") { dialog, _ ->
                dialog.dismiss()
                super.onBackPressed()
            }
            setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun createDefaultAlerter(): Alerter {
        return Alerter.create(this).apply {
            setBackgroundColorRes(R.color.gpsErrorDialog)
            enableProgress(true)
            setProgressColorInt(R.color.pureWhite)
            setDismissable(false)
            enableInfiniteDuration(true)
        }
    }

    private fun handleGpsError(isGpsAvailable: Boolean, message: String) {
        if (!isGpsAvailable) {
//            snackbar.setText(message).show() //TODO Remove snack bar, temporary display
//            Alerter.isShowing
//            if (!Alerter.isShowing)
//                alerter.setText(message).show()
        } else {

//            snackbar.dismiss()
//            Alerter.hide()
        }
    }

    private fun holdWakeLock() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

}
