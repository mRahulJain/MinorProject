package com.android.collegeproject.helper

import android.location.Geocoder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*
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
            address = addresses[0].locality+", "+addresses[0].featureName//
        } catch (e : Exception) {
            Log.e("PUI", "${e.printStackTrace()}")
        }
        Log.d("mCabHelper",address)
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

    fun calculateFare(distanceInKiloMeters : Double, timeInHrs : Double) : Double {
        val baseFare = 1.12
        val timeRate = 0.95
        val distanceRate = 0.97
        val surge = 2.0
        val timeInMins : Double = timeInHrs
        val pricePerMin : Double = timeRate * timeInMins
        val pricePerKm  : Double = distanceRate * distanceInKiloMeters

        val totalFare : Double = (baseFare + pricePerKm + pricePerMin) * surge
        return round(totalFare)
    }
    fun calculateDistanceInKm(lat1: Double, lon1: Double,lat2: Double, lon2: Double) : Double{
        val R = 6371
        val dlon = (lon2 - lon1)*PI/180
        val dlat = (lat2 - lat1)*PI/180
        val a = sin(dlat/2)*sin(dlat/2) + cos(lat1*PI/180)*cos(lat2*PI/180)*sin(dlon/2)*sin(dlon/2)
        val c = 2 * asin(sqrt(a))

        return R * c
    }
    fun clacTimeInHrs(distInKm: Double?): Double{
        Log.d("mCabHelper dist",distInKm.toString())
        val rand:kotlin.random.Random = kotlin.random.Random(System.nanoTime())
        val speed:Double = (35..45).random(rand).toDouble()

        Log.d("mCabHelper time", ""+speed+"Kmph, "+(round(distInKm!!.div(speed) * 100.0) / 100.0))
        return round(((distInKm)/speed) * 6000.0) / 100.0
    }
}

