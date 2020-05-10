package ru.digipeople.locotech.core.ui.widget

import android.text.Editable
import android.text.TextWatcher

/**
 * Абстрактный класс реализующий [TextWatcher]
 *
 * @author Aleksandr Brazhkin
 */
abstract class SimpleTextWatcher : TextWatcher {
    /**
     * после изменения
     */
    override fun afterTextChanged(s: Editable) {

    }
    /**
     * перед изменением
     */
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }
    /**
     * во время изменения
     */
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }
}