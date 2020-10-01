package com.android.texttospeech

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.android.texttospeech.Helper.AndroidPermissions
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {

    private lateinit var mAndroidPermissions: AndroidPermissions
    private val REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        mAndroidPermissions = AndroidPermissions(this)
        mAndroidPermissions.checkUserSettingsAndGetLocation()

        if(mAndroidPermissions.checkPermissionForLocation()
            && mAndroidPermissions.checkPermissionForMicrophone()
            && mAndroidPermissions.checkPermissionForCamera()
            && mAndroidPermissions.checkLocationSetting()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        if(mAndroidPermissions.checkPermissionForLocation()
            && mAndroidPermissions.checkPermissionForMicrophone()
            && mAndroidPermissions.checkPermissionForCamera()) {
            activity_allow.text = "Click to proceed"
        }

        setCheck()

        activity_permission_deny.setOnClickListener {
            finish()
        }

        activity_allow.setOnClickListener {
            if(mAndroidPermissions.checkPermissionForLocation()
                && mAndroidPermissions.checkPermissionForMicrophone()
                && mAndroidPermissions.checkPermissionForCamera()
                && mAndroidPermissions.checkLocationSetting()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else if(!mAndroidPermissions.checkLocationSetting()) {
                Toast.makeText(this, "Please turn on your location", Toast.LENGTH_SHORT).show()
            } else {
                requestPermissions()
            }
        }
    }

    fun requestPermissions() {
        val list = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        ActivityCompat.requestPermissions(
            this,
            list,
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE){
            var flag = false
            for(i in grantResults.indices){
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    flag = true
                } else {
                    setCheck()
                }
            }
            if(!flag) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun setCheck() {
        if(mAndroidPermissions.checkPermissionForCamera()) {
            camera_check.visibility = View.VISIBLE
        } else {
            camera_check.visibility = View.GONE
        }

        if(mAndroidPermissions.checkPermissionForLocation()) {
            location_check.visibility = View.VISIBLE
        } else {
            location_check.visibility = View.GONE
        }

        if(mAndroidPermissions.checkPermissionForMicrophone()) {
            microphone_check.visibility = View.VISIBLE
        } else {
            microphone_check.visibility = View.GONE
        }
    }
}