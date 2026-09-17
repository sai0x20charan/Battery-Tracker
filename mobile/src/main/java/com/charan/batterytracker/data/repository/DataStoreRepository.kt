package com.charan.batterytracker.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun setDeviceName(name: String)

    val getDeviceName: Flow<String>

    suspend fun setMinWearOsBattery(battery: String)

    val getMinWearOsBattery: Flow<String>

    suspend fun setMinHeadphonesBattery(battery: String)

    val getMinHeadphonesBattery: Flow<String>

    suspend fun setIsNotificationAllowed(isAllowed: Boolean)

    val getIsNotificationAllowed: Flow<Boolean>

    suspend fun setIsDarkModeEnabled(isEnabled: Boolean)

    val getIsDarkModeEnabled: Flow<Boolean>

    suspend fun setIsNotificationSent(isSent: Boolean)

    val getIsNotificationSent: Flow<Boolean>

    suspend fun setIsNotificationSentForHeadphones(isSent: Boolean)

    val getIsNotificationSentForHeadphones: Flow<Boolean>
}
