package com.charan.batterytracker.presentation.settings

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.batterytracker.data.repository.DataStoreRepository
import com.charan.batterytracker.data.repository.WidgetRepository
import com.charan.batterytracker.utils.AppConstants
import com.charan.batterytracker.utils.SettingsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStoreRepository,
    private val settingsUtils: SettingsUtils,
    private val widgetRepository: WidgetRepository
): ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()
    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        setInitialValues()
        observeSettingsDataStore()
    }

    fun onEvent(settingsEvent: SettingsEvent){
        when(settingsEvent){
            is SettingsEvent.onChangeDarkMode -> {
                changeDarkMode()
            }
            is SettingsEvent.onChangeHeadPhonesBatteryLevel -> {
                changeMinHeadphoneBatteryLevel(settingsEvent.level)
            }
            is SettingsEvent.onChangePhoneName -> {
                updatePhoneName(settingsEvent.name)
            }
            is SettingsEvent.onChangeWearOsBatteryLevel -> {
                changeMinWearOsBatteryLevel(settingsEvent.level)
            }
            SettingsEvent.onCheckForUpdate -> {
            }
            SettingsEvent.onGithubOpen -> {
                openGithub()
            }
            SettingsEvent.onChangePhonenNameSubmit -> {
                phoneNameSubmit()
            }
            is SettingsEvent.onBluetoothPermissionChange -> {
                changeBluetoothPermission(settingsEvent.showRational)
            }
            is SettingsEvent.onNotificationPermissionChange -> {
                changeNotificationPermission(settingsEvent.showRational)
            }
            SettingsEvent.onBluetoothPermissionGrant -> {
                changeBluetoothState()
            }
            SettingsEvent.onNotificationPermissionGrant -> {
                if(!settingsUtils.isNotificationPermissionGranted()) {
                    changeNotificationState()
                }
            }
        }
    }

    private fun setInitialValues() {
        _state.update {
            it.copy(
                isNearByPermissionGranted = settingsUtils.isBluetoothPermissionGranted(),
                isNotificationPermissionGranted = settingsUtils.isNotificationPermissionGranted(),
                phoneName = Build.MODEL
            )
        }
    }

    private fun observeSettingsDataStore() = viewModelScope.launch {
        combine(
            dataStore.getIsDarkModeEnabled,
            dataStore.getIsNotificationAllowed,
            dataStore.getDeviceName,
            dataStore.getMinWearOsBattery,
            dataStore.getMinHeadphonesBattery
        ) { isDark, isNotif, name, wearMin, headMin ->
            _state.update {
                it.copy(
                    isDarkModeEnabled = isDark,
                    isNotificationPermissionGranted = isNotif && settingsUtils.isNotificationPermissionGranted(),
                    phoneName = name,
                    wearOsMinimumBattery = wearMin.toFloatOrNull() ?: 20f,
                    headPhonesMinimumBattery = headMin.toFloatOrNull() ?: 20f,
                    isNearByPermissionGranted = settingsUtils.isBluetoothPermissionGranted()
                )
            }
        }.collect {}
    }

    private fun updatePhoneName(name: String){
        _state.update {
            it.copy(
                phoneName = name
            )
        }
    }

    private fun phoneNameSubmit() = viewModelScope.launch(Dispatchers.IO){
        dataStore.setDeviceName(_state.value.phoneName)
        widgetRepository.updateWidget()
    }

    private fun changeMinHeadphoneBatteryLevel(value: Float) = viewModelScope.launch {
        val intVal = value.roundToInt()
        _state.update {
            it.copy(
                headPhonesMinimumBattery = intVal.toFloat()
            )
        }
        dataStore.setMinHeadphonesBattery(intVal.toString())
    }

    private fun changeMinWearOsBatteryLevel(value: Float) = viewModelScope.launch {
        val intVal = value.roundToInt()
        _state.update {
            it.copy(
                wearOsMinimumBattery = intVal.toFloat()
            )
        }
        dataStore.setMinWearOsBattery(intVal.toString())
    }

    private fun changeDarkMode() = viewModelScope.launch {
        val newDarkMode = !_state.value.isDarkModeEnabled
        _state.update {
            it.copy(
                isDarkModeEnabled = newDarkMode
            )
        }
        dataStore.setIsDarkModeEnabled(newDarkMode)
        if (newDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun changeNotificationPermission(showRational: Boolean) = viewModelScope.launch{
        changeNotificationState()
        if(!settingsUtils.isNotificationPermissionGranted()){
            if(!showRational){
                _effect.emit(SettingsEffect.RequestNotificationPermission)
            } else {
                openSettings()
            }
        }
    }

    private fun changeBluetoothPermission(showRational: Boolean) = viewModelScope.launch{
        if(_state.value.isNearByPermissionGranted){
            changeBluetoothState()
        } else {
            if(!showRational){
                _effect.emit(SettingsEffect.RequestNertByPermission)
            } else {
                openSettings()
            }
        }
    }

    private fun openSettings(){
        viewModelScope.launch {
            _effect.emit(SettingsEffect.OpenSettings)
        }
    }

    private fun changeNotificationState() = viewModelScope.launch {
        val newPermissionState = if (!_state.value.isNotificationPermissionGranted) {
            settingsUtils.isNotificationPermissionGranted()
        } else {
            false
        }

        _state.update { currentState ->
            currentState.copy(isNotificationPermissionGranted = newPermissionState)
        }

        dataStore.setIsNotificationAllowed(newPermissionState)
    }

    private fun changeBluetoothState(){
        _state.update {
            it.copy(
                isNearByPermissionGranted = settingsUtils.isBluetoothPermissionGranted()
            )
        }
    }

    private fun openGithub() = viewModelScope.launch{
        _effect.emit(SettingsEffect.OpenGithub(AppConstants.GITHUB_LINK))
    }
}
