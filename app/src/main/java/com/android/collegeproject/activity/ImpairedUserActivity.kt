package com.android.collegeproject.activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.android.collegeproject.R
import com.android.collegeproject.helper.*
import kotlinx.android.synthetic.main.activity_home_page.*
import java.util.*
import kotlin.collections.ArrayList

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

        activity_home_page_popupBtn.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, activity_home_page_popupBtn)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.activity_home_page_help -> {
                        val intent = Intent(this, IntroPageActivity::class.java)
                        intent.putExtra("from", "homePage")
                        startActivity(intent)
                        true
                    }
                    R.id.activity_home_page_addBarcode -> {
                        val intent = Intent(this, AddBarcodeActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.activity_add_emergency -> {
                        val intent = Intent(this, AddEmergencyActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.activity_home_page_exit -> {
                        finish()
                        true
                    }
                }
                true
            })
            popupMenu.inflate(R.menu.homepage_popup_menu)
            try {
                val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldPopup.isAccessible = true
                val mPopup = fieldPopup.get(popupMenu)
                mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            }catch (e: Exception){
                Log.d("myPopup", e.localizedMessage)
            }finally {
                popupMenu.show()
            }
        }

        activity_home_page_myTap.setOnClickListener(object : ClickListener() {
            override fun onSingleClick(v: View?) {
                //do nothing
            }

            override fun onDoubleClick(v: View?) {
                if (!mAndroidPermission.checkPermissionForMicrophone()) {
                    //don't perform anything if permission is denied
                } else {
                    mTextToSpeechHelper.destroySpeech()
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

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            Constants().speak("Double Tap and Speak", mTextToSpeechHelper)
        }, 500)
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
            val dataSaid = data!![0].toString().toLowerCase(Locale.ROOT)

            //rest features
            when (dataSaid) {
                "call" -> {
                    Handler().postDelayed({
                        mTextToSpeechHelper.speakEnglish("Please mention contact name")
                    },100)
                    return
                }
                "sms" -> {
                    Handler().postDelayed({
                        mTextToSpeechHelper.speakEnglish("Please mention contact name and message")
                    },100)
                    return
                }
                "weather" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(this@ImpairedUserActivity, WeatherActivity::class.java)
                    startActivity(intent)
                }
                "news" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(this@ImpairedUserActivity, NewsActivity::class.java)
                    startActivity(intent)
                }
                "detect" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(
                        this@ImpairedUserActivity,
                        ImageCaptioningActivity::class.java
                    )
                    startActivity(intent)
                }
                "help" -> {
                    mTextToSpeechHelper.destroySpeech()
                    val helpText = "Welcome to Hear Us! We are here to help you in performing your day-to-day activities more independently. \n\n" +
                            "By using this application you can Identify the objects by clicking a picture and hear the app speak the identification back to you. Hear Us is an accessible reading tool with an advanced text-to-speech feature. It describes the environment by giving you updates on the weather and the latest news. This app helps you to identify details of various products by scanning their barcode, in shopping marts or anywhere you go. It's very simple and easy to use! \n\n"+
                            "To know how to access these features, kindly double tap and speak keywords"
                    Constants().speak(helpText, mTextToSpeechHelper)
                }
                "keywords" -> {
                    mTextToSpeechHelper.destroySpeech()
                    val keywordText = "hey! now i will tell you the keywords to proceed on our application \n\n\n\n"+"To identify objects by clicking a picture use keyword - DETECT\n\n"+
                            "To scan various products and view its details use keyword - PRODUCT\n\n"+
                            "To get live weather updates use keyword - WEATHER\n\n"+
                            "To hear daily news updates of your country use keyword - NEWS\n\n"+
                            "To read texts of documents, etc use keyword - READ\n\n"+
                            "In case you get stuck anywhere take our help by using keyword - HELP\n\n"+
                            "\n\n Throughout the app swipe right to go back from any screen"
                    Constants().speak(keywordText, mTextToSpeechHelper)
                }
                "read" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(
                        this@ImpairedUserActivity,
                        TextRecognitionActivity::class.java
                    )
                    startActivity(intent)
                }
                "product" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(this@ImpairedUserActivity, ScanCodeActivity::class.java)
                    intent.putExtra("purpose", "scan")
                    startActivity(intent)
                }
                "emergency" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    CallSMSHelper(
                        this@ImpairedUserActivity
                    )
                        .emergency()
                }
                "cab" -> {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val intent = Intent(this@ImpairedUserActivity, BookCabActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Constants().speak("Sorry couldn't hear you!", mTextToSpeechHelper)
                }
            }

            //for call
            if(dataSaid.length > 3) {
                if(dataSaid.substring(0,4) == "call") {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    CallSMSHelper(
                        this@ImpairedUserActivity
                    )
                        .callMethod(data!![0].toString().substring(5, data!![0].length))
                }
            }

            //for sms
            if(dataSaid.length > 2) {
                if(dataSaid.substring(0,3) == "sms") {
                    mTextToSpeechHelper.destroySpeech()
                    if(this@ImpairedUserActivity::sr.isInitialized) {
                        sr.destroy()
                    }
                    val list = getMessageAndNameArray(data!![0].toString())
                    Log.d("mHEAR", "list : $list")
                    when {
                        list[0] == "NO CONTACT" -> {
                            Handler().postDelayed({
                                mTextToSpeechHelper.speakEnglish("Please mention contact name")
                            },100)
                        }
                        list[1] == "NO MESSAGE" -> {
                            Handler().postDelayed({
                                mTextToSpeechHelper.speakEnglish("Please mention a message")
                            },100)
                        }
                        else -> {
                            CallSMSHelper(
                                this@ImpairedUserActivity
                            )
                                .smsMethod(list[0].trim(), list[1].trim())
                        }
                    }
                }
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d(s, "onPartialResults")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.d(s, "onEvent $eventType")
        }

        private fun getMessageAndNameArray(text: String) : ArrayList<String> {
            val list = arrayListOf<String>()

            val array = text.split(" ")
            val toIndex = getToIndex(array)
            var contact = ""
            if(toIndex == -1 || toIndex == array.size-1) {
                list.add("NO CONTACT")
            } else {
                for(i in toIndex+1 until array.size) {
                    contact += array[i] + " "
                }
                list.add(contact)
            }

            var message = ""
            for(i in 1 until toIndex) {
                message += array[i] + " "
            }
            if(message == "") {
                message = "NO MESSAGE"
            }
            list.add(message)
            return list
        }

        private fun getToIndex(array: List<String>) : Int {
            for(i in array.size-1 downTo 0) {
                if(array[i].toLowerCase(Locale.ROOT) == "to") {
                    return i
                }
            }
            return -1
        }
    }
}