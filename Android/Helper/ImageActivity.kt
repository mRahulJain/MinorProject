package com.android.texttospeech.Helper

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.texttospeech.R
import kotlinx.android.synthetic.main.activity_image.*
import java.io.ByteArrayOutputStream
import java.util.*


class ImageActivity : AppCompatActivity() {

    val PICK_IMAGE_REQUEST = 1
    var imageUri : Uri? = null
    var finalImage :ByteArray = ByteArray(1000)
    var flagImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        selectImage.setOnClickListener {
            flagImage = true
            openFileChooser()
        }

        convert.setOnClickListener {
            val bmp = BitmapFactory.decodeByteArray(finalImage, 0, finalImage!!.size)
            image.setImageBitmap(
                Bitmap.createScaledBitmap(
                    bmp, image.width,
                    image.height, false
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST &&
            resultCode == AppCompatActivity.RESULT_OK &&
            data != null && data.data != null) {
            flagImage = true
            imageUri = data.data

            val source = ImageDecoder.createSource(this.contentResolver, imageUri!!)
            val bitmap = ImageDecoder.decodeBitmap(source)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
            finalImage = baos.toByteArray()
            byteArray.setText(finalImage.contentToString())
        } else {
            flagImage = false
        }
    }

    private fun getFileExtension(uri: Uri) : String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

}
