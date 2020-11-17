package com.android.collegeproject.helper

import android.content.Context
import android.util.Log

class Constants {
    val USER_INFO = "USER_INFO"
    val USER_ISFIRSTLOGIN = "USER_ISFIRSTLOGIN"

    fun speak(text: String, mTextToSpeechHelper: TextToSpeechHelper) {
        mTextToSpeechHelper.speakEnglish(text)
    }
}