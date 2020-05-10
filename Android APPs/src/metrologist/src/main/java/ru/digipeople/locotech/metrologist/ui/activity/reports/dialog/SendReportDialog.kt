package ru.digipeople.locotech.metrologist.ui.activity.reports.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import ru.digipeople.locotech.metrologist.R
/**
 * Класс всплывающее окно отправки отчета
 */
class SendReportDialog(context: Context) : AlertDialog(context) {

    private lateinit var emailAddress: EditText
    /**
     * Обработка нажатия на кнопку отправить
     */
    var onSendBtnClicked: ((emailAddress: String) -> Unit)? = null
    /**
     * Действия при создании диалогового окна
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_send_report)
        emailAddress = findViewById(R.id.email_value)
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        findViewById<View>(R.id.cancel_button).setOnClickListener {
            dismiss()
        }

        findViewById<View>(R.id.send_button).setOnClickListener {
            onSendBtnClicked?.invoke(emailAddress.text.toString())
            dismiss()
        }
    }
}