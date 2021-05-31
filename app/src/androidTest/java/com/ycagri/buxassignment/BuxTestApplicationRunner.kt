package com.ycagri.buxassignment

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class BuxTestApplicationRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BuxTestApplication::class.java.name, context)
    }
}