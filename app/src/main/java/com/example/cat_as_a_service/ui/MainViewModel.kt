package com.example.cat_as_a_service.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cat_as_a_service.di.ActivityScope
import javax.inject.Inject

@ActivityScope
class MainViewModel @Inject constructor() : ViewModel() {
    fun test(){
        Log.e("@@@", "VM test")
    }
}