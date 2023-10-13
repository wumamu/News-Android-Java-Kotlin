package com.recoveryrecord.surveyandroid.example.di

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.recoveryrecord.surveyandroid.example.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.Forest.plant


@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            plant(Timber.DebugTree())
            val client = AndroidFlipperClient.getInstance(this)
            client.apply {
                addPlugin(
                    SharedPreferencesFlipperPlugin(
                        this@BaseApplication,
                        this@BaseApplication.packageName + "_preferences"
                    )
                )
//                addPlugin(
//                    SharedPreferencesFlipperPlugin(
//                        this@BaseApplication,
//                        "token"
//                    )
//                )
                addPlugin(
                    InspectorFlipperPlugin(
                        this@BaseApplication,
                        DescriptorMapping.withDefaults()
                    )
                )
            }
            client.start()
        }
    }
}