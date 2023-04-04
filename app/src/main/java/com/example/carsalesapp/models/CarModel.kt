package com.example.carsalesapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarModel(
    var id: Long = 0,
    var name: String = "",
    var year: Int = 0,
    var engineSize: Double = 0.0,
    var image: Uri = Uri.EMPTY
) : Parcelable