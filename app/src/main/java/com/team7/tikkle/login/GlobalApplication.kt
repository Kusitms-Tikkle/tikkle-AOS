package com.team7.tikkle.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.team7.tikkle.data.PreferenceUtil

class GlobalApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate() {
        GlobalApplication.prefs = PreferenceUtil(applicationContext)
        super.onCreate()
        KakaoSdk.init(this, "4f444e8815868b65a842725e9610b1d0")
    }
}