package com.example.carsalesapp.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class CarMemStore : CarStore {

    private val cars = ArrayList<CarModel>()

    override fun findAll(): List<CarModel> {
        return cars
    }

    override fun create(car: CarModel) {
        car.id = getId()
        cars.add(car)
        logAll()
    }

    override fun update(car: CarModel) {
        val foundCar: CarModel? = cars.find { c -> c.id == car.id }
        if (foundCar != null) {
            foundCar.name = car.name
            foundCar.year = car.year
            foundCar.engineSize = car.engineSize
            foundCar.image = car.image
            logAll()
        }
    }

    private fun logAll() {
        cars.forEach{ i("$it") }
    }
}