package com.android.collegeproject.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.collegeproject.R
import com.android.collegeproject.api.ApiImageCaptioning
import com.android.collegeproject.helper.AndroidPermissions
import com.android.collegeproject.helper.Constants
import com.android.collegeproject.helper.TextToSpeechHelper
import com.android.collegeproject.model.Description
import com.android.collegeproject.model.PostBody
import com.google.common.net.MediaType.PNG
import kotlinx.android.synthetic.main.activity_image_captioning.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ImageCaptioningActivity : AppCompatActivity() {

    private var ORIENTATIONS: SparseIntArray = SparseIntArray()
    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

    private lateinit var cameraId: String
    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder

    private lateinit var imageDimension: Size
    private lateinit var handler: Handler
    private lateinit var backgroundThread: HandlerThread

    val BASE_URL_IMAGE_CAPTIONING = "https://hear-us-app.herokuapp.com/"
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_IMAGE_CAPTIONING)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_captioning)

        activity_image_captioning_textureView.surfaceTextureListener = listener

        activity_image_captioning_button.setOnClickListener {
            takePicture()
        }
    }

    private val listener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
        }

        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
        }

    }

    private var stateCallback : CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(p0: CameraDevice) {
            cameraDevice = p0
            try {
                createCameraPreview()
            } catch (e: CameraAccessException) {
                Log.e("myError", e.localizedMessage)
            }
        }

        override fun onDisconnected(p0: CameraDevice) {
            cameraDevice.close()
        }

        override fun onError(p0: CameraDevice, p1: Int) {
            cameraDevice.close()
        }

    }

    @Throws(CameraAccessException::class)
    private fun createCameraPreview() {
        val texture = activity_image_captioning_textureView.surfaceTexture
        texture.setDefaultBufferSize(imageDimension.width, imageDimension.height)
        val surface = Surface(texture)

        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)

        cameraDevice.createCaptureSession(
            listOf(surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(p0: CameraCaptureSession) {
                    if (cameraDevice == null) {
                        return
                    }

                    cameraCaptureSession = p0
                    updatePreview()
                }

                override fun onConfigureFailed(p0: CameraCaptureSession) {
                    Log.e("myError", p0.toString())
                }

            },
            null
        )
    }

    private fun updatePreview() {
        if(cameraDevice == null) {
            return
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, handler)
    }

    private fun openCamera() {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = manager.cameraIdList[0]
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            imageDimension = map!!.getOutputSizes(SurfaceTexture::class.java)[0]
            if(!AndroidPermissions(this).checkPermissionForCamera()) {
                return
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            manager.openCamera(cameraId, stateCallback, null)
        } catch (e: CameraAccessException) {
            Log.e("myError", e.localizedMessage)
        }
    }

    private fun takePicture() {
        if(cameraDevice == null) {
            return
        }

        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val characteristics = manager.getCameraCharacteristics(cameraDevice.id)
            var jpegSizes: Array<Size>? = null
            if(characteristics!=null) {
                jpegSizes = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                    .getOutputSizes(ImageFormat.JPEG)

                var width = 640
                var height = 360

                if(jpegSizes != null && jpegSizes.isNotEmpty()) {
                    width = jpegSizes[0].width
                    height = jpegSizes[0].height
                }

                val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
                var outputSurface: ArrayList<Surface> = arrayListOf()
                outputSurface.add(reader.surface)
                outputSurface.add(Surface(activity_image_captioning_textureView.surfaceTexture))

                val captureBuilder: CaptureRequest.Builder = cameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_STILL_CAPTURE
                )
                captureBuilder.addTarget(reader.surface)
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

                val rotation: Int = windowManager.defaultDisplay.rotation
                captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))

                reader.setOnImageAvailableListener({
                    var image: Image? = null
                    try {
                        image = it.acquireLatestImage()
                        val buffer = image.planes[0].buffer
                        var bytes = ByteArray(buffer.remaining())
                        buffer.get(bytes)

                        val body = PostBody(bytes)

                        val imageCaptioningService = retrofit.create(ApiImageCaptioning::class.java)
                        imageCaptioningService.getDescription(body).enqueue(object :
                            Callback<Description> {
                            override fun onResponse(
                                call: Call<Description>,
                                response: Response<Description>
                            ) {
                                if (response.isSuccessful) {
                                    val description = response.body()
                                    Log.d(
                                        "myBYTE",
                                        "On Response Successful = " + description!!.description
                                    )
                                    Constants().speak(
                                        description!!.description,
                                        TextToSpeechHelper(this@ImageCaptioningActivity)
                                    )
                                } else {
                                    Log.d(
                                        "myBYTE",
                                        "On Response Not Successful = " + response.message()
                                    )
                                }
                            }

                            override fun onFailure(call: Call<Description>, t: Throwable) {
                                Log.d("myBYTE", "On Failure = " + t.localizedMessage)
                            }

                        })
                    } catch (e: Exception) {
                        Log.d("myBYTE", "Exception = " + e.localizedMessage)
                    } finally {
                        image?.close()
                    }
                }, handler)

                val captureListener = object : CameraCaptureSession.CaptureCallback() {
                    override fun onCaptureCompleted(
                        session: CameraCaptureSession,
                        request: CaptureRequest,
                        result: TotalCaptureResult
                    ) {
                        super.onCaptureCompleted(session, request, result)
                        Toast.makeText(this@ImageCaptioningActivity, "OKAY", Toast.LENGTH_SHORT).show()
                        try {
                            createCameraPreview()
                        } catch (e: CameraAccessException) {
                            Log.e("myError", e.localizedMessage)
                        }
                    }
                }

                cameraDevice.createCaptureSession(
                    outputSurface,
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(p0: CameraCaptureSession) {
                            try {
                                p0.capture(captureBuilder.build(), captureListener, handler)
                            } catch (e: CameraAccessException) {
                                Log.e("myError", e.localizedMessage)
                            }
                        }

                        override fun onConfigureFailed(p0: CameraCaptureSession) {
                            TODO("Not yet implemented")
                        }
                    },
                    handler
                )
            }
        } catch (e: CameraAccessException) {

        }
    }

    override fun onResume() {
        super.onResume()

        startBackgroundThread()

        if(activity_image_captioning_textureView.isAvailable) {
            try {
                openCamera()
            } catch (e: CameraAccessException) {
                Log.e("myError", e.localizedMessage)
            }
        } else {
            activity_image_captioning_textureView.surfaceTextureListener = listener
        }
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("Camera Background")
        backgroundThread.start()
        handler = Handler(backgroundThread.looper)
    }

    override fun onPause() {
        try {
            stopBackgroundThread()
        } catch (e: InterruptedException) {
            Log.e("myError", e.localizedMessage)
        }
        super.onPause()
    }

    @Throws(InterruptedException::class)
    private fun stopBackgroundThread() {
        backgroundThread.quitSafely()
        backgroundThread.join()
    }

}