package com.example.cat_as_a_service.di

import android.content.Context
import com.example.cat_as_a_service.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [StorageModule::class, AppSubcomponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun subComponentSample(): SubComponentSample.Factory
}