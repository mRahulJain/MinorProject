package com.android.collegeproject.helper

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.database.Cursor
import android.location.Geocoder
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class CallSMSHelper {

    private var mContext: AppCompatActivity
    private var mAndroidPermission: AndroidPermissions
    private var mTextToSpeechHelper: TextToSpeechHelper
    private var mLocationHelper: LocationHelper

    constructor(context: AppCompatActivity) {
        mContext = context
        mAndroidPermission = AndroidPermissions(context)
        mTextToSpeechHelper = TextToSpeechHelper(context)
        mLocationHelper = LocationHelper(mContext, mContext)
    }

    fun callMethod(name: String) {
        if(!mAndroidPermission.checkPermissionForContacts()) {
            //do nothing
        } else {
            makeCall(name)
        }
    }

    fun smsMethod(name: String, message: String) {
        if(!mAndroidPermission.checkPermissionForSendSMS()) {
            //do nothing
        } else {
            sendMessage(name, message)
        }
    }

    fun emergency() {
        if(!mAndroidPermission.checkPermissionForSendSMS() && !mAndroidPermission.checkPermissionForLocation()) {
            //do nothing
        } else {
            contactEmergency()
        }
    }

    private fun makeCall(name: String) {
        try {
            val cursor: Cursor? = mContext.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE
                ),
                "DISPLAY_NAME LIKE '%$name%'", null, null
            )
            if(cursor == null) {
                return
            }
            cursor!!.moveToFirst()
            val number: String = cursor.getString(0)
            if (number.trim { it <= ' ' }.isNotEmpty()) {
                if(mAndroidPermission.checkPermissionForCallPhone()) {
                    android.os.Handler().postDelayed({
                        mTextToSpeechHelper.speakEnglish("Calling $name")
                    }, 100)
                    android.os.Handler().postDelayed({
                        val dial = "tel:$number"
                        mContext.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
                    }, 2500)
                } else {
                    //do nothing
                }
            } else {
                //do nothing
            }
        } catch (e: Exception) {
            android.os.Handler().postDelayed({
                mTextToSpeechHelper.speakEnglish("Couldn't find $name")
            }, 100)
            e.printStackTrace()
        }
    }

    private fun sendMessage(name: String, message: String) {
        try {
            val cursor: Cursor? = mContext.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE
                ),
                "DISPLAY_NAME LIKE '%$name%'", null, null
            )
            if(cursor == null) {
                return
            }
            cursor!!.moveToFirst()
            val number: String = cursor.getString(0)
            if (number.trim { it <= ' ' }.isNotEmpty()) {
                android.os.Handler().postDelayed({
                    val smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(number, null, message, null, null)
                    mTextToSpeechHelper.speakEnglish("Message sent to $name")
                }, 500)
            } else {
                //do nothing
            }
        } catch (e: Exception) {
            android.os.Handler().postDelayed({
                mTextToSpeechHelper.speakEnglish("Couldn't find $name")
            }, 100)
            e.printStackTrace()
        }
    }

    private fun sendEmergencyMessage(name: String, message: String) {
        try {
            val cursor: Cursor? = mContext.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE
                ),
                "DISPLAY_NAME LIKE '%$name%'", null, null
            )
            if(cursor == null) {
                return
            }
            cursor!!.moveToFirst()
            val number: String = cursor.getString(0)
            if (number.trim { it <= ' ' }.isNotEmpty()) {
                android.os.Handler().postDelayed({
                    val smsManager: SmsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(number, null, message, null, null)
                }, 500)
            } else {
                //do nothing
            }
        } catch (e: Exception) {
            android.os.Handler().postDelayed({
                mTextToSpeechHelper.speakEnglish("Couldn't find $name")
            }, 100)
            e.printStackTrace()
        }
    }

    private fun contactEmergency() {
        android.os.Handler().postDelayed({
            val location = mLocationHelper.getLocation()
            val address = getAddress(location[0], location[1])
            val sharedPreferences = mContext.getSharedPreferences(
                Constants().EMERGENCY,
                MODE_PRIVATE
            )
            val names = sharedPreferences.getString(Constants().EMERGENCY_NAMES, "")
            val nameList = names!!.split(",")
            for(i in nameList.indices) {
                sendEmergencyMessage(nameList[i], "$address\nhttps://maps.google.com/?q=${location[0]},${location[1]}")
            }
        }, 500)
    }

    private fun getAddress(latitude: Double, longitude: Double) : String {
        var myCity = ""

        val geoCoder = Geocoder(mContext, Locale.getDefault())
        try {
            val addresses = geoCoder.getFromLocation(
                latitude,
                longitude,
                1
            )
            myCity = addresses[0].getAddressLine(0)
        } catch (e : Exception) {
            Log.e("PUI", "${e.printStackTrace()}")
        }
        return myCity
    }
}