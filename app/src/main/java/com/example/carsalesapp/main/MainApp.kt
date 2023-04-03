package com.example.carsalesapp.main

import android.app.Application
import com.example.carsalesapp.models.CarMemStore
import com.example.carsalesapp.models.CarModel

import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val cars = CarMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("CarClass started")

    }
}