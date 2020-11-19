package com.android.collegeproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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

        val waitingText = "Generating your scanned document. \n please wait.\n\n\n"

        //Constants().speak(waitingText, mTextToSpeechHelper)

        val string = intent.getStringExtra("text")
        activity_text_recognition_text_textView.text = string

        val scannedText = "The scanned docment says \n\n\n\n\n"+string
        Handler().postDelayed({
            Constants().speak(waitingText+scannedText, mTextToSpeechHelper)
        }, 500)

        activity_text_recognition_text_mainScreen.setOnTouchListener(object : SwipeListener(this) {
            override fun onSwipeRight() {
                mTextToSpeechHelper.destroySpeech()
                val intent = Intent(this@TextRecognitionTextActivity, ImpairedUserActivity::class.java)
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