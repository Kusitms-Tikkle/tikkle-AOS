package com.team7.tikkle.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.team7.tikkle.Constants.KAKAO_API_KEY

class KakaoLoginReset : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화 - 네이티브 앱 키
        KakaoSdk.init(this, KAKAO_API_KEY)
    }
}
