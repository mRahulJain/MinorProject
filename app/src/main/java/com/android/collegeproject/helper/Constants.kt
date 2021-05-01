package com.android.collegeproject.helper

class Constants {
    val USER_INFO = "USER_INFO"
    val USER_ISFIRSTLOGIN = "USER_ISFIRSTLOGIN"
    val IP_HOST = "192.168.1.7"
    val EMERGENCY = "EMERGENCY"
    val EMERGENCY_NAMES = "EMERGENCY_NAMES"

    fun speak(text: String, mTextToSpeechHelper: TextToSpeechHelper) {
        mTextToSpeechHelper.speakEnglish(text)
    }
}