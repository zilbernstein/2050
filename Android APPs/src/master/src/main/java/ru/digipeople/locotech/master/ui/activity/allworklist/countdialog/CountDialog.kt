package ru.digipeople.locotech.master.ui.activity.allworklist.countdialog

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import ru.digipeople.locotech.master.R
import javax.inject.Inject

/**
 * Диалог задания количества повторов
 *
 * @author Kashonkov Nikita
 */
class CountDialog @Inject constructor() : androidx.fragment.app.DialogFragment() {
    //region Const
    private val MAX_REPEATS = 99
    //end Region
    private lateinit var count: EditText
    private lateinit var cancelButton: Button
    private lateinit var okButton: Button
    var onCountClickListener: ((count: Int) -> Unit)? = null
    var onDismissListener: (() -> Unit)? = null
    /**
     * Функцилнал, выполняющийся при создании экрана
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_count, container, false)

        count = view.findViewById(R.id.count)
        count.requestFocus()
        count.addTextChangedListener(textWatcher)

        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener { dismiss() }

        okButton = view.findViewById(R.id.send)
        /**
         * Действия, по нажатию на кнопку "Ок"
         */
        okButton.setOnClickListener { onCountClickListener?.invoke(count.text.toString().toInt()) }
        return view
    }
    /**
     * Обработчик ввода текста
     */
    private val textWatcher = object : TextWatcher {
        /**
         * Действия после изменения текста
         */
        override fun afterTextChanged(s: Editable?) {
            okButton.isEnabled = !s.isNullOrEmpty()

            s?.let {
                if (s.isNotEmpty() && s.toString().toInt() > MAX_REPEATS) {
                    count.setText(s.substring(0, s.lastIndex))
                    count.setSelection(s.lastIndex)
                }
            }

        }
        /**
         * Действия перед изменением текста
         */
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        /**
         * Действия во время изменения текста
         */
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }
    /**
     * Действия при отмене
     */
    override fun onDismiss(dialog: DialogInterface) {
        onDismissListener?.invoke()
        super.onDismiss(dialog)
    }
}