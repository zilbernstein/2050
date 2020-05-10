package ru.digipeople.locotech.inspector.ui.activity.print.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import ru.digipeople.locotech.inspector.R

/**
 * Диалоговое окно ввода email
 *
 * @author Kashonkov Nikita
 */
class EnterEmailDialog(context: Context, val mail: String) : Dialog(context) {
    //region Views
    private lateinit var sendBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var email: EditText
    private lateinit var clearEmail: ImageView
    //endregion
    //region Other
    var sendListener: ((email: String) -> Unit)? = null
    //endregion
    /**
     * действия при создании диалогового окна
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.enter_email_dialog)

        email = findViewById(R.id.message)
        email.setText(mail)
        email.setSelection(mail.length)
        email.addTextChangedListener(textWatcher)

        sendBtn = findViewById(R.id.send)
        sendBtn.setOnClickListener {
            sendListener?.invoke(email.text.toString())
            dismiss()
        }

        cancelBtn = findViewById(R.id.cancel)
        cancelBtn.setOnClickListener { dismiss() }

        clearEmail = findViewById(R.id.clear_email)
        clearEmail.setOnClickListener { email.setText("") }
    }
    /**
     *обработка изменения текста
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            sendBtn.isEnabled = !s.isNullOrEmpty()
        }
    }

}