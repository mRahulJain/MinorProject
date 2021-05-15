package com.android.collegeproject.api

import com.android.collegeproject.model.Bookings
import com.android.collegeproject.model.CabMessage
import com.android.collegeproject.model.Cabs
import retrofit2.Call
import retrofit2.http.*

interface BookCabAPI {

    @POST("/book-cab")
    @FormUrlEncoded
    fun postBooking(
        @Field("lat") lat: Double,
        @Field("long") long: Double,
        @Field("destination") destination: String
    ) : Call<Cabs>

    @DELETE("/delete-booking")
    fun deleteBooking(
        @Query("driverId") driverId: String
    ) : Call<CabMessage>

    @PATCH("/update-status")
    fun updateStatus(
        @Query("driverId") driverId: String,
        @Query("status") status: String
    ) : Call<CabMessage>

    @GET("/get-status")
    fun getStatus(
        @Query("driverId") driverId: String
    ) : Call<Bookings>

    @GET("/get-cabs")
    fun getCabDetails(
        @Query("_id") _id: String
    ) : Call<Cabs>

}