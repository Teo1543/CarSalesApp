package com.example.carsalesapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Switch
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.carsalesapp.R
import com.example.carsalesapp.databinding.ActivityCarBinding
import com.example.carsalesapp.helpers.showImagePicker
import com.example.carsalesapp.main.MainApp
import com.example.carsalesapp.models.CarModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import timber.log.Timber.i

class CarSellActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarBinding
    private var car = CarModel()
    private lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
  //  var location = Location(52.245696, -7.139102, 15f)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityCarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        //setSupportActionBar(binding.toolbarAdd)

        val switch: Switch = findViewById(R.id.theme)
        switch.setOnCheckedChangeListener{ buttonView, isChecked ->
            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            }
        }

        binding.carLocation.setOnClickListener {
            i ("Set Location Pressed")
        }

        binding.deleteButton.setOnClickListener {
            i ("Delete Button Pressed")
        }


        app = application as MainApp

        i("Cars Activity started...")

        if (intent.hasExtra("car_edit")) {
            edit = true
            car = intent.extras?.getParcelable("car_edit")!!
            binding.carModel.setText(car.name)
            binding.carYear.setText(car.year.toString())
            binding.carEngineSize.setText(car.engineSize.toString())
            binding.btnAdd.setText(R.string.save_car)
            Picasso.get()
                .load(car.image)
                .into(binding.carImage)
            if (car.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_car_image)
            }
        }


            binding.btnAdd.setOnClickListener {
            car.name = binding.carModel.text.toString()
            car.year = binding.carYear.text.toString().toInt()
            car.engineSize = binding.carEngineSize.text.toString().toDouble()

            if (car.name.isEmpty()) {
                Snackbar.make(it, R.string.enter_car_name, Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                if(edit) {
                    app.cars.update(car.copy())
                }
                else {
                    app.cars.create(car.copy())
                }
            }
            i("add Button Pressed: $car")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.carLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java)
            mapIntentLauncher.launch(launcherIntent)
        }

        carModelFocusListener()
        yearFocusListener()
        engineSizeFocusListener()

        registerImagePickerCallback()
        registerMapCallback()

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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            car.image = result.data!!.data!!
                            Picasso.get()
                                .load(car.image)
                                .into(binding.carImage)
                            binding.chooseImage.setText(R.string.change_car_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { i("Map Loaded") }
    }

}

