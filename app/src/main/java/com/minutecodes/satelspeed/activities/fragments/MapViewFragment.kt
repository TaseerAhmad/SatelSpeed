package com.minutecodes.satelspeed.activities.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.minutecodes.satelspeed.R
import com.minutecodes.satelspeed.activities.model.LocationData
import com.minutecodes.satelspeed.activities.viewmodel.DataViewModel
import com.minutecodes.satelspeed.activities.viewmodel.SharedLocationViewModel
import com.minutecodes.satelspeed.activities.viewmodel.factory.DataViewModelFactory
import com.minutecodes.satelspeed.databinding.MapviewFragmentBinding
import java.util.*
import kotlin.math.roundToInt


class MapViewFragment : Fragment(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var mapBox: MapboxMap
    private lateinit var dataViewModel: DataViewModel
    private lateinit var sharedLocationViewModel: SharedLocationViewModel
    private var mapViewBinding: MapviewFragmentBinding? = null
    private val binding get() = mapViewBinding!!
    private val cameraPosition = CameraPosition.Builder()

    override fun onClick(v: View?) {
        var camZoom = mapBox.cameraPosition.zoom
        when (v?.id) {
            R.id.zoomIn -> mapBox.cameraPosition = cameraPosition.zoom(++camZoom).build()
            R.id.zoomOut -> mapBox.cameraPosition = cameraPosition.zoom(--camZoom).build()
            R.id.reTrackCamera -> mapBox.locationComponent.cameraMode = CameraMode.TRACKING
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mapBox = mapboxMap

        binding.mapView!!.addOnDidFailLoadingMapListener {
            Toast.makeText(
                requireActivity().applicationContext,
                "Map loading error",
                Toast.LENGTH_LONG
            ).show()
        }

        cameraPosition.tilt(dataViewModel.getMapCamTilt())

        mapBox.setStyle(dataViewModel.getMapThemeId()) {
            mapBox.uiSettings.isCompassEnabled = false
            val zoom = dataViewModel.getMapCameraHeight()

            mapBox.cameraPosition = cameraPosition.zoom(zoom).build()

            mapBox.locationComponent.addOnCameraTrackingChangedListener(object :
                OnCameraTrackingChangedListener {
                private val mainHandler = Handler(Looper.getMainLooper())

                override fun onCameraTrackingChanged(currentMode: Int) {
                    if (currentMode == CameraMode.TRACKING)
                        binding.reTrackCamera!!.visibility = View.GONE

                }

                override fun onCameraTrackingDismissed() {
                    mainHandler.postDelayed({
                        binding.reTrackCamera!!.visibility = View.VISIBLE
                    }, 200)

                }
            })

            mapBox.addOnMapClickListener {
                handleZoomControllerVisibility()
                true
            }

            enableLocationComponent(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        mapViewBinding = MapviewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TooltipCompat.setTooltipText(binding.signalStrength!!, "GPS signal")

        binding.toolBar!!.inflateMenu(R.menu.map_theme_menu)
        binding.toolBar!!.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.darkTheme -> mapBox.setStyle(Style.DARK)
                R.id.lightTheme -> mapBox.setStyle(Style.LIGHT)
                R.id.outDoorTheme -> mapBox.setStyle(Style.OUTDOORS)
                R.id.satelliteTheme -> mapBox.setStyle(Style.SATELLITE)
                R.id.streetTheme -> mapBox.setStyle(Style.MAPBOX_STREETS)
                R.id.satelliteStreetTheme -> mapBox.setStyle(Style.SATELLITE_STREETS)
            }
            mapBox.getStyle { style ->
                dataViewModel.saveMapThemeId(style.uri)
            }
            false
        }

        binding.mapView!!.also {
            it.onCreate(savedInstanceState)
            it.getMapAsync(this)
            it.setOnClickListener(this)
        }

        binding.zoomIn!!.setOnClickListener(this)
        binding.zoomOut!!.setOnClickListener(this)
        binding.reTrackCamera!!.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataViewModel = getDataViewModel()
        sharedLocationViewModel = getSharedLocationViewModel()
        sharedLocationViewModel.locationData().observe(viewLifecycleOwner, Observer {
            updateViews(it)
        })

    }

    private fun updateViews(data: LocationData) {
        with(binding) {
            signalStrength!!.signalLevel = data.accuracy
            distanceCovered!!.text = "${data.distanceCovered} Km"
            currentSpeed!!.text = data.speed.roundToInt().toString()
        }
    }

    override fun onStart() {
        super.onStart()

        binding.mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()

        binding.mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()

        dataViewModel.saveMapCamTilt(mapBox.cameraPosition.tilt)
        dataViewModel.saveMapCameraHeight(mapBox.cameraPosition.zoom)

        binding.mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        mapBox.locationComponent.isLocationComponentEnabled = false
        binding.mapView!!.onDestroy()

        mapViewBinding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()

        binding.mapView!!.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        binding.mapView!!.onSaveInstanceState(outState)
    }

    private fun handleZoomControllerVisibility() {
        binding.zoomIn!!.isVisible = !binding.zoomOut!!.isVisible
        binding.zoomOut!!.isVisible = !binding.zoomOut!!.isVisible
    }

    private fun getSharedLocationViewModel(): SharedLocationViewModel {
        return if (::sharedLocationViewModel.isInitialized) {
            sharedLocationViewModel
        } else {
            ViewModelProvider(requireActivity())[SharedLocationViewModel::class.java]
        }
    }

    private fun getDataViewModel(): DataViewModel {
        val appContext = requireActivity().application
        return ViewModelProvider(requireActivity(), DataViewModelFactory(appContext))
            .get(DataViewModel::class.java)
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .trackingGesturesManagement(true)
                .accuracyAnimationEnabled(true)
                .accuracyColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                .build()

            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            mapBox.locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.GPS
            }
        }
    }

}