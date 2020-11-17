package com.android.collegeproject.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.collegeproject.R;
import com.android.collegeproject.adapter.NewsAdapter;
import com.android.collegeproject.api.ApiNewsMethods;
import com.android.collegeproject.helper.Constants;
import com.android.collegeproject.helper.TextToSpeechHelper;
import com.android.collegeproject.model.NewsModelClass;
import com.android.collegeproject.model.Sources;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {

    private TextToSpeechHelper mTextToSpeechHelper;
    private Constants constants;

    public NewsModelClass newsModelBody;
    Sources sources;
    String newsBaseUrl = "https://newsapi.org";
    String newsApiKey = "6fb753a97d764c93b02631fd32e80df5";

    FrameLayout mLoader;
    LinearLayout mMainScreen;
    RecyclerView mRecyclerView;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(newsBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mTextToSpeechHelper = new TextToSpeechHelper(this);
        constants = new Constants();

        initView();

        //loading note
        Handler initHandler = new Handler();
        Runnable initRunnable = new Runnable() {
            @Override
            public void run() {
                constants.speak("Hold On! Fetching top headlines", mTextToSpeechHelper);
            }
        };
        initHandler.postDelayed(initRunnable, 500);

        fetchTopHeadlines();
        //text to speech command for 'Please tell us what you wanna hear for: 1) top headlines 2) everything or 3) the Sources'

    }

    private  void initView(){
        mLoader = findViewById(R.id.activity_news_loader);
        mMainScreen = findViewById(R.id.activity_news_mainScreen);
        mRecyclerView = findViewById(R.id.activity_news_recyclerView);
    }

    private void newsUIChanges() {
        mLoader.setVisibility(View.GONE);
        mMainScreen.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new NewsAdapter(this, newsModelBody));
    }

    private void newsSourceUIChanges() {

    }

    private void fetchTopHeadlines() {
        //using country
        //input voice command for 'Sow top headlines of ? 1) india 2) USA 3)......'
        ApiNewsMethods newsTopHeadlines = retrofit.create(ApiNewsMethods.class);
        newsTopHeadlines.getTopHeadlines("in"/*country through voice command*/, newsApiKey).enqueue(new Callback<NewsModelClass>() {
            @Override
            public void onResponse(Call<NewsModelClass> call, Response<NewsModelClass> response) {
                if(response.isSuccessful()) {
                    newsModelBody = response.body();

                    //change UI
                    newsUIChanges();

                    //Prepare speech

                } else {
                    Log.d("mySTRING", "DID NOT OCCUR");
                }
            }

            @Override
            public void onFailure(Call<NewsModelClass> call, Throwable t) {
            }
        });
    }

    private void fetchEverything() {
        //using topic
        //voice commnd for 'Please tell us your topic  of interest ?'
        ApiNewsMethods newsEverything = retrofit.create(ApiNewsMethods.class);
        newsEverything.getEverything("bitcoin"/*topic input by user*/, newsApiKey).enqueue(new Callback<NewsModelClass>() {
            @Override
            public void onResponse(Call<NewsModelClass> call, Response<NewsModelClass> response) {
                if(response.isSuccessful()) {
                    newsModelBody = response.body();
                    Log.d("myBODY", newsModelBody.getStatus());
                    //text to speech object of the response fetched
                    newsUIChanges();
                } else {
                    Log.d("mySTRING", "DID NOT OCCUR");
                }
            }

            @Override
            public void onFailure(Call<NewsModelClass> call, Throwable t) {
            }

        });
    }

    private void fetchSources() {
        ApiNewsMethods newsSources = retrofit.create(ApiNewsMethods.class);
        newsSources.getSources(newsApiKey).enqueue(new Callback<Sources>() {
            @Override
            public void onResponse(Call<Sources> call, Response<Sources> response) {
                if(response.isSuccessful()) {
                    sources = response.body();
                    Log.d("myBODY", sources.getStatus());
                    //text to speech object of the response fetched
                    newsSourceUIChanges();
                } else {
                    Log.d("mySTRING", "DID NOT OCCUR");
                }
            }
            @Override
            public void onFailure(Call<Sources> call, Throwable t) {

            }
        });
    }
}
