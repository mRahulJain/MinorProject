package com.android.collegeproject.helper

class Constants {
    val USER_INFO = "USER_INFO"
    val USER_ISFIRSTLOGIN = "USER_ISFIRSTLOGIN"
    val IP_HOST = "192.168.29.193"
    val EMERGENCY = "EMERGENCY"
    val EMERGENCY_NAMES = "EMERGENCY_NAMES"

    val CAB = "CAB"
    val CAB_ID = "CAB_ID"
    val CAB_FROM = "CAB_FROM"
    val CAB_TO = "CAB_TO"
    val CAB_VEHICLE = "CAB_VEHICLE"
    val CAB_DRIVER_NAME = "CAB_DRIVER_NAME"
    val CAB_VEHICLE_NUMBER = "CAB_VEHICLE_NUMBER"
    val CAB_VEHICLE_TYPE = "CAB_VEHICLE_TYPE"
    val CAB_CONTACT_NUMBER = "CAB_CONTACT_NUMBER"
    val CAB_FARE = "CAB_FARE"
    val CAB_ESTIMATED_TIME = "CAB_ESTIMATED_TIME"

    val LOCATION = "LOCATION"
    val LATITUDE = "LATITUTDE"
    val LONGITUDE = "LONGITUDE"

    fun speak(text: String, mTextToSpeechHelper: TextToSpeechHelper) {
        mTextToSpeechHelper.speakEnglish(text)
    }
}