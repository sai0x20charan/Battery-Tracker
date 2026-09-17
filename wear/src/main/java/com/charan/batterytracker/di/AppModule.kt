package com.charan.batterytracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.charan.batterytracker.ListenerService
import com.charan.batterytracker.data.repository.BatteryInfoRepo
import com.charan.batterytracker.data.repository.impl.BatteryInfoRepoImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideBatteryInfoRepo(@ApplicationContext context: Context) : BatteryInfoRepo {
        return BatteryInfoRepoImp(context)
    }

}