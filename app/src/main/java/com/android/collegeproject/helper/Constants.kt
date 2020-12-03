package com.android.collegeproject.helper

class Constants {
    val USER_INFO = "USER_INFO"
    val USER_ISFIRSTLOGIN = "USER_ISFIRSTLOGIN"
    val IP_HOST = "192.168.1.12"

    fun speak(text: String, mTextToSpeechHelper: TextToSpeechHelper) {
        mTextToSpeechHelper.speakEnglish(text)
    }
}