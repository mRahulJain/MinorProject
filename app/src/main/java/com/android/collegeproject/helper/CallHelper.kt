package com.android.collegeproject.helper

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.logging.Handler

class CallHelper {

    private var mContext: AppCompatActivity
    private var mAndroidPermission: AndroidPermissions
    private var mCallingPerson: String
    private lateinit var mTextToSpeechHelper: TextToSpeechHelper

    constructor(context: AppCompatActivity, name: String) {
        mContext = context
        mAndroidPermission = AndroidPermissions(context)
        mCallingPerson = name
        mTextToSpeechHelper = TextToSpeechHelper(context)
    }

    fun callMethod() {
        if(!mAndroidPermission.checkPermissionForContacts()) {
            //do nothing
        } else {
            makeCall(mCallingPerson)
        }
    }

    private fun makeCall(name: String) {
        try {
            val cursor: Cursor? = mContext.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE),
                "DISPLAY_NAME = '$name'", null, null)
            if(cursor == null) {
                return
            }
            cursor!!.moveToFirst()
            val number: String = cursor.getString(0)
            if (number.trim { it <= ' ' }.isNotEmpty()) {
                if (!mAndroidPermission.checkPermissionForCallPhone()) {
                    //do nothing
                } else {
                    android.os.Handler().postDelayed({
                        mTextToSpeechHelper.speakEnglish("Calling $name")
                    }, 100)
                    android.os.Handler().postDelayed({
                        val dial = "tel:$number"
                        mContext.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
                    }, 2500)
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
}