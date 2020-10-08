package com.android.collegeproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.android.collegeproject.R
import com.android.collegeproject.helper.AndroidPermissions
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val splashScreenTimeOut : Long = 1000
    private lateinit var mAndroidPermissions: AndroidPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            mAndroidPermissions = AndroidPermissions(this)
            mAndroidPermissions.checkUserSettingsAndGetLocation()

            if(mAndroidPermissions.checkPermissionForLocation()
                && mAndroidPermissions.checkPermissionForMicrophone()
                && mAndroidPermissions.checkPermissionForCamera()
                && mAndroidPermissions.checkLocationSetting()) {
                val intent = Intent(this, ImpairedUserActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, PermissionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, splashScreenTimeOut)
    }

}