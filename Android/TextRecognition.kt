package com.android.texttospeech

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.texttospeech.Helper.AndroidPermissions
import com.android.texttospeech.Helper.DoubleClickListener
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

class TextRecognition : AppCompatActivity() {

    lateinit var cameraView : SurfaceView
    lateinit var cameraSource: CameraSource
    lateinit var mAndroidPermissions: AndroidPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)

        cameraView = findViewById(R.id.surfaceView)
        mAndroidPermissions = AndroidPermissions(this)


//        cameraView.setOnClickListener(object : DoubleClickListener(){
//            @SuppressLint("MissingPermission")
//            override fun onDoubleClick(v: View?) {
//                try {
//                    if(!mAndroidPermissions.checkPermissionForCamera()) {
//                        mAndroidPermissions.requestPermissionForCamera()
//                    } else {
//                        cameraSource.start(cameraView.holder)
//                    }
//                } catch (e: IOException) {
//                    Log.e("TextRecognition", e.localizedMessage)
//                }
//            }
//            override fun onSingleClick(v: View?) {
//            }
//        })

        val textRecognizer = TextRecognizer.Builder(applicationContext).build()
        if(!textRecognizer.isOperational) {
            Log.w("TextRecognition", "Detector dependencies are not available yet")
        } else {
            val display = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(display)
            val height = display.heightPixels
            val width = display.widthPixels
            cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(height, width)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build()
            cameraView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
                }

                override fun surfaceDestroyed(p0: SurfaceHolder?) {
                    cameraSource.stop()
                }

                @SuppressLint("MissingPermission")
                override fun surfaceCreated(p0: SurfaceHolder?) {
                    try {
                        if(!mAndroidPermissions.checkPermissionForCamera()) {
                            //don't perform anything if permission is denied
                        } else {
                            cameraSource.start(cameraView.holder)
                        }
                    } catch (e: IOException) {
                        Log.e("TextRecognition", e.localizedMessage)
                    }
                }

            })
            textRecognizer.setProcessor(object: Detector.Processor<TextBlock> {
                override fun release() {
                }

                override fun receiveDetections(p0: Detector.Detections<TextBlock>?) {
                    val items = p0!!.detectedItems
                    if(items.size() != 0) {
                        //different thread
//                        textView.post {
//                            val stringBuilder = StringBuilder()
//                            for(i in 0 until items.size() - 1) {
//                                val text = items[i]
//                                stringBuilder.append(text.value)
//                                stringBuilder.append("\n")
//                            }
//                            val text = "$stringBuilder\n\nDouble Tap to scan again"
////                            textView.text = text
//                        }
                    }
                }

            })
        }
    }
}