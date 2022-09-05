package com.example.cat_as_a_service.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCat(catImage: CatImage)
}