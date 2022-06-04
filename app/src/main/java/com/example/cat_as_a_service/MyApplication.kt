package com.example.cat_as_a_service

import android.app.Application
import com.example.cat_as_a_service.di.AppComponent
import com.example.cat_as_a_service.di.DaggerAppComponent
import dagger.android.DaggerApplication

class MyApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}