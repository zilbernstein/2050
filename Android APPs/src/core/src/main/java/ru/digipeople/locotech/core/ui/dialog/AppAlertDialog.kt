package ru.digipeople.locotech.core.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import ru.digipeople.locotech.core.R

/**
 * AlertDialog для приложения
 */
class AppAlertDialog(context: Context) : AlertDialog(context) {
    //region Other
    var title: String = ""
    var message: String = ""
    var onOkBtnClickListener: (() -> Unit)? = null
    var onCancelBtnClickListener: (() -> Unit)? = null
    //endregion
    /**
     * Действия при создании диалога
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_app_alert)

        findViewById<TextView>(R.id.title).setText(title)
        findViewById<TextView>(R.id.message).setText(message)

        /**
         * обработка нажатия ок
         */
        findViewById<View>(R.id.ok_button).setOnClickListener {
            onOkBtnClickListener?.invoke()
            dismiss()
        }
        /**
         * обработка нажатия cancel
         */
        findViewById<View>(R.id.cancel_button).setOnClickListener {
            onCancelBtnClickListener?.invoke()
            dismiss()
        }
    }
}