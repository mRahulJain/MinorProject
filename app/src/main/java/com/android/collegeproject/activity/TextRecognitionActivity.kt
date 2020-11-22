package com.android.collegeproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import com.android.collegeproject.R
import com.android.collegeproject.helper.*
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.IOException
import java.lang.StringBuilder

class TextRecognitionActivity : AppCompatActivity() {

    private lateinit var mCameraView : SurfaceView
    private lateinit var mCameraSource: CameraSource
    private lateinit var mAndroidPermissions: AndroidPermissions
    private lateinit var mTextToSpeechHelper: TextToSpeechHelper
    private var check: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)

        mCameraView = findViewById(R.id.activity_text_recognition_surfaceView)
        mAndroidPermissions = AndroidPermissions(this)
        mTextToSpeechHelper = TextToSpeechHelper(this)

        val loadingString = "Hello! \n\n\n To scan a document kindly hold your phone firmly or place it on a surface.\n\n"+
                " use your phone's camera to scan the document \n wait for a few seconds. \n\n"+
                "then swipe up to hear the scanned document\n\n\n\n\nYou can swipe right to go back."
        Handler().postDelayed({
            Constants().speak(loadingString, mTextToSpeechHelper)
        }, 500)

        val textRecognizer = TextRecognizer.Builder(applicationContext).build()
        if(!textRecognizer.isOperational) {
            Log.w("TextRecognition", "Detector dependencies are not available yet")
        } else {
            val display = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(display)
            val height = display.heightPixels
            val width = display.widthPixels
            mCameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(height, width)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build()
            mCameraView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
                }

                override fun surfaceDestroyed(p0: SurfaceHolder?) {
                    mCameraSource.stop()
                }

                @SuppressLint("MissingPermission")
                override fun surfaceCreated(p0: SurfaceHolder?) {
                    try {
                        if(!mAndroidPermissions.checkPermissionForCamera()) {
                            //don't perform anything if permission is denied
                        } else {
                            mCameraSource.start(mCameraView.holder)
                        }
                    } catch (e: IOException) {
                        Log.e("TextRecognition", e.localizedMessage)
                    }
                }
            })
        }

        mCameraView.setOnTouchListener(object : SwipeListener(this) {
            override fun onSwipeRight() {
                //close window
                mTextToSpeechHelper.destroySpeech()
                finish()
            }

            override fun onSwipeLeft() {
            }

            @SuppressLint("MissingPermission")
            override fun onSwipeTop() {
                if(!mAndroidPermissions.checkPermissionForCamera()) {
                    // do nothing
                } else {
                    mCameraSource.start(mCameraView.holder)
                    textRecognizer.setProcessor(object: Detector.Processor<TextBlock> {
                        override fun release() {
                        }

                        override fun receiveDetections(p0: Detector.Detections<TextBlock>?) {
                            val items = p0!!.detectedItems
                            if(items.size() != 0) {
                                //different thread
                                mCameraView.post {
                                    if(!check) {
                                        check = true
                                        val stringBuilder = StringBuilder()
                                        for(i in 0 until items.size() - 1) {
                                            val text = items[i]
                                            stringBuilder.append(text.value)
                                            if (i == items.size()-2)
                                                stringBuilder.append(" \"")
                                            else
                                                stringBuilder.append("\n")
                                        }
                                        val text =
                                            "\" $stringBuilder\n\n\n That's all! \n\n Swipe right to return to the main screen"
                                        val intent = Intent(this@TextRecognitionActivity, TextRecognitionTextActivity::class.java)
                                        intent.putExtra("text", text)
                                        startActivity(intent)
                                        mCameraSource.stop()
                                        finish()
                                    }
                                }
                            }
                        }

                    })
                }
            }

            override fun onSwipeBottom() {
            }

        })
    }
}