package com.charan.batterytracker.data.repository.impl

import android.content.Context
import android.os.Build
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.charan.batterytracker.data.repository.DataStoreRepository
import com.charan.batterytracker.utils.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DataStoreRepository {

    companion object {
        private val Context.dataStore by preferencesDataStore("app_preferences")

        private val KEY_DEVICE_NAME = stringPreferencesKey(AppConstants.PHONE_NAME)
        private val KEY_MIN_WEAR_OS_BATTERY = stringPreferencesKey(AppConstants.MIN_WEAR_OS_POWER)
        private val KEY_MIN_HEADPHONES_BATTERY = stringPreferencesKey(AppConstants.MIN_HEADPHONE_POWER)
        private val KEY_IS_NOTIFICATION_ALLOWED = booleanPreferencesKey(AppConstants.IS_NOTIFICATION_ALLOWED)
        private val KEY_IS_DARK_MODE = booleanPreferencesKey(AppConstants.IS_DARK_MODE)
        private val KEY_IS_NOTIFICATION_SENT = booleanPreferencesKey(AppConstants.IS_NOTIFICATION_SENT)
        private val KEY_IS_NOTIFICATION_SENT_FOR_HEADPHONES = booleanPreferencesKey(AppConstants.IS_NOTIFICATION_SENT_FOR_HEADPHONES)
    }

    private val preferencesFlow: Flow<Preferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    override suspend fun setDeviceName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_DEVICE_NAME] = name
        }
    }

    override val getDeviceName: Flow<String>
        get() = preferencesFlow.map { preferences ->
            preferences[KEY_DEVICE_NAME] ?: Build.MODEL
        }

    override suspend fun setMinWearOsBattery(battery: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MIN_WEAR_OS_BATTERY] = battery
        }
    }

    override val getMinWearOsBattery: Flow<String>
        get() = preferencesFlow.map { preferences ->
            preferences[KEY_MIN_WEAR_OS_BATTERY] ?: "20"
        }

    override suspend fun setMinHeadphonesBattery(battery: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MIN_HEADPHONES_BATTERY] = battery
        }
    }

    override val getMinHeadphonesBattery: Flow<String>
        get() = preferencesFlow.map { preferences ->
            preferences[KEY_MIN_HEADPHONES_BATTERY] ?: "20"
        }

    override suspend fun setIsNotificationAllowed(isAllowed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_NOTIFICATION_ALLOWED] = isAllowed
        }
    }

    override val getIsNotificationAllowed: Flow<Boolean>
        get() = preferencesFlow.map { preferences ->
            preferences[KEY_IS_NOTIFICATION_ALLOWED] ?: false
        }

    override suspend fun setIsDarkModeEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_DARK_MODE] = isEnabled
        }
    }

    override val getIsDarkModeEnabled: Flow<Boolean>
        get() = preferencesFlow.map { preferences ->
            preferences[KEY_IS_DARK_MODE] ?: false
        }

    override suspend fun setIsNotificationSent(isSent: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_NOTIFICATION_SENT] = isSent
        }
    }

    override val getIsNotificationSent: Flow<Boolean>
        get() = preferencesFlow.map { preferences ->
            preferences[KEY_IS_NOTIFICATION_SENT] ?: false
        }

    override suspend fun setIsNotificationSentForHeadphones(isSent: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_IS_NOTIFICATION_SENT_FOR_HEADPHONES] = isSent
        }
    }

    override val getIsNotificationSentForHeadphones: Flow<Boolean>
        get() = preferencesFlow.map { preferences ->
            preferences[KEY_IS_NOTIFICATION_SENT_FOR_HEADPHONES] ?: false
        }
}
