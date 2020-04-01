package com.minutecodes.satelspeed.activities.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

private const val ERROR_DELAY = 1000L
private const val WARN_DELAY = 1000L

class GpsEvent(private val lifecycle: Lifecycle) : LifecycleObserver {
    private var isErrorActive = false
    private var isWarnActive = false
    private var warnView: View? = null
    private var errorView: View? = null
    private val handler = Handler(Looper.getMainLooper())
    private val warnRunnable: Runnable = getWarnRunnable()
    private val errorRunnable: Runnable = getErrorRunnable()

    fun setErrorEventView(v: View) {
        errorView = v
    }

    fun setWarnEventView(v: View) {
        warnView = v
    }

    fun showError(isAvailable: Boolean) {
        if (isAvailable) {
            hideError()
        } else {
            if (!isErrorActive) {
                isErrorActive = true
                handler.post(errorRunnable)
            }
        }
    }

    fun hideError() {
        isErrorActive = false
        handler.removeCallbacks(errorRunnable)
        errorView!!.visibility = View.GONE
    }

    fun showWarn() {
        if (!isWarnActive) {
            isWarnActive = true
            handler.post(warnRunnable)
        }
    }

    fun hideWarn() {
        isWarnActive = false
        handler.removeCallbacks(warnRunnable)
        warnView!!.visibility = View.GONE
    }

    fun removeViews() {
        warnView = null
        errorView = null
    }

    fun bindLifeCycle() {
        lifecycle.addObserver(this)
    }

    fun unbindLifeCycle() {
        lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected fun resumeWork() {
        if (isErrorActive)
            resumeError()

        if (isWarnActive)
            resumeWarn()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun stopWork() {
        if (isWarnActive)
            handler.removeCallbacks(warnRunnable)

        if (isErrorActive)
            handler.removeCallbacks(errorRunnable)
    }

    private fun getWarnRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                warnView!!.isVisible = !warnView!!.isVisible
                handler.postDelayed(this, WARN_DELAY)
            }
        }
    }

    private fun getErrorRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                errorView!!.isVisible = !errorView!!.isVisible
                handler.postDelayed(this, ERROR_DELAY)
            }
        }
    }

    private fun resumeError() {
        handler.removeCallbacks(errorRunnable)
        handler.post(errorRunnable)
    }

    private fun resumeWarn() {
        handler.removeCallbacks(warnRunnable)
        handler.post(warnRunnable)
    }

}