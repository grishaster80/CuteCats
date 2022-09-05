package com.example.cat_as_a_service.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cat_as_a_service.repository.CatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val catRepository: CatRepository) : ViewModel() {
    fun test(){
        Log.e("@@@", "VM test")
    }


    fun saveCatImage(catBitmap: Bitmap?) {
        viewModelScope.launch {
            val bos: ByteArrayOutputStream = object : ByteArrayOutputStream(){}
            catBitmap?.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val catByteArray = bos.toByteArray()
            catRepository.addCatToDatabase(catByteArray)
        }
    }
}