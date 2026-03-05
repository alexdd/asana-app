package com.asana.timer

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class LocationSyncWorker(
    appContext: android.content.Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        runLocationSync(applicationContext)
        return Result.success()
    }
}
