package com.helpfulapps.base.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    abstract fun showMessage(text: String)
}