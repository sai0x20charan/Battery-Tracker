package com.charan.batterytracker.presentation.home

import com.charan.batterytracker.data.model.BatteryInfo

data class HomeState(
    val batteryState : BatteryInfo = BatteryInfo()
)
