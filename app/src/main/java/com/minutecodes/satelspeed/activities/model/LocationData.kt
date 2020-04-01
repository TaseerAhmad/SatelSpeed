package com.minutecodes.satelspeed.activities.model

data class LocationData(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var altitude: Int = 0,
    var accuracy: Int = 0,
    var speed: Float = 0F,
    var distanceCovered: Float = 0F,
    var averageSpeed: Float = 0F,
    var topSpeed: Float = 0F
)