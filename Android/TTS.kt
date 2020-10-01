package com.android.texttospeech

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.texttospeech.Helper.TextToSpeechHelper
import kotlinx.android.synthetic.main.activity_main_tts.*


class TTS : AppCompatActivity() {
    private lateinit var mTextToSpeechHelper: TextToSpeechHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tts)

        mTextToSpeechHelper = TextToSpeechHelper(this)

        activity_main_button.setOnClickListener {
            val text = activity_main_editText.text.toString()
            mTextToSpeechHelper.speakHindi(text)
        }


        activity_main_button1.setOnClickListener {
            val text = activity_main_editText.text.toString()
            mTextToSpeechHelper.speakEnglish(text)
        }

        activity_main_button2.setOnClickListener {
            activity_main_editText.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTextToSpeechHelper.destroySpeech()
    }
}