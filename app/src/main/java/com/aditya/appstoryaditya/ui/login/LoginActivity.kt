package com.aditya.appstoryaditya.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.aditya.appstoryaditya.R
import com.aditya.appstoryaditya.databinding.ActivityLoginBinding
import com.aditya.appstoryaditya.models.LoginRequest
import com.aditya.appstoryaditya.ui.main.MainActivity
import com.aditya.appstoryaditya.ui.register.RegisterActivity
import com.aditya.appstoryaditya.util.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private var isError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {

            tvRegister.setOnClickListener {
                Intent(this@LoginActivity, RegisterActivity::class.java).also {
                    startActivity(it)
                }
            }

            setInputEmail()
            setInputPassword()

            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val loginRequest = LoginRequest(email, password)

                if (!isError) {
                    loginUser(loginRequest)
                }
            }


            imageView.contentDescription =
                getString(R.string.image_description, getString(R.string.login))

        }

        playAnimation()

    }

    private fun intentToMain() {
        Intent(this@LoginActivity, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
        }
    }

    private fun loginUser(loginRequest: LoginRequest) {
        viewModel.loginUser(loginRequest).observe(this@LoginActivity) { result ->
            binding.apply {
                when (result) {

                    is Resource.Loading -> {
                        progressBar.visibility = VISIBLE
                    }

                    is Resource.Success -> {
                        progressBar.visibility = GONE
                        intentToMain()
                    }

                    is Resource.Error -> {
                        progressBar.visibility = GONE
                        btnLogin.isEnabled
                    }
                }
            }

        }
    }

    private fun playAnimation() {
        binding.apply {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.imageView,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1.0f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1.0f)
            ).apply {
                duration = 1500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()


            val login = ObjectAnimator.ofFloat(textView, View.ALPHA, 1f).setDuration(500)
            val emailLable = ObjectAnimator.ofFloat(textView2, View.ALPHA, 1f).setDuration(200)
            val etEmail = ObjectAnimator.ofFloat(ilEmail, View.ALPHA, 1f).setDuration(200)
            val passwordLable = ObjectAnimator.ofFloat(textView3, View.ALPHA, 1f).setDuration(200)
            val etPassword = ObjectAnimator.ofFloat(ilPassword, View.ALPHA, 1f).setDuration(200)
            val btnLogin = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(200)
            val dontHaveAccount = ObjectAnimator.ofFloat(textView4, View.ALPHA, 1f).setDuration(200)
            val registerLabel = ObjectAnimator.ofFloat(tvRegister, View.ALPHA, 1f).setDuration(200)

            AnimatorSet().apply {
                playSequentially(
                    login,
                    emailLable,
                    etEmail,
                    passwordLable,
                    etPassword,
                    btnLogin,
                    dontHaveAccount,
                    registerLabel
                )
                start()
            }
        }
    }

    private fun setInputPassword() {
        binding.apply {
            etPassword.onValidateInput(
                activity = this@LoginActivity,
                hideErrorMessage = {
                    ilPassword.isErrorEnabled = false
                    isError = false
                },
                setErrorMessage = {
                    ilPassword.error = it
                    ilPassword.isErrorEnabled = true
                    isError = true
                }
            )
        }
    }

    private fun setInputEmail() {
        binding.apply {
            etEmail.onValidateInput(
                activity = this@LoginActivity,
                hideErrorMessage = {
                    ilEmail.isErrorEnabled = false
                    isError = false
                },
                setErrorMessage = {
                    ilEmail.error = it
                    ilEmail.isErrorEnabled = true
                    isError = true
                }
            )
        }
    }
}

