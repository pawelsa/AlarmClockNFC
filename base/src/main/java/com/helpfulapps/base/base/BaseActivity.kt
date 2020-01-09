package com.helpfulapps.base.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.helpfulapps.base.R

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
        window.navigationBarColor = ContextCompat.getColor(this, R.color.background)
        init()
    }

    abstract fun showMessage(text: String)
    abstract fun init()
}