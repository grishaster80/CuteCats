package com.example.cat_as_a_service.repository

import javax.inject.Inject

class CatRepository @Inject constructor(private val catDao: CatDao) {
    suspend fun addCatToDatabase(catByteArray: ByteArray) {
        catDao.insertCat(CatImage(byteArrayImage = catByteArray))
    }

    fun removeCatFromDatabase() {

    }
}