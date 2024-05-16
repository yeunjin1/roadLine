package com.lgcns.crossdev.onboarding1.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int) : AppCompatActivity() {
    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
        binding = DataBindingUtil.setContentView(this, layoutResourceId)
        binding.lifecycleOwner = this
        initView()
        setObserve()
    }

    open fun loadData() {}
    open fun initView() {}
    open fun setObserve() {}
}