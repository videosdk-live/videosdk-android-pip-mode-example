package live.videosdk.rtc.android.quickstart

import android.app.Application
import live.videosdk.rtc.android.VideoSDK

class MainApplication : Application() {
    val sampleToken = "Your VideoSDK Token"

    override fun onCreate() {
        super.onCreate()
        VideoSDK.initialize(applicationContext)
    }
}