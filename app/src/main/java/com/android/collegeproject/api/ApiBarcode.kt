package com.android.collegeproject.api

import com.android.collegeproject.model.Product
import retrofit2.Call
import retrofit2.http.*

interface ApiBarcode {

    @POST("/add-product")
    @FormUrlEncoded
    fun addProduct(
        @Field("productBarcode") productBarcode: String,
        @Field("productName") productName: String
    ) : Call<String>


    @GET("/get-product")
    fun getProduct(
        @Query("productBarcode") productBarcode: String
    ) : Call<Product>
}