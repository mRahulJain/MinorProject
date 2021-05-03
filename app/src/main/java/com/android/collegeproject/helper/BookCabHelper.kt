package com.android.collegeproject.helper

import android.location.Geocoder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.common.math.DoubleMath.roundToLong
import java.util.*
import kotlin.collections.ArrayList

class BookCabHelper{
    private var mContext: AppCompatActivity
    constructor(context: AppCompatActivity) {
        mContext = context
    }

    fun getAddressFromLocation(latitude: Double, longitude: Double) : String {
        var address = ""
        val geoCoder = Geocoder(mContext, Locale.getDefault())
        try {
            val addresses = geoCoder.getFromLocation(
                latitude,
                longitude,
                1
            )
            address = addresses[0].getAddressLine(0)
        } catch (e : Exception) {
            Log.e("PUI", "${e.printStackTrace()}")
        }
        return address
    }

    fun getLocationFromAddress(address : String) : ArrayList<Double> {
        val location = ArrayList<Double>()
        val geoCoder = Geocoder(mContext, Locale.getDefault())
        try {
            val coordinates = geoCoder.getFromLocationName(address, 1)
            location.add(coordinates[0].latitude)
            location.add(coordinates[0].longitude)
        } catch (e : Exception) {
            Log.e("PUI", "${e.printStackTrace()}")
        }
        return location
    }

    fun calculateFare(distanceInMeters : Double, timeInHrs : Double) : Long {
        val baseFare = 0.4
        val timeRate = 0.14
        val distanceRate = 0.97
        val surge = 2.0
        val distanceInKm : Double = distanceInMeters * 0.001
        val timeInMins : Double = timeInHrs * 0.0166667
        val pricePerMin : Double = timeRate * timeInMins
        val pricePerKm  : Double = distanceRate * distanceInKm

        val totalFare : Double = (baseFare + pricePerKm + pricePerMin) * surge
        return Math.round(totalFare)
    }

}