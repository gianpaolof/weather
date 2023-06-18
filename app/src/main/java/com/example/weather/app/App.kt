package com.example.weather.app

import android.app.Application
import com.example.weather.app.di.*

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .dataModule(DataModule())
            .domainModule(DomainModule())
            .build()
    }

}