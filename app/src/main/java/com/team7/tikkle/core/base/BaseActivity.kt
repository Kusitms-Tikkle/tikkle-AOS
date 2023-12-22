package com.team7.tikkle.core.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.team7.tikkle.R

abstract class BaseActivity<T : ViewDataBinding>(private val resId: Int) : FragmentActivity() {
    var binding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Tikkle)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, resId)
        binding?.lifecycleOwner = this

        setup()
    }

    abstract fun setup()
}
