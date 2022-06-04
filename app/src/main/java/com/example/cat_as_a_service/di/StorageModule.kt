package com.example.cat_as_a_service.di

import com.example.cat_as_a_service.di_codepath.SharedPreferencesStorage
import com.example.cat_as_a_service.di_codepath.Storage
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {

    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage

}