package com.aditya.appstoryaditya.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
                ilName.error = getString(R.string.tidak_boleh_kosong)
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



            val rotateDuration = 500L
            val fadeInDuration = 150L


            val login = ObjectAnimator.ofFloat(textView, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val nameLabelFadeIn = ObjectAnimator.ofFloat(textView5, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val etNameFadeIn = ObjectAnimator.ofFloat(ilName, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val emailLabelFadeIn = ObjectAnimator.ofFloat(textView2, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val etEmailFadeIn = ObjectAnimator.ofFloat(ilEmail, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val passwordLabelFadeIn = ObjectAnimator.ofFloat(textView3, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val etPasswordFadeIn = ObjectAnimator.ofFloat(ilPassword, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val btnRegisterFadeIn = ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val dontHaveAccountFadeIn = ObjectAnimator.ofFloat(textView4, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)
            val registerLabelFadeIn = ObjectAnimator.ofFloat(tvLogin, View.ALPHA, 0f, 1f).setDuration(fadeInDuration)

            val rotateAnimator = ObjectAnimator.ofFloat(btnRegister, View.ROTATION, 0f, 360f).setDuration(rotateDuration)

            val linearInterpolator = LinearInterpolator()
            login.interpolator = linearInterpolator
            nameLabelFadeIn.interpolator = linearInterpolator
            etNameFadeIn.interpolator = linearInterpolator
            emailLabelFadeIn.interpolator = linearInterpolator
            etEmailFadeIn.interpolator = linearInterpolator
            passwordLabelFadeIn.interpolator = linearInterpolator
            etPasswordFadeIn.interpolator = linearInterpolator
            dontHaveAccountFadeIn.interpolator = linearInterpolator
            registerLabelFadeIn.interpolator = linearInterpolator


            val animatorSet = AnimatorSet().apply {
                playTogether(
                    login,
                    nameLabelFadeIn,
                    etNameFadeIn,
                    emailLabelFadeIn,
                    etEmailFadeIn,
                    passwordLabelFadeIn,
                    etPasswordFadeIn,
                    btnRegisterFadeIn,
                    dontHaveAccountFadeIn,
                    registerLabelFadeIn,
                    rotateAnimator
                )
            }

            animatorSet.start()


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
