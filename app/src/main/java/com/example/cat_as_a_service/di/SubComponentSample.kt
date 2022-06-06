package com.example.cat_as_a_service.di

import com.example.cat_as_a_service.ui.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface SubComponentSample {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SubComponentSample
    }

    fun inject(activity: MainActivity)
}