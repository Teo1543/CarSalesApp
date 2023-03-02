package com.example.carsalesapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.example.carsalesapp.R
import com.example.carsalesapp.databinding.ActivityCarBinding
import com.example.carsalesapp.main.MainApp
import com.example.carsalesapp.models.CarModel
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber.i

class CarSellActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarBinding
    private var car = CarModel()
    private lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        //setSupportActionBar(binding.toolbarAdd)


        app = application as MainApp

        i("Cars Activity started...")



        binding.btnAdd.setOnClickListener {
            car.name = binding.carModel.text.toString()

            car.year = binding.carYear.text.toString().toInt()
            car.engineSize = binding.carEngineSize.text.toString().toDouble()

            if (car.name.isNotEmpty() && car.year > 1890 && car.year < 2023 && car.engineSize > 0.8 && car.engineSize < 6.0) {

                app.cars.add(car.copy())
                i("add Button Pressed: $car")
                for (i in app.cars.indices) {
                    i("Car[$i]:${this.app.cars[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar
                    .make(it,"Please Enter a full car name", Snackbar.LENGTH_LONG)
                    .show()
            }

        }

        carModelFocusListener()
        yearFocusListener()
        engineSizeFocusListener()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_listing, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun carModelFocusListener() {

        binding.carModel.setOnFocusChangeListener { _, focused ->
            if(!focused) {
                binding.carModelContainer.helperText = validCarModel()
            }
        }
    }

    private fun validCarModel(): String? {
        val carModel = binding.carModel.text.toString()
        if(carModel.length > 30){
            return "Your vehicle name is too large."
        }
        if(carModel.length < 5){
            return "Your vehicle name is too short. Make sure that the name is greater than 4 letters."
        }
        return null
    }




    private fun yearFocusListener() {

        binding.carYear.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.carYearContainer.helperText = validYear()
            }
        }
    }

    private fun validYear(): String? {

        val carYear = binding.carYear.text.toString()
        if(carYear.length != 4) {
            return "Please specify year in 4 number formats. Make sure the year is between 1890-2023"
        }
        return null
    }





    private fun engineSizeFocusListener() {
        binding.carEngineSize.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.carEngineSizeContainer.helperText = validEngineSize()
            }
        }
    }

    private fun validEngineSize(): String? {
        val carEngineSize = binding.carEngineSize.text.toString()
        if(carEngineSize.length < 3) {
            return "Please enter a decimal number. E.G 3.0. Make sure the size is between 0.8 - 6.0"
        }
        return null
    }
}