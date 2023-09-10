package com.aditya.appstoryaditya.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aditya.appstoryaditya.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}