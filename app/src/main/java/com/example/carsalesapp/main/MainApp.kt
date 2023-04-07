package com.example.carsalesapp.main

import android.app.Application
import com.example.carsalesapp.models.CarJSONStore
import com.example.carsalesapp.models.CarMemStore
import com.example.carsalesapp.models.CarModel
import com.example.carsalesapp.models.CarStore

import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var cars: CarStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        // cars = CarMemStore()
        cars = CarJSONStore(applicationContext)
        i("Carsales started")
    }
}