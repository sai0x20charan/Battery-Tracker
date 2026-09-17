package com.charan.batterytracker.data.repository

import com.charan.batterytracker.data.model.BatteryInfo

interface BatteryInfoRepo {

    fun getBatteryDetails() : BatteryInfo
}