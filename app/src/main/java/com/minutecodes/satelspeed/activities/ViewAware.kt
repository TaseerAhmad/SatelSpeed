package com.minutecodes.satelspeed.activities

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Lifecycle aware Handler() wrapper for safely delaying any work related to view.
 */
@Suppress("ProtectedInFinal")
class ViewAware(private val lifeCycle: Lifecycle) : LifecycleObserver {
    private lateinit var runnable: Runnable
    private val mainHandler = Handler(Looper.getMainLooper())
    private val waitingWorkList = ArrayList<() -> Unit>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected fun onResume() {
        bindLifeCycle()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected fun onPause() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected fun onStop() {

        unBindLifeCycle()
    }

    private fun bindLifeCycle() {
        lifeCycle.addObserver(this)
    }

    protected fun unBindLifeCycle() {
        lifeCycle.removeObserver(this)
    }

    /**
     * Delay work on a view, do not use it for blocking I/O or CPU intensive tasks
     */
    fun delayed(block: () -> Unit, delayTime: Long): ViewAware {

        runnable = Runnable {
            mainHandler.postDelayed(runnable, 1000L)
        }

        waitingWorkList.add(block)
        mainHandler.postDelayed({
            block()
            waitingWorkList.remove(block)
        }, delayTime)
        return this
    }

    fun repeat() {
        waitingWorkList.first()
    }


}
