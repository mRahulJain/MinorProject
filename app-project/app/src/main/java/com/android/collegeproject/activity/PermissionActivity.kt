package com.android.collegeproject.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.collegeproject.R
import com.android.collegeproject.helper.AndroidPermissions
import kotlinx.android.synthetic.main.activity_get_permissions.*

class PermissionActivity : AppCompatActivity() {

    private lateinit var mAndroidPermissions: AndroidPermissions
    private val REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_permissions)

        mAndroidPermissions = AndroidPermissions(this)
        mAndroidPermissions.checkUserSettingsAndGetLocation()

        activity_get_permissions_continueButton.setOnClickListener {
            if(mAndroidPermissions.checkPermissionForLocation()
                && mAndroidPermissions.checkPermissionForMicrophone()
                && mAndroidPermissions.checkPermissionForCamera()
                && mAndroidPermissions.checkLocationSetting()) {
                val intent = Intent(this, ImpairedUserActivity::class.java)
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
                    //do nothing
                }
            }
            if(!flag) {
                val intent = Intent(this, ImpairedUserActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}