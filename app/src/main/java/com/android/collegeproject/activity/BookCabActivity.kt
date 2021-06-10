package com.android.collegeproject.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.android.collegeproject.R
import com.android.collegeproject.api.BookCabAPI
import com.android.collegeproject.helper.*
import com.android.collegeproject.model.Bookings
import com.android.collegeproject.model.CabMessage
import com.android.collegeproject.model.Cabs
import kotlinx.android.synthetic.main.activity_book_cab.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

class BookCabActivity: AppCompatActivity() {

    private var toFetch: Boolean = true
    private lateinit var mLocationHelper : LocationHelper
    private lateinit var mMyLocation: ArrayList<Double>
    private lateinit var mTextToSpeechHelper: TextToSpeechHelper
    private lateinit var mDestination: String
    private lateinit var mCabHelper: BookCabHelper
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mSharedPreferencesLocation: SharedPreferences
    private lateinit var mAndroidPermission: AndroidPermissions
    private lateinit var sr: SpeechRecognizer
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http:/192.168.1.7:2332")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_cab)
        mMyLocation = ArrayList<Double>()
        mLocationHelper = LocationHelper(this, this)
        mCabHelper = BookCabHelper(this)
        mTextToSpeechHelper = TextToSpeechHelper(this)
        mAndroidPermission = AndroidPermissions(this)
        mSharedPreferences = this.getSharedPreferences(
            Constants().CAB,
            MODE_PRIVATE
        )
        mSharedPreferencesLocation = this.getSharedPreferences(
            Constants().LOCATION,
            MODE_PRIVATE
        )
        val type = intent.getStringExtra("type")
        if(type == "direct") {
            ifTypeDirect()
        } else {
            ifTypeBooking()
        }

