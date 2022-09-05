package com.example.cat_as_a_service.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_table")
data class CatImage(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(
        name = "cat",
        typeAffinity = ColumnInfo.BLOB
    ) val byteArrayImage: ByteArray
)
