package com.asana.timer

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.asana.timer.data.AsanaRepository
import java.util.concurrent.TimeUnit

class AsanaTimerApplication : Application() {

    lateinit var repository: AsanaRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = AsanaRepository(this)
        scheduleLocationSync()
    }

    private fun scheduleLocationSync() {
        val networkConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<LocationSyncWorker>(
            1, TimeUnit.HOURS
        )
            .setConstraints(networkConstraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "location_sync_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )

        // Trigger one immediate sync when the app starts (no constraints, damit wir immer Logs sehen)
        val oneTimeRequest = OneTimeWorkRequestBuilder<LocationSyncWorker>()
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "location_sync_immediate",
            ExistingWorkPolicy.REPLACE,
            oneTimeRequest
        )
    }
}

