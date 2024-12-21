package com.fifahkhirnnsa.dicodingstory.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.fifahkhirnnsa.dicodingstory.R

class CustomEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setupEmailValidation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.email_hint)
        background = ContextCompat.getDrawable(context, R.drawable.custom_text)
    }

    private fun setupEmailValidation() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString().trim()
                error = when {
                    email.isEmpty() -> context.getString(R.string.email_set_empty)
                    " " in email -> context.getString(R.string.email_set_space)
                    !email.contains("@") || !email.contains(".") -> context.getString(R.string.email_set_char)
                    else -> null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
