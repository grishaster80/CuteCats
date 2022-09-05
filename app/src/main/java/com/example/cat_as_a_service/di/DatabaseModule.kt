package com.example.cat_as_a_service.di

import com.example.cat_as_a_service.repository.CatDao
import com.example.cat_as_a_service.repository.CatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideCatDao(catDatabase: CatDatabase): CatDao {
        return catDatabase.catDao()
    }
}