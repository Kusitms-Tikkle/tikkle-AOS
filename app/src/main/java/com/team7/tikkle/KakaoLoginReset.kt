package com.team7.tikkle

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoLoginReset : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화 - 네이티브 앱 키
        KakaoSdk.init(this, "{KAKAO_NATIVE_APP_KEY}")
    }
}