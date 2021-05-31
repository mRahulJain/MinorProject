package com.android.collegeproject.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class LocationHelper : LocationListener {

    private var myLat = 0.0
    private var myLong = 0.0
    private var locationManager: LocationManager? = null
    private var mContext: Context
    private var mActivity: AppCompatActivity
    private lateinit var mSharedPreferences: SharedPreferences
    constructor(context: Context,activity: AppCompatActivity) {
        this.mActivity = activity
        this.mContext = context
        this.mSharedPreferences = this.mActivity.getSharedPreferences(
            Constants().LOCATION,
            MODE_PRIVATE
        )
        startLocationUpdates()
    }

    fun getLocation(): ArrayList<Double> {
        val list = arrayListOf<Double>()
        list.add(myLat)
        list.add(myLong)
        return list
    }

    override fun onLocationChanged(p0: Location?) {
        p0?.let { it ->
            myLat = it!!.latitude
            myLong = it!!.longitude
            mSharedPreferences.edit().putString(
              Constants().LATITUDE,
              ""+myLat
            ).apply()
            mSharedPreferences.edit().putString(
                Constants().LONGITUDE,
                ""+myLong
            ).apply()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val enabledProvider =
            when {
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                else -> "nill"
            }
        locationManager?.requestLocationUpdates(
            enabledProvider,
            1000,
            0f,
            this
        )
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

}