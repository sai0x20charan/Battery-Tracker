package com.charan.batterytracker.data.repository

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.charan.batterytracker.data.model.BatteryInfo
import com.charan.batterytracker.data.model.BluetoothDeviceBatteryInfo
import com.charan.batterytracker.data.prefs.SharedPref
import com.charan.batterytracker.utils.NotificationHelper
import com.charan.batterytracker.utils.SettingsUtils
import com.charan.batterytracker.widgets.Material3widget
import com.charan.batterytracker.widgets.WidgetState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetRepository @Inject constructor(
    val batteryInfoRepo: BatteryInfoRepo,
    val sharedPref: SharedPref,
    val settingsUtils : SettingsUtils,
    @ApplicationContext val context : Context,
) {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetRepositoryEntryPoint {
        fun widgetModelRepository(): WidgetRepository
    }
    init {
        startObserving()
    }

    companion object {
        fun get(applicationContext: Context): WidgetRepository {
            val widgetRepositoryEntryPoint: WidgetRepositoryEntryPoint = EntryPoints.get(
                applicationContext,
                WidgetRepositoryEntryPoint::class.java
            )
            return widgetRepositoryEntryPoint.widgetModelRepository()
        }
    }

    fun startObserving() {
        if(settingsUtils.isBluetoothPermissionGranted()){
            batteryInfoRepo.registerBluetoothBatteryReceiver()
            batteryInfoRepo.registerWearOsBatteryReceiver()
        }
        batteryInfoRepo.registerBatteryReceiver()



    }

    fun batteryData() : BatteryInfo =
         batteryInfoRepo.getPhoneBatteryData()


    fun bluetoothBatteryData() : Flow<BluetoothDeviceBatteryInfo?> =
        batteryInfoRepo.getBluetoothBatteryDetails()

    suspend fun allDevicesBatteryData(): WidgetState {
        if(settingsUtils.isBluetoothPermissionGranted()) {
            batteryInfoRepo.sendSignalToWearOs()
        }
        return WidgetState(
            deviceBattery = batteryInfoRepo.getPhoneBatteryData(),
            bluetoothBattery = batteryInfoRepo.getBluetoothBatteryDetails().first() ?: BluetoothDeviceBatteryInfo()
        )
    }




    fun cleanUp() {
        batteryInfoRepo.unRegisterBatteryReceiver()
    }

    suspend fun updateWidget() {
        Material3widget.updateAll(context)


    }



}