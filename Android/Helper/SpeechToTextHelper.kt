package com.android.texttospeech.Helper

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.*

open class SpeechToTextHelper {

    private var myContext: Context
    private lateinit var sr: SpeechRecognizer
    private lateinit var mTextView: TextView

    constructor(context: Context) {
        this.myContext = context
    }

    fun makeText(textView: TextView) {
        mTextView = textView
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        intent.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE,
            myContext.packageName
        )

        // Add custom listeners.
        val listener = CustomRecognitionListener()
        sr = SpeechRecognizer.createSpeechRecognizer(myContext)
        sr.setRecognitionListener(listener)
        sr.startListening(intent)
    }

    inner class CustomRecognitionListener: RecognitionListener {
        private val s = "RecognitionListener"

        override fun onReadyForSpeech(params: Bundle?) {
            Log.d(s, "onReadyForSpeech")
        }

        override fun onBeginningOfSpeech() {
            Log.d(s, "onBeginningOfSpeech")
        }

        override fun onRmsChanged(rmsdB: Float) {
            Log.d(s, "onRmsChanged")
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.d(s, "onBufferReceived")
        }

        override fun onEndOfSpeech() {
            Log.d(s, "onEndOfSpeech")
        }

        override fun onError(error: Int) {
            Toast.makeText(myContext, "Please give permission", Toast.LENGTH_SHORT).show()
            Log.e(s, "error $error")
        }

        override fun onResults(results: Bundle?) {
            //get your text here
            val data = results!!
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            mTextView.text = data!![0]
            Log.d(s, "${data!![0]}")
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d(s, "onPartialResults")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.d(s, "onEvent $eventType")
        }
    }

    fun destroyListening() {
        if(this::sr.isInitialized) {
            sr.destroy()
        }
    }
}