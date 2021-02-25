package com.android.collegeproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.android.collegeproject.R
import com.android.collegeproject.helper.AndroidPermissions
import com.android.collegeproject.helper.Constants
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val splashScreenTimeOut : Long = 1000
    private lateinit var mAndroidPermissions: AndroidPermissions
    private lateinit var mSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mSharedPreferences = this.getSharedPreferences(
            Constants().USER_INFO,
            Context.MODE_PRIVATE
        )

        Handler().postDelayed({
            mAndroidPermissions = AndroidPermissions(this)
            mAndroidPermissions.checkUserSettingsAndGetLocation()

            if(mAndroidPermissions.checkPermissionForLocation()
                && mAndroidPermissions.checkPermissionForMicrophone()
                && mAndroidPermissions.checkPermissionForCamera()
                && mAndroidPermissions.checkLocationSetting()
                && mAndroidPermissions.checkPermissionForContacts()
                && mAndroidPermissions.checkPermissionForCallPhone()
                && mAndroidPermissions.checkPermissionForSendSMS()) {
                val intent = Intent(this, ImpairedUserActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if(mSharedPreferences.getString(Constants().USER_ISFIRSTLOGIN, "") != "NotFirst") {
                    val intent = Intent(this, IntroPageActivity::class.java)
                    intent.putExtra("from", "splash")
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, PermissionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, splashScreenTimeOut)
    }

}