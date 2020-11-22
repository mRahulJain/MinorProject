package com.android.collegeproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.collegeproject.R;
import com.android.collegeproject.api.ApiWeatherMethods;
import com.android.collegeproject.helper.AndroidPermissions;
import com.android.collegeproject.helper.ClickListener;
import com.android.collegeproject.helper.Constants;
import com.android.collegeproject.helper.LocationHelper;
import com.android.collegeproject.helper.SwipeListener;
import com.android.collegeproject.helper.TextToSpeechHelper;
import com.android.collegeproject.model.WeatherModelClass;
import com.squareup.picasso.Picasso;
import java.util.Calendar;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    private TextToSpeechHelper mTextToSpeechHelper;
    private AndroidPermissions mAndroidPermissions;
    private LocationHelper locationHelper;
    private String weatherDetails = "";
    private Constants constants;
    double latitude,longitude;

    private LinearLayout mMainScreen;
    private FrameLayout mLoader;
    private TextView mTextViewPlace;
    private TextView mTextViewCountryTimeZone;
    private TextView mTextViewMinTemp;
    private TextView mTextViewMaxTemp;
    private TextView mTextViewNormalTemp;
    private TextView mTextViewSky;
    private TextView mTextViewSunRise;
    private TextView mTextViewSunSet;
    private TextView mTextViewWindSpeed;
    private TextView mTextViewHumidity;
    private ImageView mImageViewIcon;
    public WeatherModelClass mBody;

    public String weatherApiKey = "1f4e8778858369c41de84408badb2c1e";
    public String weatherBaseUrl = "https://api.openweathermap.org/data/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(weatherBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //initialize some variables
        mTextToSpeechHelper = new TextToSpeechHelper(this);
        mAndroidPermissions = new AndroidPermissions(this);
        constants = new Constants();

        //loading note
        Handler initHandler = new Handler();
        Runnable initRunnable = new Runnable() {
            @Override
            public void run() {
                constants.speak("Hold On! Fetching the weather details", mTextToSpeechHelper);
            }
        };
        initHandler.postDelayed(initRunnable, 500);

        //getting current location
        //safety check with if-else
        if(mAndroidPermissions.checkPermissionForLocation() && mAndroidPermissions.checkLocationSetting()){
            //initializing the view
            initView();

            //get current location
            locationHelper = new LocationHelper(this, this);

            //making sure that lat long is fetched
            //call myCurrentWeather
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    latitude = locationHelper.getLocation().get(0);
                    longitude = locationHelper.getLocation().get(1);
                    myCurrentWeather(latitude, longitude);
                }
            }, 5000);

            //exit page
            mMainScreen.setOnTouchListener(new SwipeListener(this){
                @Override
                public void onSwipeRight() {
                    mTextToSpeechHelper.destroySpeech();
                    finish();
                }

                @Override
                public void onSwipeLeft() {}
                @Override
                public void onSwipeTop() {}
                @Override
                public void onSwipeBottom() { }
            });
        }
        else {
            finish();
        }

        //input voice command to query weather details according to the place spoken.
        //text to speech object for 'Please tell us the area for which you wanna know the weather forecast ?'
        //voice command to input the city or place and activate weather results and convert it to text
       /* mButtonWeatherByPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPlaceWeather(place);
            }
        });*/
    }

    //initialize views
    private void initView() {
        mMainScreen = findViewById(R.id.activity_weather_mainScreen);
        mLoader = findViewById(R.id.activity_weather_loader);
        mTextViewSky = findViewById(R.id.activity_weather_updates_sky);
        mImageViewIcon = findViewById(R.id.activity_weather_updates_weatherIcon);
        mTextViewPlace = findViewById(R.id.textViewPlace);
        mTextViewCountryTimeZone = findViewById(R.id.textViewCountryTimeZone);
        mTextViewMaxTemp = findViewById(R.id.activity_weather_updates_maxTemp);
        mTextViewMinTemp = findViewById(R.id.activity_weather_updates_minTemp);
        mTextViewNormalTemp = findViewById(R.id.textViewNormalTemp);
        mTextViewHumidity = findViewById(R.id.activity_weather_updates_humidity);
        mTextViewSunRise = findViewById(R.id.activity_weather_updates_sunrise);
        mTextViewSunSet = findViewById(R.id.activity_weather_updates_sunset);
        mTextViewWindSpeed = findViewById(R.id.activity_weather_updates_windSpeed);
    }

    //change UI with the weather result
    private void weatherUIChanges() {
        mTextViewSky.setText(mBody.getWeather().get(0).getMain());
        String base = "https://openweathermap.org/img/wn/"+mBody.getWeather().get(0).getIcon()+"@2x.png";

        weatherDetails = "Details fetched successfully!\nIt’s clear in "+mBody.getName()+", today. " +
                "Current temperature is "+String.format("%.2f",mBody.getMain().getTemp())+" degree Celsius," +
                " with maximum temperature of "+String.format("%.2f",mBody.getMain().getTemp_max())+" " +
                "degree C and minimum temperature of "+String.format("%.2f",mBody.getMain().getTemp_min())+" " +
                "degree Celsius, as recorded.\n\n\n\n Swipe Right to go back";
        constants.speak(weatherDetails, mTextToSpeechHelper);

        mLoader.setVisibility(View.GONE);
        mMainScreen.setVisibility(View.VISIBLE);

        Picasso.with(this).load(base).into(mImageViewIcon);
        mTextViewMaxTemp.setText(String.format("%.2f",mBody.getMain().getTemp_max())+" \u2103");
        mTextViewMinTemp.setText(String.format("%.2f",mBody.getMain().getTemp_min())+" \u2103");
        mTextViewNormalTemp.setText(String.format("%.2f",mBody.getMain().getTemp())+" \u2103");
        mTextViewWindSpeed.setText(String.format("%.2f",mBody.getWind().getSpeed())+" Kmph");
        mTextViewHumidity.setText(mBody.getMain().getHumidity()+"%");

        Calendar calendar = Calendar.getInstance();
        Date date = new Date(mBody.getSys().getSunrise()*1000);
        calendar.setTime(date);
        mTextViewSunRise.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+"AM  ");
        date = new Date(mBody.getSys().getSunset()*1000);
        calendar.setTime(date);
        mTextViewSunSet.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+"PM");
        date = new Date(mBody.getTimezone());
        calendar.setTime(date);
        mTextViewPlace.setText(mBody.getName());
        mTextViewCountryTimeZone.setText(mBody.getSys().getCountry()+" ("+"+"+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+")");
    }

    //get weather using lat long
    private void myCurrentWeather(double lat, double lng) {
        //using lat long
        ApiWeatherMethods weatherByLatLong = retrofit.create(ApiWeatherMethods.class);
        weatherByLatLong.getWeatherByCoordinates(lat,lng, weatherApiKey).enqueue(new Callback<WeatherModelClass>() {
            @Override
            public void onResponse(Call<WeatherModelClass> call, Response<WeatherModelClass> response) {
                if(response.isSuccessful()) {
                    mBody = response.body();
                    Log.d("myBODY", mBody.getWeather().get(0).getMain());
                    //text to speech object of the response fetched
                    weatherUIChanges();
                } else {
                    Log.d("mySTRING", "DID NOT OCCUR");
                }
            }

            @Override
            public void onFailure(Call<WeatherModelClass> call, Throwable t) {
                Log.d("myError", t.getLocalizedMessage());
            }
        });
    }

    //not required as of now
    private void myPlaceWeather(String myPlace) {
        //using place
        ApiWeatherMethods weatherByLatLong = retrofit.create(ApiWeatherMethods.class);
        weatherByLatLong.getWetherByCityName(myPlace, weatherApiKey).enqueue(new Callback<WeatherModelClass>() {
            @Override
            public void onResponse(Call<WeatherModelClass> call, Response<WeatherModelClass> response) {
                if(response.isSuccessful()) {
                    mBody = response.body();
                    Log.d("myBODY", mBody.toString());
                    //text to speech object of the response fetched
                    weatherUIChanges();
                } else {
                    Log.d("mySTRING", "DID NOT OCCUR");
                }
            }

            @Override
            public void onFailure(Call<WeatherModelClass> call, Throwable t) {
                Log.d("myError", t.getLocalizedMessage());
            }
        });
    }

    //destroy speech helper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTextToSpeechHelper.destroySpeech();
    }
}