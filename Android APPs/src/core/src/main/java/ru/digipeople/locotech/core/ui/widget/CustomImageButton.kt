package ru.digipeople.locotech.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import ru.digipeople.locotech.core.R

/**
 * Кастомная кнопка с изображением
 */
class CustomImageButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = R.attr.imageButtonStyle) : AppCompatImageButton(context, attrs, defStyle) {
    override fun setOnClickListener(l: OnClickListener?) {
        if (l == null) {
            super.setOnClickListener(null)
        } else {
            super.setOnClickListener(DelayedClickListener(l))
        }
    }
}
