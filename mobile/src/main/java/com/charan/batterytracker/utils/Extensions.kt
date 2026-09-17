package com.charan.batterytracker.utils

import com.charan.batterytracker.data.model.BatteryInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun BatteryInfo.convertToJsonString() : String{
    return Json.encodeToString(this)

}
fun String.convertToBatteryModel() : BatteryInfo {
    try {
        val batteryInfo: BatteryInfo = Json.decodeFromString(this)
        return batteryInfo
    } catch (e:Exception){
        return BatteryInfo()
    }
}
