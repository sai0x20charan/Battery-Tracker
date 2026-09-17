package com.charan.batterytracker.data.model

data class WearOsBatteryInfo(
    val isWearOsConnected : Boolean = false,
    val wearosBatteryLevel : String = "",
    val wearOsDeviceName : String = "",
    val wearOsBatteryPercentage : Float = 0f,
    val isWearOsCharging : Boolean = false
)
