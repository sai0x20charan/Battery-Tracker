package com.charan.batterytracker.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.charan.batterytracker.data.repository.BatteryInfoRepo
import com.charan.batterytracker.data.repository.DataStoreRepository
import com.charan.batterytracker.data.repository.WidgetRepository
import com.charan.batterytracker.data.repository.impl.BatteryInfoRepoImp
import com.charan.batterytracker.data.repository.impl.DataStoreRepositoryImpl
import com.charan.batterytracker.utils.NotificationHelper
import com.charan.batterytracker.utils.SettingsUtils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStoreRepository {
        return DataStoreRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideBatteryInfoRepo(
        @ApplicationContext context: Context,
        dataStoreRepository: DataStoreRepository,
        notificationHelper: NotificationHelper
    ): BatteryInfoRepo {
        return BatteryInfoRepoImp(context, dataStoreRepository, notificationHelper)
    }

    @Provides
    @Singleton
    fun provideSettingsUtils(@ApplicationContext context: Context): SettingsUtils {
        return SettingsUtils(context)
    }

    @Provides
    @Singleton
    fun provideWidgetRepository(
        @ApplicationContext context: Context,
        batteryInfoRepo: BatteryInfoRepo,
        dataStoreRepository: DataStoreRepository,
        settingsUtils: SettingsUtils
    ): WidgetRepository {
        return WidgetRepository(
            context = context,
            batteryInfoRepo = batteryInfoRepo,
            dataStoreRepository = dataStoreRepository,
            settingsUtils = settingsUtils
        )
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }
}
