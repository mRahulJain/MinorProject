package com.android.collegeproject.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

class AndroidPermissions {

    private var mActivity: AppCompatActivity

    constructor(mActivity: AppCompatActivity) {
        this.mActivity = mActivity
    }

    fun checkPermissionForCamera(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            mActivity,
            Manifest.permission.CAMERA
        )
        if(result ==  PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
    fun checkPermissionForLocation(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            mActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if(result ==  PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
    fun checkPermissionForMicrophone(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            mActivity,
            Manifest.permission.RECORD_AUDIO
        )
        if(result ==  PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
    fun checkLocationSetting(): Boolean {
        val locationManager = mActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false
        }
        return true
    }
    fun checkPermissionForContacts(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            mActivity,
            Manifest.permission.READ_CONTACTS
        )
        if(result ==  PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
    fun checkPermissionForCallPhone(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            mActivity,
            Manifest.permission.CALL_PHONE
        )
        if(result ==  PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    //checks whether the user's location is ON or OFF
    //if OFF it turns it ON
    //if ON it does nothing
    fun checkUserSettingsAndGetLocation() {
        val locationManager = mActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val locationRequest = LocationRequest().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val request = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build()
            val client = LocationServices.getSettingsClient(mActivity)

            client.checkLocationSettings(request).apply {
                addOnSuccessListener {
                    Log.d("PUI","success")
                }
                addOnFailureListener{
                    Log.d("PUI", "$it")
                    val e = it as ApiException
                    if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                        val resolvable = it as ResolvableApiException
                        resolvable.startResolutionForResult(mActivity,101)
                    }
                }
            }
        }
    }



}