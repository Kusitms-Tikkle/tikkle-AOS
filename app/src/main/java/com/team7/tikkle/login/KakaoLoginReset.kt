package com.team7.tikkle.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoLoginReset : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화 - 네이티브 앱 키
        KakaoSdk.init(this, "kakao4f444e8815868b65a842725e9610b1d0")
    }
}