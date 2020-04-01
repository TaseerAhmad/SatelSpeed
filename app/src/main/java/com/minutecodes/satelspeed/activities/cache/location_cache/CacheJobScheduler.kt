package com.minutecodes.satelspeed.activities.cache.location_cache

import android.os.CountDownTimer
import com.minutecodes.satelspeed.activities.COUNT_DOWN_INTERVAL
import com.minutecodes.satelspeed.activities.JOB_EXECUTION_TIME
import com.minutecodes.satelspeed.activities.interfaces.JobInvokeNotifier

class CacheJobScheduler(private val jobNotify: JobInvokeNotifier) :
    CountDownTimer(JOB_EXECUTION_TIME, COUNT_DOWN_INTERVAL) {
    private var isSchedulingStopped = false

    override fun onFinish() {
        jobNotify.onJobInvoked()
        if (!isSchedulingStopped)
            startScheduler()
    }

    override fun onTick(millisUntilFinished: Long) {}

    fun startScheduler() {
        isSchedulingStopped = false
        start()
    }

    fun stopScheduler() {
        isSchedulingStopped = true
        cancel()
    }

}