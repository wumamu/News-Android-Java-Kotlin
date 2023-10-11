package com.recoveryrecord.surveyandroid.util

import com.recoveryrecord.surveyandroid.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Constants {
    companion object {
        const val BASE_URL = "https://fcm.googleapis.com/v1/projects/newsmoment-ios/messages:send/"
        const val SERVER_KEY =
            "1093b9007202de66322cfaf2737e539104feb688d6044a6fbc8c4435b69bde5265e6df239cd583de6b981df34a954261faaf45eda0e0e3ba87f34ab5"
        const val CONTENT_TYPE = "application/json"
    }
}

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api: NotificationAPI by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}