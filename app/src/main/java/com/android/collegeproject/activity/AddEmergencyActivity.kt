package com.android.collegeproject.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.android.collegeproject.R
import com.android.collegeproject.helper.Constants
import kotlinx.android.synthetic.main.activity_add_emergency.*

class AddEmergencyActivity() : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_emergency)

        sharedPreferences = this.getSharedPreferences(
            Constants().EMERGENCY,
            MODE_PRIVATE
        )

        var names = sharedPreferences.getString(Constants().EMERGENCY_NAMES, "")
        activity_add_emergency_displayText.text = names

        activity_add_emergency_addContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101 && resultCode == Activity.RESULT_OK && data!!.data != null) {
            val contactUri = data!!.data
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            val cursor = this.contentResolver.query(contactUri!!, projection,
                null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                var names = sharedPreferences.getString(Constants().EMERGENCY_NAMES, "")
                names += if(names == "") {
                    "${cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))}"
                } else {
                    ",${cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))}"
                }

                sharedPreferences.edit().putString(
                    Constants().EMERGENCY_NAMES,
                    names
                ).apply()

                activity_add_emergency_displayText.text = names
            }
        }
    }
}