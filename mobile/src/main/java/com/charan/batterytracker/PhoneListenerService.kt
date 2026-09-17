package com.charan.batterytracker

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.updateAll
import com.charan.batterytracker.widgets.Material3widget
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.qualifiers.ApplicationContext
import com.charan.batterytracker.data.prefs.SharedPref
import com.charan.batterytracker.data.repository.BatteryInfoRepo
import com.charan.batterytracker.data.repository.impl.BatteryInfoRepoImp
import com.charan.batterytracker.utils.NotificationHelper
import com.charan.batterytracker.utils.convertToJsonString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PhoneListenerService: WearableListenerService() {
    private val scope = CoroutineScope(Dispatchers.IO)
    override fun onCreate() {
        super.onCreate()
        val batteryInfoRepo = BatteryInfoRepoImp(applicationContext, sharedPref = SharedPref(applicationContext), notificationHelper = NotificationHelper(applicationContext))
        val batteryData = batteryInfoRepo.getPhoneBatteryData().convertToJsonString()
        scope.launch {
            getNodes(applicationContext).forEach { nodeId ->
                Wearable.getMessageClient(applicationContext).sendMessage(
                    nodeId,
                    MESSAGE_PATH,
                    batteryData.toByteArray()
                ).apply {
                    addOnSuccessListener {
                        Log.d(TAG, "onSuccess: Data send success")
                    }
                    addOnFailureListener {
                        Log.d(TAG, "onFail: Unable to send the data $it")
                    }
                }
            }

        }

    }
    companion object{
        private const val TAG = "PhoneListenerService"
        private const val MESSAGE_PATH = "/deploy"
    }
}
private fun getNodes(context: Context): Collection<String> {
    return Tasks.await(Wearable.getNodeClient(context).connectedNodes).map { it.id }
}
