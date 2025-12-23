package com.towhid.memeic

import android.app.Application
import com.towhid.memeic.di.initKoin
import org.koin.android.ext.koin.androidContext

class MemeCreatorApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MemeCreatorApplication)
        }

    }
}