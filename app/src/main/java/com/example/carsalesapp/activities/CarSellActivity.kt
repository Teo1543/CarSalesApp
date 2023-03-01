package com.example.carsalesapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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


        app = application as MainApp

        carModelFocusListener()
        yearFocusListener()
        engineSizeFocusListener()

        i("Cars Activity started...")



        binding.btnAdd.setOnClickListener {
            car.name = binding.carModel.text.toString()

            car.year = binding.carYear.text.toString().toInt()
            car.engineSize = binding.carEngineSize.text.toString().toDouble()

            if (car.name.isNotEmpty()) {

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
            return "Your vehicle name is too short."
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
        if(carYear.length < 4 && carYear.isEmpty()) {
            return "Please specify year in 4 number formats"
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
        if(carEngineSize.length < 3 && carEngineSize.isEmpty()) {
            return "Please enter a decimal number. E.G 3.0"
        }
        return null
    }







    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_car, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Potential bug?
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}