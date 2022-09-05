package com.example.cat_as_a_service.di

import android.content.Context
import androidx.room.Room
import com.example.cat_as_a_service.repository.CatDao
import com.example.cat_as_a_service.repository.CatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideCatDao(catDatabase: CatDatabase): CatDao {
        return catDatabase.catDao()
    }

    @Provides
    @Singleton
    fun provideCatDatabase(@ApplicationContext appContext: Context): CatDatabase {
        return Room.databaseBuilder(
            appContext,
            CatDatabase::class.java,
            "CatDatabase"
        ).build()
    }
}