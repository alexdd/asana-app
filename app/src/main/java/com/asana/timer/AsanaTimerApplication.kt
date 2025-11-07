package com.asana.timer

import android.app.Application
import com.asana.timer.data.AsanaRepository

class AsanaTimerApplication : Application() {

    lateinit var repository: AsanaRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = AsanaRepository(this)
    }
}

