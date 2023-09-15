package com.aditya.appstoryaditya.costumview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.util.Patterns
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.enter_email)
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_error)
        errorIcon?.setBounds(0, 0, errorIcon?.intrinsicWidth ?: 0, errorIcon?.intrinsicHeight ?: 0)

        doAfterTextChanged { text ->
            if (text?.isEmpty() == true) {
                setErrorMessage(context.getString(R.string.must_not_empty))
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    setErrorMessage(context.getString(R.string.not_type_email))
                } else {
                    hideErrorMessage()
                }
            }
        }
    }

    private fun hideErrorMessage() {
        hideError.value = true
        setCompoundDrawablesRelative(null, null, null, null)
    }

    private fun setErrorMessage(message: String) {
        errorMessage.value = message
        setCompoundDrawablesRelative(null, null, errorIcon, null)
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