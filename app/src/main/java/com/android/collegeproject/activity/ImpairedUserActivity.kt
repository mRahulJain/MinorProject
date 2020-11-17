package com.android.collegeproject.activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.collegeproject.R
import com.android.collegeproject.helper.*
import kotlinx.android.synthetic.main.activity_home_page.*
import java.util.*

class ImpairedUserActivity : AppCompatActivity() {

    private lateinit var mTextToSpeechHelper: TextToSpeechHelper
    private lateinit var mAndroidPermission: AndroidPermissions
    private lateinit var sr: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        mTextToSpeechHelper = TextToSpeechHelper(this)
        mAndroidPermission = AndroidPermissions(this)

        Handler().postDelayed({
            Constants().speak("Double Tap and Speak", mTextToSpeechHelper)
        }, 500)

        activity_home_page_infoBtn.setOnClickListener {
            val intent = Intent(this, IntroPageActivity::class.java)
            intent.putExtra("from", "homePage")
            startActivity(intent)
        }

        activity_home_page_myTap.setOnClickListener(object : ClickListener() {
            override fun onSingleClick(v: View?) {
                //do nothing
            }

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
                        this@ImpairedUserActivity.packageName
                    )

                    // Add custom listeners.
                    val listener = CustomRecognitionListener()
                    sr = SpeechRecognizer.createSpeechRecognizer(this@ImpairedUserActivity)
                    sr.setRecognitionListener(listener)
                    sr.startListening(intent)
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            Constants().speak("Double Tap and Speak", mTextToSpeechHelper)
        }, 500)
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
            Log.e(s, "error $error")
        }

        override fun onResults(results: Bundle?) {
            val data = results!!
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            when {
                data!![0].toString().toLowerCase(Locale.ROOT) == "weather" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(this@ImpairedUserActivity, WeatherActivity::class.java)
                    startActivity(intent)
                }
                data!![0].toString().toLowerCase(Locale.ROOT) == "news" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(this@ImpairedUserActivity, NewsActivity::class.java)
                    startActivity(intent)
                }
                data!![0].toString().toLowerCase() == "c" -> {
                    mTextToSpeechHelper.destroySpeech()
//                    if(this@MainActivity::sr.isInitialized) {
//                        sr.destroy()
//                    }
//                    startActivity(Intent(this@MainActivity, ScanCodeActivity::class.java))
                }
                data!![0].toString().toLowerCase() == "d" -> {
//                    startActivity(Intent(this@MainActivity, TextRecognition::class.java))
                }
                else -> {
                    Constants().speak("Sorry couldn't hear you!", mTextToSpeechHelper)
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