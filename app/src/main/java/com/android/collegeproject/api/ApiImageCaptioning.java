package com.android.collegeproject.api;

import com.android.collegeproject.model.Description;
import com.android.collegeproject.model.PostBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiImageCaptioning {

    @POST("/predict")
    Call<Description> getDescription(
            @Body PostBody array
    );
}
