package ru.digipeople.locotech.core.ui.valueeditor

import android.text.InputType
import android.view.View
import android.widget.TextView
import ru.digipeople.locotech.core.ui.dialog.DateTimePickerDialog
import ru.digipeople.utils.input.Keyboard
import java.util.*

/**
 * Редактор значения для даты/времени
 *
 * @author Aleksandr Brazhkin
 */
class DateTimeValueEditor(private val textView: TextView) : ValueEditor<Date> {

    private val calendar = Calendar.getInstance()

    override var value: Date? = null
        set(value) {
            field = value
            textView.text = format(value)
        }
    /**
     * обработчик измекнения значения
     */
    override var onValueChangedListener: ((value: Date?) -> Unit)? = null

    override var format: ((value: Date?) -> String) = { it?.toString() ?: "" }

    init {
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
    /**
     * отображение диалога времени и даты
     */
    private fun showDialog() {
        Keyboard.hide(textView)

        value?.let { calendar.time = it }

        DateTimePickerDialog(calendar)
                .onDateTimeSet { calendar ->
                    val dateTime = calendar.time
                    if (value != dateTime) {
                        value = dateTime
                        onValueChangedListener?.invoke(dateTime)
                    }
                }
                .show(textView.context)
    }
}