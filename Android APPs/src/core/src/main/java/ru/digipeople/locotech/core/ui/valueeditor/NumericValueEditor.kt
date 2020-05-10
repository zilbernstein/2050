package ru.digipeople.locotech.core.ui.valueeditor

import android.text.Editable
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import ru.digipeople.locotech.core.ui.widget.SimpleTextWatcher

/**
 * Цифровой редактор значений
 *
 * @author Aleksandr Brazhkin
 */
class NumericValueEditor(private val textView: TextView) : ValueEditor<Float> {

    private var _value: Float? = null

    override var value: Float?
        get() = _value
        set(value) {
            _value = value
            textView.text = format(value)
        }

    /**
     * обработчик измекнения значения
     */
    override var onValueChangedListener: ((value: Float?) -> Unit)? = null
    override var format: ((value: Float?) -> String) = { it?.toString() ?: "" }

    init {
        textView.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        textView.imeOptions = EditorInfo.IME_ACTION_DONE
        textView.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                val number = s.toString().toFloatOrNull()
                if (_value != number) {
                    _value = number
                    onValueChangedListener?.invoke(number)
                }
            }
        })
    }
}