package com.aditya.appstoryaditya.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.aditya.appstoryaditya.R
import com.aditya.appstoryaditya.databinding.ActivityRegisterBinding
import com.aditya.appstoryaditya.models.RegisterRequest
import com.aditya.appstoryaditya.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private var isError = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.apply {
            setInputEmail()
            setInputPassword()

            btnRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (validateInput(name)) {
                    registerUser(RegisterRequest(name, email, password))
                }
            }

            tvLogin.setOnClickListener {
                finish()
            }

            imageView.contentDescription =
                getString(R.string.image_description, getString(R.string.register))
        }

        playAnimation()
    }


private fun registerUser(registerRequest: RegisterRequest) {
    viewModel.registerUser(registerRequest).observe(this@RegisterActivity) { response ->
     binding.apply {
         when (response) {
             is Resource.Loading -> {
                 progressBar.visibility = View.VISIBLE
             }

             is Resource.Success -> {
                 progressBar.visibility = View.GONE
                 Toast.makeText(this@RegisterActivity, response.data.message, Toast.LENGTH_SHORT)
                     .show()

                 finish()
             }

             is Resource.Error -> {
                 progressBar.visibility = View.GONE
                 Toast.makeText(
                     this@RegisterActivity,
                     getString(R.string.terjadi_kesalahan),
                     Toast.LENGTH_SHORT
                 ).show()
             }
         }
     }

    }
}


    private fun validateInput(name: String): Boolean {
        binding.apply {
            if (name.isEmpty()) {
                ilName.isErrorEnabled = true
                ilName.error = getString(R.string.must_not_empty)
                return false
            }
            if (isError) {
                return false
            }
            return true
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

            val login = ObjectAnimator.ofFloat(textView, View.ALPHA, 1f).setDuration(200)
            val nameLable = ObjectAnimator.ofFloat(textView5, View.ALPHA, 1f).setDuration(150)
            val etName = ObjectAnimator.ofFloat(ilName, View.ALPHA, 1f).setDuration(150)
            val emailLable = ObjectAnimator.ofFloat(textView2, View.ALPHA, 1f).setDuration(150)
            val etEmail = ObjectAnimator.ofFloat(ilEmail, View.ALPHA, 1f).setDuration(150)
            val passwordLable = ObjectAnimator.ofFloat(textView3, View.ALPHA, 1f).setDuration(150)
            val etPassword = ObjectAnimator.ofFloat(ilPassword, View.ALPHA, 1f).setDuration(150)
            val btnRegister = ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 1f).setDuration(150)
            val dontHaveAccount = ObjectAnimator.ofFloat(textView4, View.ALPHA, 1f).setDuration(150)
            val registerLabel = ObjectAnimator.ofFloat(tvLogin, View.ALPHA, 1f).setDuration(150)

            AnimatorSet().apply {
                playSequentially(
                    login,
                    nameLable,
                    etName,
                    emailLable,
                    etEmail,
                    passwordLable,
                    etPassword,
                    btnRegister,
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
                activity = this@RegisterActivity,
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
                activity = this@RegisterActivity,
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
