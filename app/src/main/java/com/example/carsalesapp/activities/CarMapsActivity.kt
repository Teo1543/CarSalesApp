package com.example.carsalesapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carsalesapp.databinding.ActivityCarMapsBinding
import com.example.carsalesapp.databinding.ContentCarMapsBinding
import com.example.carsalesapp.main.MainApp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso

class CarMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityCarMapsBinding
    private lateinit var contentBinding: ContentCarMapsBinding
    private lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentBinding = ContentCarMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        app = application as MainApp

        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.cars.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.name).position(loc)
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            map.setOnMarkerClickListener(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        //val car = marker.tag as CarModel
        val tag = marker.tag as Long
        val car = app.cars.findById(tag)
        contentBinding.currentTitle.text = car!!.name
        contentBinding.currentDescription.text = car.year.toString()
        Picasso.get().load(car.image).into(contentBinding.imageView2)
        return false
    }


}