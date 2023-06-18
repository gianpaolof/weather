package com.example.weather.app

import android.app.Application
import com.example.weather.app.di.AppComponent
import com.example.weather.app.di.AppModule
import com.example.weather.app.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(context = this))
            .build()
    }

}