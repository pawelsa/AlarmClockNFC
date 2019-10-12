package com.helpfulapps.base.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    val TAG: String = this::class.java.simpleName
    abstract val layoutId: Int
    abstract val viewModel: VM
    lateinit var binding: DB
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        init()
    }

    abstract fun showMessage(text: String)
    abstract fun init()
}