package com.android.collegeproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.collegeproject.R
import com.android.collegeproject.helper.Constants
import com.android.collegeproject.helper.SwipeListener
import com.android.collegeproject.helper.TextToSpeechHelper
import kotlinx.android.synthetic.main.activity_text_recognition_text.*

class TextRecognitionTextActivity : AppCompatActivity() {

    private lateinit var mTextToSpeechHelper: TextToSpeechHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition_text)

        mTextToSpeechHelper = TextToSpeechHelper(this)

        val string = intent.getStringExtra("text")
        activity_text_recognition_text_textView.text = string

        //scanned text
        Constants().speak("", mTextToSpeechHelper)

        activity_text_recognition_text_mainScreen.setOnTouchListener(object : SwipeListener(this) {
            override fun onSwipeRight() {
                val intent = Intent(this@TextRecognitionTextActivity, TextRecognitionActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onSwipeLeft() {
            }

            override fun onSwipeTop() {
            }

            override fun onSwipeBottom() {
            }

        })
    }
}