package com.minutecodes.satelspeed.activities.fragments

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.minutecodes.satelspeed.R
import com.minutecodes.satelspeed.activities.helpers.SelectionViewSwitcher
import com.minutecodes.satelspeed.activities.model.LocationData
import com.minutecodes.satelspeed.activities.util.GpsEvent
import com.minutecodes.satelspeed.activities.viewmodel.SharedLocationViewModel
import com.minutecodes.satelspeed.databinding.LolBinding

class SpeedometerFragment : Fragment(), View.OnClickListener {
    private var lolBinding: LolBinding? = null
    private val binding get() = lolBinding!!
    private val selectionViewSwitcher = SelectionViewSwitcher()
    private lateinit var sharedLocationViewModel: SharedLocationViewModel
    private val gpsEvent = GpsEvent(lifecycle)

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.forward_selection_button -> showNextView(v)
            R.id.back_selection_button -> showPreviousView(v)
            R.id.recordController -> {
                //TODO Start a service, bind to the activity, and keep recording event there.
                //TODO On stop, remove service and store in the DB
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        lolBinding = LolBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyToolTips()
        setupSwitchableViews()

        binding.also {
            it.recordController.setOnClickListener(this)
            it.backSelectionButton.setOnClickListener(this)
            it.forwardSelectionButton.setOnClickListener(this)
        }

        with(gpsEvent) {
            setErrorEventView(binding.gpsError)
            setWarnEventView(binding.gpsWarn)
            bindLifeCycle()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(ViewModelProvider(requireActivity())) {
            sharedLocationViewModel = get(SharedLocationViewModel::class.java)
        }

        sharedLocationViewModel.locationAvailability().observe(viewLifecycleOwner, Observer {
            gpsEvent.showError(it)
        })

        sharedLocationViewModel.locationData().observe(viewLifecycleOwner, Observer {
            updateViews(it)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()

        gpsEvent.removeViews()
        gpsEvent.unbindLifeCycle()
        removeViewReferences()
    }

    private fun applyToolTips() {
        with(binding) {
            TooltipCompat.setTooltipText(signalStrength, "GPS signal")
            TooltipCompat.setTooltipText(recordController, "Record this session")
        }
    }

    private fun removeViewReferences() {
        lolBinding = null
        selectionViewSwitcher.clearAllViews()
    }

    private fun showNextView(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        selectionViewSwitcher.nextView()
    }

    private fun showPreviousView(v: View) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        selectionViewSwitcher.previousView()
    }

    private fun setupSwitchableViews() {
        with(selectionViewSwitcher) {
            addSwitchableView(lolBinding!!.avgSpeed)
            addSwitchableView(lolBinding!!.maxSpeed)
            addSwitchableView(lolBinding!!.recordController)
        }
    }

    private fun updateViews(it: LocationData) {
        binding.apply {
            signalStrength.signalLevel = it.accuracy
            currentSpeed.text = it.speed.toInt().toString()
            distanceCovered.text = "${it.distanceCovered} Km"
            maxSpeed.text = "Max " + it.topSpeed.toInt().toString() + " Km/h"
            avgSpeed.text = "Avg " + it.averageSpeed.toInt().toString() + " Km/h"
        }
    }

}
