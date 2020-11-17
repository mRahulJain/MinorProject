package com.android.collegeproject.activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.http.ServiceCallback;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.cloud.sdk.core.service.exception.RequestTooLargeException;
import com.ibm.cloud.sdk.core.service.exception.ServiceResponseException;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

public class TranslationActivity extends AppCompatActivity {
//<<<<<<< Updated upstream
//    public String translationBaseUrl = "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/88c05332-32ad-4eb8-8a3a-9d221b245ff5";
//    public String translationApiKey = "-OTkK4HEOf4hJAFFjXBrmf-I9IbrRvLIiavuWoe5a1B1";
//=======
//    public String baseUrl = "https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/88c05332-32ad-4eb8-8a3a-9d221b245ff5";
//    public String apiKey = "-OTkK4HEOf4hJAFFjXBrmf-I9IbrRvLIiavuWoe5a1B1";
//>>>>>>> Stashed changes
//    public String version = "2018-05-01";
//    public IamAuthenticator authenticator;
//    public LanguageTranslator languageTranslator;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //setContentView();
//        initView();
//        getLanguages(languageTranslator);
//        getTranslation(languageTranslator,"hello! how are you?","en","hi");
//
//    }
//
//    private  void initView(){
//        try {
//<<<<<<< Updated upstream
//            authenticator = new IamAuthenticator(translationApiKey);
//            languageTranslator = new LanguageTranslator(version,  authenticator);
//            languageTranslator.setServiceUrl(translationBaseUrl);
//=======
//            authenticator = new IamAuthenticator(apiKey);
//            languageTranslator = new LanguageTranslator(version,  authenticator);
//            languageTranslator.setServiceUrl(baseUrl);
//>>>>>>> Stashed changes
//        } catch (NotFoundException e) {
//            // Handle Not Found (404) exception
//        } catch (RequestTooLargeException e) {
//            // Handle Request Too Large (413) exception
//        } catch (ServiceResponseException e) {
//            // Base class for all exceptions caused by error responses from the service
//            System.out.println("Service returned status code "+ e.getStatusCode() + ": " + e.getMessage());
//        }
//    }
//
//    private void getLanguages(LanguageTranslator languageTranslator) {
//        //BasicAuthenticator authenticator = new BasicAuthenticator("AKSHAT BISHT", "!IBMizgr8");
//        languageTranslator.listIdentifiableLanguages().enqueue(new ServiceCallback<IdentifiableLanguages>() {
//
//            public void onResponse(Response<IdentifiableLanguages> arg0) {
//
//                System.out.println("success"+arg0.getResult());
//            }
//
//            public void onFailure(Exception arg0) {
//                System.out.println("failed"+arg0.toString());
//            }
//        });
//    }
//     private void getTranslation(LanguageTranslator languageTranslator,String text, String source, String target) {
//
//        TranslateOptions translateOptions = new TranslateOptions.Builder().addText(text).modelId(source+"-"+target).build();
//        languageTranslator.translate(translateOptions).enqueue(new ServiceCallback<TranslationResult>(){
//
//            public void onFailure(Exception arg0) {
//                System.out.println(arg0.getMessage());
//            }
//
//            public void onResponse(Response<TranslationResult> arg0) {
//                System.out.println(arg0.getResult());
//            }
//        });
//
//
//    }

}
