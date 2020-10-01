package com.android.texttospeech

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.texttospeech.Helper.AndroidPermissions
import com.android.texttospeech.Helper.SpeechToTextHelper
import kotlinx.android.synthetic.main.activity_main_stt.*

class STT : AppCompatActivity() {

    private lateinit var mSpeechToTextHelper: SpeechToTextHelper
    private lateinit var mAndroidPermissions: AndroidPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_stt)

        mAndroidPermissions = AndroidPermissions(this)
        mSpeechToTextHelper = SpeechToTextHelper(this)

        activity_main_button3.setOnClickListener {
            if(!mAndroidPermissions.checkPermissionForMicrophone()) {
                //don't perform anything if permission is denied
            } else {
                mSpeechToTextHelper.makeText(activity_main_editText2)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSpeechToTextHelper.destroyListening()
    }

}