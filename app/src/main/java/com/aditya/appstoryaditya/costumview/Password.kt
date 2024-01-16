package com.aditya.appstoryaditya.costumview

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.aditya.appstoryaditya.R


class Password : AppCompatEditText {
    private val errorMessage = MutableLiveData<String>()
    private val hideError = MutableLiveData<Boolean>()

    private var showPasswordDrawable: Drawable? = null
    private var hidePasswordDrawable: Drawable? = null
    private var isPasswordVisible = false

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.masukan_password)
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        transformationMethod = PasswordTransformationMethod.getInstance()

        showPasswordDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_lock_hidden)
        hidePasswordDrawable =
            ContextCompat.getDrawable(context, R.drawable.ic_lock_visible)

        setCompoundDrawablesWithIntrinsicBounds(null, null, hidePasswordDrawable, null)

        doAfterTextChanged { text ->
            if (text?.isEmpty() == true) {
                setErrorMessage(context.getString(R.string.tidak_boleh_kosong))
            } else {
                if ((text?.length ?: 0) < 8) {
                    setErrorMessage(context.getString(R.string.harus_lebih_8_karakter))
                } else {
                    hideErrorMessage()
                }
            }
        }

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= right - compoundPaddingEnd) {
                togglePasswordVisibility()
                performClick()
                true
            } else {
                false
            }
        }

    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        transformationMethod =
            if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()

        val drawable = if (isPasswordVisible) hidePasswordDrawable else showPasswordDrawable
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    private fun hideErrorMessage() {
        hideError.value = true
    }

    private fun setErrorMessage(message: String) {
        errorMessage.value = message
    }

    fun onValidateInput(
        activity: Activity,
        hideErrorMessage: () -> Unit,
        setErrorMessage: (message: String) -> Unit
    ) {
        hideError.observe(activity as LifecycleOwner) { hideErrorMessage() }
        errorMessage.observe(activity as LifecycleOwner) { setErrorMessage(it) }
    }
}

