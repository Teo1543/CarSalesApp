package com.example.carsalesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.carsalesapp.databinding.CardCarBinding
import com.example.carsalesapp.models.CarModel

class CarAdapter constructor(private var cars: List<CarModel>) :
    RecyclerView.Adapter<CarAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardCarBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val car = cars[holder.adapterPosition]
        holder.bind(car)
    }

    override fun getItemCount(): Int = cars.size

    class MainHolder(private val binding : CardCarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: CarModel) {
            binding.carModel.text = car.name
            binding.carYear.text = car.year.toString()
            binding.carEngineSize.text = car.engineSize.toString()
        }
    }
}