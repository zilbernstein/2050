package ru.digipeople.locotech.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

/**
 * Кастомная кнопка
 */
class CustomButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = android.R.attr.buttonStyle) : AppCompatButton(context, attrs, defStyle) {
    override fun setOnClickListener(l: OnClickListener?) {
        if (l == null) {
            super.setOnClickListener(null)
        } else {
            super.setOnClickListener(DelayedClickListener(l))
        }
    }
}