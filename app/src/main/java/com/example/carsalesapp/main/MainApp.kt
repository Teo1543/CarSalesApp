package com.example.carsalesapp.main

import android.app.Application
import com.example.carsalesapp.models.CarModel

import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val cars = ArrayList<CarModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("CarClass started")

    }
}