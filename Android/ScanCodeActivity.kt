package com.android.texttospeech

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import com.android.texttospeech.Helper.AndroidPermissions
import com.android.texttospeech.Helper.TextToSpeechHelper
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_main_tts.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.*

class ScanCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var mScannerView: ZXingScannerView
    private lateinit var mTextToSpeechHelper: TextToSpeechHelper
    private lateinit var mAndroidPermissions: AndroidPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)

        mAndroidPermissions = AndroidPermissions(this)

        if(!mAndroidPermissions.checkPermissionForCamera()) {
              //don't perform anything if permission is denied
        } else {
            setContentView(mScannerView)
        }

        mTextToSpeechHelper = TextToSpeechHelper(this)
    }

    override fun handleResult(rawResult: Result?) {
        Log.d("myTEXT", rawResult!!.toString())
        val text = rawResult!!.text
        mTextToSpeechHelper.speakHindi(text)
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        mTextToSpeechHelper.destroySpeech()
        mScannerView.stopCamera()
    }
}