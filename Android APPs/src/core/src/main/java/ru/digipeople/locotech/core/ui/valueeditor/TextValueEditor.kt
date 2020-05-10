package ru.digipeople.locotech.core.ui.valueeditor

import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import ru.digipeople.locotech.core.ui.widget.SimpleTextWatcher

/**
 * Редактор значения для текста
 *
 * @author Aleksandr Brazhkin
 */
class TextValueEditor(private val textView: TextView) : ValueEditor<String> {
    private var _value: String? = null

    override var value: String?
        get() = _value
        set(value) {
            _value = value
            textView.text = format(value)
        }
    /**
     * обработчик измекнения значения
     */
    override var onValueChangedListener: ((value: String?) -> Unit)? = null

    override var format: ((value: String?) -> String) = { it ?: "" }

    init {
        textView.imeOptions = EditorInfo.IME_ACTION_DONE
        textView.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                val str = s.toString()
                if (_value != str) {
                    _value = str
                    onValueChangedListener?.invoke(str)
                }
            }
        })
    }
}