        activity_book_cab_view2.setOnClickListener(object : ClickListener() {
            override fun onSingleClick(v: View?) {
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
                        this@BookCabActivity.packageName
                    )

                    // Add custom listeners.
                    val listener = CustomRecognitionListener()
                    sr = SpeechRecognizer.createSpeechRecognizer(this@BookCabActivity)
                    sr.setRecognitionListener(listener)
                    sr.startListening(intent)
                }
            }

        })

    }

    private fun ifTypeDirect() {
        activity_book_cab_loader_text.text = "Hold On!\nWe are fetching the booking details!"
        Handler().postDelayed({
            mTextToSpeechHelper.speakEnglish("Hold On!\nWe are fetching the booking details")
        }, 500)

        Handler().postDelayed({
            updateUI(mSharedPreferences)
        }, 4000)
    }

    private fun ifTypeBooking() {

        activity_book_cab_loader_text.text = "Hold On!\nWe are connecting you with the nearest driver!"
        mDestination = intent.getStringExtra("destination")

        Handler().postDelayed({
            mTextToSpeechHelper.speakEnglish("Hold On!\nWe are connecting you with the nearest driver!")
        }, 500)

        Handler().postDelayed({
            mMyLocation.add(mSharedPreferencesLocation.getString(
                Constants().LATITUDE,
                ""
            )!!.toDouble())
            mMyLocation.add(mSharedPreferencesLocation.getString(
                Constants().LONGITUDE,
                ""
            )!!.toDouble())

            Log.d("mMyLocation", mMyLocation.toString())
            val from = mCabHelper.getAddressFromLocation(mMyLocation[0], mMyLocation[1])
            val toLoc = mCabHelper.getLocationFromAddress(mDestination)
            val distance = mCabHelper.calculateDistanceInKm(
                mMyLocation[0],
                mMyLocation[1],
                toLoc[0],
                toLoc[1]
            )
            val time = mCabHelper.clacTimeInHrs(distance)
            val service = retrofit.create(BookCabAPI::class.java)
            service.postBooking(mMyLocation[0], mMyLocation[1], mDestination)
                .enqueue(object : Callback<Cabs>{
                    override fun onResponse(call: Call<Cabs>, response: Response<Cabs>) {
                        if(response.code() == 404) {
                            toFetch = false
                            mTextToSpeechHelper.speakEnglish("No Cabs Available")
                            finish()
                            return
                        }
                        if(response.body() != null) {
                            mSharedPreferences.edit().putString(
                                Constants().CAB_FROM,
                                from
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_TO,
                                mDestination
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_ID,
                                response.body()!!._id
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_VEHICLE,
                                response.body()!!.vehicle
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_DRIVER_NAME,
                                response.body()!!.name
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_VEHICLE_NUMBER,
                                response.body()!!.vehicleNumber
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_VEHICLE_TYPE,
                                response.body()!!.vehicleType
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_CONTACT_NUMBER,
                                response.body()!!.contact
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_FARE,
                                mCabHelper.calculateFare(distance, time).toString()
                            ).apply()
                            mSharedPreferences.edit().putString(
                                Constants().CAB_ESTIMATED_TIME,
                                mCabHelper.clacTimeInHrs(distance).toString()
                            ).apply()
                        } else {
                            mTextToSpeechHelper.speakEnglish("No Cabs Available")
                            Handler().postDelayed({
                                finish()
                            },1000)
                        }

                    }

                    override fun onFailure(call: Call<Cabs>, t: Throwable) {
                        Log.d("myCAB", t.localizedMessage)
                    }

                })
        }, 4000)

        Handler().postDelayed({
            if(toFetch) {
                updateUI(mSharedPreferences)
            }
        }, 8000)
    }

    private fun updateUI(sharedPreferences: SharedPreferences) {
        activity_book_cab_from.text = sharedPreferences.getString(Constants().CAB_FROM, "")
        activity_book_cab_to.text = sharedPreferences.getString(Constants().CAB_TO, "")
        activity_book_cab_vehicleName.text = sharedPreferences.getString(Constants().CAB_VEHICLE, "")
        getCabStatus()
        activity_book_cab_driverName.text = sharedPreferences.getString(Constants().CAB_DRIVER_NAME, "")
        activity_book_cab_vehicleNumber.text = sharedPreferences.getString(Constants().CAB_VEHICLE_NUMBER, "")
        activity_book_cab_contact.text = sharedPreferences.getString(Constants().CAB_CONTACT_NUMBER, "")
//        val hours : Int = sharedPreferences.getString(Constants().CAB_ESTIMATED_TIME, "")!!.toInt()/60
//        val mins : Int = sharedPreferences.getString(Constants().CAB_ESTIMATED_TIME, "")!!.toInt() % 60
        activity_book_cab_estTime.text = "${sharedPreferences.getString(Constants().CAB_ESTIMATED_TIME, "")} min(s)"
        activity_book_cab_fare.text = "Rs. "+sharedPreferences.getString(Constants().CAB_FARE, "")
        activity_book_cab_loader.visibility = View.GONE
        activity_book_cab_view1.visibility = View.VISIBLE
        activity_book_cab_view2.visibility = View.VISIBLE
        activity_book_cab_view3.visibility = View.VISIBLE
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

            when (data!![0].toString().toLowerCase(Locale.ROOT)) {
                "status" -> {
                    getCabStatus()
                    return
                }
                "estimated time" -> {
                    Handler().postDelayed({
                        mTextToSpeechHelper.speakEnglish("${mSharedPreferences.getString(Constants().CAB_ESTIMATED_TIME, "")} min(s)")
                    },100)
                }
                "ride fare" -> {
                    Handler().postDelayed({
                        mTextToSpeechHelper.speakEnglish("Rs. "+mSharedPreferences.getString(Constants().CAB_FARE,""))
                    },100)
                }
                "delete" -> {
                    val service = retrofit.create(BookCabAPI::class.java)
                    service.deleteBooking(mSharedPreferences.getString(Constants().CAB_ID, "")!!)
                        .enqueue(object : Callback<CabMessage>{
                            override fun onResponse(
                                call: Call<CabMessage>,
                                response: Response<CabMessage>
                            ) {
                                removeSavedData()
                                mTextToSpeechHelper.speakEnglish("Trip Ended")
                                Handler().postDelayed({
                                    finish()
                                },1000)
                            }

                            override fun onFailure(call: Call<CabMessage>, t: Throwable) {
                                TODO("Not yet implemented")
                            }

                        })
                }
                "call" -> {
                    Handler().postDelayed({
                        mTextToSpeechHelper.speakEnglish("Calling driver!")
                    }, 100)
                    android.os.Handler().postDelayed({
                        val dial = "tel:${mSharedPreferences.getString(Constants().CAB_CONTACT_NUMBER, "")}"
                        this@BookCabActivity.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
                    }, 2500)
                }
                else -> {
                    mTextToSpeechHelper.speakEnglish("Sorry couldn't hear you!")
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

    private fun getCabStatus() {
        val service = retrofit.create(BookCabAPI::class.java)
        service.getStatus(mSharedPreferences.getString(Constants().CAB_ID, "")!!)
            .enqueue(object : Callback<Bookings>{
                override fun onResponse(
                    call: Call<Bookings>,
                    response: Response<Bookings>
                ) {
                    activity_book_cab_status.text = response.body()!!.status
                    mTextToSpeechHelper.speakEnglish(response.body()!!.status)
                }

                override fun onFailure(call: Call<Bookings>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun removeSavedData() {
        mSharedPreferences.edit().putString(
            Constants().CAB_FROM,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_TO,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_ID,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_VEHICLE,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_DRIVER_NAME,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_VEHICLE_NUMBER,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_VEHICLE_TYPE,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_CONTACT_NUMBER,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_FARE,
            ""
        ).apply()
        mSharedPreferences.edit().putString(
            Constants().CAB_ESTIMATED_TIME,
            ""
        ).apply()
    }
}

