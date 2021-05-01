package com.android.collegeproject.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.loader.content.CursorLoader
import com.android.collegeproject.R
import com.android.collegeproject.R.drawable
import com.android.collegeproject.helper.Constants
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.activity_add_emergency.*
import kotlin.random.Random

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

        when (names) {
            "" -> {
                val chip = TextView(activity_add_barcode_chipGroup.context)
                chip.hint= "You contact names appear here"
                chip.setHintTextColor(Color.parseColor("#808080"))
                // necessary to get single selection working
                activity_add_barcode_chipGroup.addView(chip)
            }
            else -> {
               val contactNames : List<String> = names!!.split(",")
                for (index in contactNames) {
                    val chip = Chip(activity_add_barcode_chipGroup.context)
                    chip.setTextColor(Color.WHITE)
                    when {
                       contactNames.indexOf(index)%2 == 0 ->{
                            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#ffeb707a" ))
                        }else -> {
                        chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#ffeba370"))
                        }
                    }
                    chip.text= "${index}"
                    // necessary to get single selection working
                    activity_add_barcode_chipGroup.addView(chip)
                }
            }
        }

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
                activity_add_barcode_chipGroup!!.removeAllViews()
                val contactNames : List<String> = names!!.split(",")
                for (index in contactNames) {
                    val chip = Chip(activity_add_barcode_chipGroup.context)
                    chip.text= "${index}"
                    when {
                        contactNames.indexOf(index)%2 == 0 ->{
                            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#ffeb707a" ))
                        }else -> {
                        chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#ffeba370"))
                    }
                    }
                    activity_add_barcode_chipGroup.addView(chip)
                }
                sharedPreferences.edit().putString(
                    Constants().EMERGENCY_NAMES,
                    names
                ).apply()

               // activity_add_emergency_displayText.text = names
            }
        }
    }
}