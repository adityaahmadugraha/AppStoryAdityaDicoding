package com.aditya.appstoryaditya.costumview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.aditya.appstoryaditya.R


class Email : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private val errorMessage = MutableLiveData<String>()
    private val hideError = MutableLiveData<Boolean>()
    private var errorIcon: Drawable? = null
    private var errorIconMarginEnd: Int = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.masukan_email)
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_error)
        errorIcon?.setBounds(0, 0, errorIcon?.intrinsicWidth ?: 0, errorIcon?.intrinsicHeight ?: 0)

        errorIconMarginEnd = resources.getDimensionPixelSize(R.dimen.error_icon_margin_end)

        doAfterTextChanged { text ->
            if (text?.isNotEmpty() == true) {
                val parts = text.split("@")
                if (parts.size == 2) {
                    val domain = parts[1]
                    if (domain.endsWith(".com", ignoreCase = true)) {
                        hideErrorMessage()
                    } else {
                        setErrorMessage(context.getString(R.string.invalid_domain))
                    }
                } else {
                    setErrorMessage(context.getString(R.string.invalid_email_format))
                }
            } else {
                setErrorMessage(context.getString(R.string.tidak_boleh_kosong))
            }
        }
    }

    private fun hideErrorMessage() {
        hideError.value = true
        animateErrorIcon(false)
    }

    private fun setErrorMessage(message: String) {
        errorMessage.value = message
        animateErrorIcon(true)
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun animateErrorIcon(show: Boolean) {
        val iconAlpha = if (show) 1f else 0f
        val translationX = if (show) 0f else errorIconMarginEnd.toFloat()

        val alphaAnimator = ObjectAnimator.ofFloat(errorIcon, "alpha", iconAlpha).apply {
            duration = 200
        }

        val translationAnimator =
            ObjectAnimator.ofFloat(errorIcon, "translationX", translationX).apply {
                duration = 200
            }

        val animatorSet = AnimatorSet().apply {
            playTogether(alphaAnimator, translationAnimator)
            interpolator = LinearInterpolator()
        }

        animatorSet.start()

        errorIcon?.let {
            if (!show) {
                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
                    }
                })
            } else {

                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, it, null)
            }
        }
    }
    
    fun onValidateInput(
        activity: LifecycleOwner,
        hideErrorMessage: () -> Unit,
        setErrorMessage: (message: String) -> Unit
    ) {
        hideError.observe(activity) { hideErrorMessage() }
        errorMessage.observe(activity) { setErrorMessage(it) }
    }
}



