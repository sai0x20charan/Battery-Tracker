package com.charan.batterytracker.widgets

import com.charan.batterytracker.data.model.BatteryInfo
import com.charan.batterytracker.data.model.BluetoothDeviceBatteryInfo

data class WidgetState(
    val deviceBattery : BatteryInfo = BatteryInfo(),
    val bluetoothBattery : BluetoothDeviceBatteryInfo = BluetoothDeviceBatteryInfo()
)