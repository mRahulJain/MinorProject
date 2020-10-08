package com.android.collegeproject
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ImpairedUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()//setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_get_permissions)//activity_home_page)
    }
}