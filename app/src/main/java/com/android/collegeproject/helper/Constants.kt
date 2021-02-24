package com.android.collegeproject.helper

class Constants {
    val USER_INFO = "USER_INFO"
    val USER_ISFIRSTLOGIN = "USER_ISFIRSTLOGIN"
    val IP_HOST = "192.168.29.193"

    fun speak(text: String, mTextToSpeechHelper: TextToSpeechHelper) {
        mTextToSpeechHelper.speakEnglish(text)
    }

    fun capitalizeFirstCharacter(text: String) : String {
        val charArray: CharArray = text.toCharArray()
        var foundSpace = true

        for (i in charArray.indices) {
            if (Character.isLetter(charArray[i])) {
                if (foundSpace) {
                    charArray[i] = Character.toUpperCase(charArray[i])
                    foundSpace = false
                }
            } else {
                foundSpace = true
            }
        }

        return String(charArray)
    }
}