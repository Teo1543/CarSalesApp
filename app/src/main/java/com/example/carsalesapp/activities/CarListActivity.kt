package com.example.carsalesapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carsalesapp.R
import com.example.carsalesapp.adapters.CarAdapter
import com.example.carsalesapp.adapters.CarListener
import com.example.carsalesapp.databinding.ActivityCarListBinding
import com.example.carsalesapp.main.MainApp
import com.example.carsalesapp.models.CarModel
import com.example.carsalesapp.models.CarStore
import kotlin.random.Random

class CarListActivity : AppCompatActivity(), CarListener {

    private lateinit var app: MainApp
    private lateinit var binding: ActivityCarListBinding
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarListBinding.inflate(layoutInflater)
        setContentView(binding.root)



        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = CarAdapter(app.cars)
//        binding.recyclerView.adapter = CarAdapter(app.cars.findAll())
        binding.recyclerView.adapter = CarAdapter(app.cars.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_car, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, CarSellActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.cars.findAll().size)
            }
        }

    override fun onCarClick(car: CarModel, pos : Int) {
        val launcherIntent = Intent(this, CarSellActivity::class.java)
        launcherIntent.putExtra("car_edit", car)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.cars.findAll().size)
            }
            else // Deleting
                if (it.resultCode == 99)     (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }


}