package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import ru.digipeople.locotech.master.R

/**
 * Диалог подтверждения списания ТМЦ
 *
 * @author Kashonkov Nikita
 */
class WriteOffTmcDialog(context: Context, val  warningVisible: Boolean) : Dialog(context) {
    //region Views
    private lateinit var sendBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var overrunWarning: TextView
    //endregion
    //region Other
    var acceptListener: (() -> Unit)? = null
    //endregion
    /**
     * Действия при создании диалогового окна
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_write_off)


        sendBtn = findViewById(R.id.send)
        /**
         * Обрабока нажатия на кнопку принять
         */
        sendBtn.setOnClickListener {
            acceptListener?.invoke()
            dismiss()
        }

        cancelBtn = findViewById(R.id.cancel)
        /**
         * ОБработка нажатия на кнопку отмена
         */
        cancelBtn.setOnClickListener { dismiss() }

        overrunWarning = findViewById(R.id.write_off_overrun)
        if(warningVisible){
            overrunWarning.visibility = View.VISIBLE
        }

    }
}