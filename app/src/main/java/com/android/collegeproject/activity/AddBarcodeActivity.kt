package com.android.collegeproject.activity

import android.R.color
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.component1
import com.android.collegeproject.R
import com.android.collegeproject.api.ApiBarcode
import com.android.collegeproject.helper.Constants
import com.android.collegeproject.model.Product
import kotlinx.android.synthetic.main.activity_add_barcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddBarcodeActivity : AppCompatActivity() {
    val IP_HOST = "https://product-recog-hearus-123.herokuapp.com"
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(IP_HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var mBarcode: String = ""
    private lateinit var mIntent: Intent

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_barcode)

        mIntent = Intent(this, ScanCodeActivity::class.java)
        mIntent.putExtra("purpose", "add")

        startActivityForResult(mIntent, 123)

        activity_add_barcode_button.setOnClickListener {
            val mName = activity_add_barcode_editTextName.text.toString()
            if(mName == "" || mBarcode == "") {
                return@setOnClickListener
            } else {
                val addProductService = retrofit.create(ApiBarcode::class.java)
                addProductService.addProduct(mBarcode, mName).enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful) {
                            if(response.body()!!.toString() == "Product added") {
                                activity_add_barcode_editTextName.setText("")
                                activity_add_barcode_editTextBarcode.setText("")
                                Toast.makeText(this@AddBarcodeActivity,
                                "Added",
                                Toast.LENGTH_SHORT).show()
                                startActivityForResult(mIntent, 123)
                            } else if(response.body()!!.toString() == "Product Updated") {
                                activity_add_barcode_editTextName.setText("")
                                activity_add_barcode_editTextBarcode.setText("")
                                Toast.makeText(this@AddBarcodeActivity,
                                    "Updated",
                                    Toast.LENGTH_SHORT).show()
                                startActivityForResult(mIntent, 123)
                            }
                        } else {
                            Log.d("myBarcode", response.message())
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("myBarcode", t.localizedMessage)
                    }
                })
            }
        }

        activity_add_barcode_scanAgain.setOnClickListener {
            /*activity_add_barcode_scan_card.setCardBackgroundColor(Color.TRANSPARENT)
            activity_add_barcode_add_barcode_card.setCardBackgroundColor(Color.parseColor("#f4f4f6"))
            activity_add_barcode_scanAgain.setTextColor(Color.parseColor("#ffeb707a"))
            activity_add_barcode_scanAgain.setBackgroundResource(R.drawable.white_button_bg)
            activity_add_barcode_addBarcode.setTextColor(Color.WHITE)
            activity_add_barcode_addBarcode.setBackgroundResource(R.drawable.button_bg)*/
            activity_add_barcode_editTextBarcode.setText("")
            activity_add_barcode_editTextName.setText("")
            startActivityForResult(mIntent, 123)
        }
    }

    private fun getProductDetailsIfExists() {
        val getProductService = retrofit.create(ApiBarcode::class.java)
        getProductService.getProduct(mBarcode).enqueue(object : Callback<Product>{
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if(response.isSuccessful) {
                    if(response.body()!!.productBarcode != "-1") {
                        activity_add_barcode_addBarcode.text = "Update"
                        activity_add_barcode_button.text = "Update My Product"
                        activity_add_barcode_editTextName.setText(response.body()!!.productName)
                    } else {
                        activity_add_barcode_addBarcode.text = "Add Product"
                        activity_add_barcode_button.text = "Add My Product"
                    }
                } else {
                    Log.d("myBarcode", response.message())
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d("myBarcode", t.localizedMessage)
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
            mBarcode = data.getStringExtra("barcode")

            if(mBarcode == "no result") {
                finish()
            } else {
                activity_add_barcode_editTextBarcode.setText(mBarcode)
                getProductDetailsIfExists()
            }
        }
    }
}