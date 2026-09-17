package com.charan.batterytracker.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun BatteryTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}