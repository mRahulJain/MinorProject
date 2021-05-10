package com.android.collegeproject.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.collegeproject.api.ApiBarcode
import com.android.collegeproject.helper.AndroidPermissions
import com.android.collegeproject.helper.Constants
import com.android.collegeproject.helper.TextToSpeechHelper
import com.android.collegeproject.model.Product
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScanCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler  {

    private lateinit var mScannerView: ZXingScannerView
    private lateinit var mAndroidPermissions: AndroidPermissions
    private lateinit var mPurpose: String
    private lateinit var mTextToSpeechHelper: TextToSpeechHelper
    val IP_HOST = "https://product-recog-hearus-123.herokuapp.com"
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(IP_HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)
        mPurpose = intent.getStringExtra("purpose")
        mTextToSpeechHelper = TextToSpeechHelper(this)
        mAndroidPermissions = AndroidPermissions(this)

        Log.d("scan code", "Entered to scan code activity")
        if(!mAndroidPermissions.checkPermissionForCamera()) {
            //don't perform anything if permission is denied
        } else {
            setContentView(mScannerView)
        }
    }

    override fun handleResult(rawResult: Result?) {
        val text = rawResult!!.text

        if(mPurpose == "add") {
            val intent = Intent()
            intent.putExtra("barcode", text)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            getProductDetailsIfExists(text)
        }
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

    override fun onBackPressed() {
        super.onBackPressed()
        if(mPurpose == "add") {
            val intent = Intent(this, ImpairedUserActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mScannerView.stopCamera()
    }

    private fun getProductDetailsIfExists(mBarcode: String) {

        val getProductService = retrofit.create(ApiBarcode::class.java)
        Log.d("scan code", "before retrofit call ${mBarcode}, ")
        getProductService.getProduct(mBarcode).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                 if(response.isSuccessful) {
                    if(response.body()!!.productBarcode != "-1") {
                        Constants().speak(response.body()!!.productName, mTextToSpeechHelper)
                    } else {
                        Constants().speak("Product does not exist", mTextToSpeechHelper)
                    }
                    mScannerView.setResultHandler(this@ScanCodeActivity)
                    mScannerView.startCamera()
                } else {
                    Log.d("myBarcode E", response.message())
                }
            }
            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d("myBarcode", t.localizedMessage)
            }

        })
    }
}