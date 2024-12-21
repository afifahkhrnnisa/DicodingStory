package com.fifahkhirnnsa.dicodingstory.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.fifahkhirnnsa.dicodingstory.R

class CustomPassword @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setupPasswordValidation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = ContextCompat.getString(context, R.string.password_hint)
        background = ContextCompat.getDrawable(context, R.drawable.custom_text)
    }

    private fun setupPasswordValidation() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    setError(context.getString(R.string.password_set), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}