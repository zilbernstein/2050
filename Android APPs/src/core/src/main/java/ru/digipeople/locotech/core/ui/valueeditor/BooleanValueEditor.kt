package ru.digipeople.locotech.core.ui.valueeditor

import android.text.InputType
import android.view.View
import android.widget.TextView
import ru.digipeople.locotech.core.R
import ru.digipeople.locotech.core.ui.dialog.AppBooleanSelectDialog
import ru.digipeople.utils.input.Keyboard

/**
 * Редактор значений для Boolean
 *
 * @author Aleksandr Brazhkin
 */
class BooleanValueEditor(private val textView: TextView) : ValueEditor<Boolean> {
    var positiveValue: String
    var negativeValue: String

    override var value: Boolean? = null
        set(value) {
            field = value
            textView.text = format(value)
        }
    /**
     * обработчик измекнения значения
     */
    override var onValueChangedListener: ((value: Boolean?) -> Unit)? = null

    init {
        val context = textView.context
        positiveValue = context.getString(R.string.boolean_editor_positive)
        negativeValue = context.getString(R.string.boolean_editor_negative)

        textView.isCursorVisible = false
        textView.showSoftInputOnFocus = false
        textView.inputType = InputType.TYPE_NULL
        textView.setOnFocusChangeListener { v: View, hasFocus: Boolean ->
            if (hasFocus) {
                showDialog()
            }
        }
        /**
         * обработчик нажатия на текст
         */
        textView.setOnClickListener {
            showDialog()
        }
    }

    override var format: ((value: Boolean?) -> String) = { value ->
        when (value) {
            true -> positiveValue
            false -> negativeValue
            else -> ""
        }
    }
    /**
     * отображение диалога
     */
    private fun showDialog() {
        Keyboard.hide(textView)

        AppBooleanSelectDialog(textView.context)
                .apply {
                    title = ""
                    value = this@BooleanValueEditor.value
                    onOkBtnClickListener = {
                        if (this@BooleanValueEditor.value != it) {
                            this@BooleanValueEditor.value = it
                            onValueChangedListener?.invoke(it)
                        }
                    }
                }
                .show()
    }
}