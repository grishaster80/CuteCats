package com.example.cat_as_a_service.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(CatImage::class), version = 1, exportSchema = false)
public abstract class CatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao

}