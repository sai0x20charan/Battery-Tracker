package com.charan.batterytracker.data.repository

import com.charan.batterytracker.data.model.BatteryInfo
import com.charan.batterytracker.data.model.BluetoothDeviceBatteryInfo
import kotlinx.coroutines.flow.Flow

interface BatteryInfoRepo {

    fun registerBatteryReceiver()
    fun unRegisterBatteryReceiver()
    fun getBatteryDetails() : Flow<BatteryInfo?>
    fun getBluetoothBatteryDetails() : Flow<BluetoothDeviceBatteryInfo?>
    fun registerWearOsBatteryReceiver()
    fun registerBluetoothBatteryReceiver()
    fun getHeadPhoneBatteryInfo() : BluetoothDeviceBatteryInfo
    fun getPhoneBatteryData() : BatteryInfo
    fun getBluetoothBattery() : BluetoothDeviceBatteryInfo
    suspend fun sendSignalToWearOs()
}