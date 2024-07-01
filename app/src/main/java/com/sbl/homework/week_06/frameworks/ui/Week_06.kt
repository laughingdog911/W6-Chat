package com.sbl.homework.week_06.frameworks.ui

import android.app.Application
import com.google.android.material.color.DynamicColors

class Week_06: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}