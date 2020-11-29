package com.android.collegeproject.helper

class Constants {
    val USER_INFO = "USER_INFO"
    val USER_ISFIRSTLOGIN = "USER_ISFIRSTLOGIN"
    val IMAGE_HOST = "<YOUR IP ADDRESS>"

    fun speak(text: String, mTextToSpeechHelper: TextToSpeechHelper) {
        mTextToSpeechHelper.speakEnglish(text)
    }
}