package com.fifahkhirnnsa.dicodingstory.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.fifahkhirnnsa.dicodingstory.R

class CustomName @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.name_hint)
        background = ContextCompat.getDrawable(context, R.drawable.custom_text)
    }

}
