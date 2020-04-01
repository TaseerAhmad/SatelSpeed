package com.minutecodes.satelspeed.activities.util

class AverageSpeed {
    private val speedIntervals = ArrayList<Float>()

    fun recordSpeed(value: Float) {
        speedIntervals.add(value)
    }

    fun intervalPassed(): Int {
        return speedIntervals.size * 2
    }

    fun getAverage(): Float {
        var totalSpeed = 0F
        val intervalsCount = speedIntervals.size
        speedIntervals.forEach { totalSpeed += it }
        speedIntervals.clear()
        return totalSpeed / intervalsCount
    }
}