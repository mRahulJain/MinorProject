package com.android.texttospeech

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.texttospeech.Helper.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAndroidPermission: AndroidPermissions
    private lateinit var sr: SpeechRecognizer
    private lateinit var mTextToSpeechHelper: TextToSpeechHelper
    private lateinit var mLocationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAndroidPermission = AndroidPermissions(this)
        mLocationHelper = LocationHelper(this, this)
        mTextToSpeechHelper = TextToSpeechHelper(this)

//        if(!Python.isStarted()) {
//            Python.start(AndroidPlatform(this))
//        }
//        val py = Python.getInstance()
//        val pyf = py.getModule("okstart")
//        val obj = pyf.callAttr("start")
//        Log.d("myPython", "$obj")

        Handler().postDelayed({
            speak("Double Tap and Speak")
        }, 500)

        main.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                if(!mAndroidPermission.checkPermissionForMicrophone()) {
                    //don't perform anything if permission is denied
                } else {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
                    intent.putExtra(
                        RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        packageName
                    )
                    // Add custom listeners.
                    val listener = CustomRecognitionListener()
                    sr = SpeechRecognizer.createSpeechRecognizer(this@MainActivity)
                    sr.setRecognitionListener(listener)
                    sr.startListening(intent)
                }
            }
            override fun onSingleClick(v: View?) {
            }
        })

        clickMe.setOnClickListener {
            val intent = Intent(this, ImageActivity::class.java)
            startActivity(intent)
        }
    }


    private fun speak(text: String) {
        mTextToSpeechHelper.speakEnglish(text)
    }

    override fun onRestart() {
        super.onRestart()
        speak("Double Tap and Speak")
    }

    override fun onResume() {
        super.onResume()
        speak("Double Tap and Speak")
    }

    override fun onDestroy() {
        super.onDestroy()
        mTextToSpeechHelper.destroySpeech()
        if(this::sr.isInitialized) {
            sr.destroy()
        }
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
            Toast.makeText(this@MainActivity, "Please give permission", Toast.LENGTH_SHORT).show()
            Log.e(s, "error $error")
        }

        override fun onResults(results: Bundle?) {
            val data = results!!
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            Toast.makeText(this@MainActivity, data!![0], Toast.LENGTH_SHORT).show()
            when {
                data!![0].toString().toLowerCase(Locale.ROOT) == "a" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@MainActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(this@MainActivity, TTS::class.java)
                    startActivity(intent)
                }
                data!![0].toString().toLowerCase() == "b" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@MainActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(this@MainActivity, STT::class.java)
                    startActivity(intent)
                }
                data!![0].toString().toLowerCase() == "c" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@MainActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    startActivity(Intent(this@MainActivity, ScanCodeActivity::class.java))
                }
                data!![0].toString().toLowerCase() == "d" -> {
                    startActivity(Intent(this@MainActivity, TextRecognition::class.java))
                }
                else -> {
                    speak("Sorry couldn't hear you!")
                }
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d(s, "onPartialResults")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.d(s, "onEvent $eventType")
        }
    }
}