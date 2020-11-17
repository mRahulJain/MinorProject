package com.android.collegeproject.helper

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.*

open class TextToSpeechHelper {

    private var myContext: Context
    private lateinit var textToSpeechHindi: TextToSpeech
    private lateinit var textToSpeechEnglish: TextToSpeech
    private val pitch = 1f
    private val speed = 0.8f

    constructor(context: Context) {
        this.myContext = context
        initializeLanguages()
    }

    private fun initializeLanguages() {
        //initializing the TextToSpeech variable
        textToSpeechHindi = TextToSpeech(myContext, TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS) {
                val result = textToSpeechHindi.setLanguage(Locale.forLanguageTag("hi"))

                if(result == TextToSpeech.LANG_MISSING_DATA) {
                    Toast.makeText(myContext, "Missing Data", Toast.LENGTH_SHORT).show()
                }
                if(result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(myContext, "Language Not Supported", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(myContext, "Initialization failed", Toast.LENGTH_SHORT).show()
            }
        })

        //initializing the TextToSpeech variable
        textToSpeechEnglish = TextToSpeech(myContext, TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS) {
                val result = textToSpeechEnglish.setLanguage(Locale.ENGLISH)

                if(result == TextToSpeech.LANG_MISSING_DATA) {
                    Toast.makeText(myContext, "Missing Data", Toast.LENGTH_SHORT).show()
                }
                if(result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(myContext, "Language Not Supported", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(myContext, "Initialization failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun speakHindi(text: String) {
        if(text == "") {
            return
        }

        textToSpeechHindi.setPitch(pitch)
        textToSpeechHindi.setSpeechRate(speed)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeechHindi.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            textToSpeechHindi.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    fun speakEnglish(text: String) {
        if(text == "") {
            return
        }
        textToSpeechEnglish.setPitch(pitch)
        textToSpeechEnglish.setSpeechRate(speed)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeechEnglish.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            textToSpeechEnglish.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    fun destroySpeech() {
        if(this::textToSpeechEnglish.isInitialized) {
            textToSpeechEnglish.stop()
        }
        if(this::textToSpeechHindi.isInitialized) {
            textToSpeechHindi.stop()
        }
    }
}