package com.team7.tikkle

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.team7.tikkle.data.PreferenceUtil

class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
        KakaoSdk.init(this, Constants.KAKAO_API_KEY)
    }
}
