package com.minutecodes.satelspeed.activities

const val WORKER_THREAD = "Worker-Thread-01"
const val BAD_GPS = "Waiting for better GPS accuracy"
const val GPS_UNAVAILABLE = "No GPS connection available"

const val KMH_VALUE = 3.65F //Added 0.005 to 3.600 for brute error correction
const val MPH_VALUE = 2.237F
const val ACCEPTABLE_ACCELERATION_DIFFERENCE = 3.5F
const val MAX_ACCELERATION_THRESHOLD = 250F
const val STOPPED_ACCELERATION = 0F
const val GOOD_ACCURACY_RANGE = 12

const val JOB_EXECUTION_TIME = 2000L
const val COUNT_DOWN_INTERVAL = 1000L
const val PERMISSION_REQUEST_CODE = 500
const val CACHE_SIZE = 4
const val MIN_SATELLITE_COUNT = 4
const val MAX_ACCURACY_THRESHOLD = 35
const val LOCATION_UPDATE_INTERVAL = 500L
const val SPEEDOMETER_FRAG_NAME = "speedometer_fragment"
