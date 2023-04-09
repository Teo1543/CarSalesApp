package com.example.carsalesapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.example.carsalesapp.models.Location
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import timber.log.Timber.i

class CarSellActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarBinding
    private var car = CarModel()
    private lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private var edit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        edit = false

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
            i("Set Location Pressed")
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
            showImagePicker(imageIntentLauncher,this)
        }

        binding.carLocation.setOnClickListener {
            val location = Location(-36.888049, 174.745918, 15f)
            if (car.zoom != 0f) {
                location.lat =  car.lat
                location.lng = car.lng
                location.zoom = car.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }


        carModelFocusListener()
        yearFocusListener()
        engineSizeFocusListener()

        registerImagePickerCallback()
        registerMapCallback()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_car, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.cars.delete(car)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
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
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            car.image = image

                            Picasso.get()
                                .load(car.image)
                                .into(binding.carImage)
                            binding.chooseImage.setText(R.string.change_car_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            car.lat = location.lat
                            car.lng = location.lng
                            car.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

}

