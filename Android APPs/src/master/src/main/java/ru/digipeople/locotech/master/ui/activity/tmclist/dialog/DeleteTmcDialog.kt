package ru.digipeople.locotech.master.ui.activity.tmclist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import ru.digipeople.locotech.master.R

/**
 * Диалог удаления элемента из списка ТМЦ
 *
 * @author Kashonkov Nikita
 */
class DeleteTmcDialog(context: Context) : Dialog(context) {
    //region Views
    private lateinit var title: TextView
    private lateinit var agreeBtn: Button
    private lateinit var cancelBtn: Button
    var agreeListener: (() -> Unit)? = null
    //endregion

    /**
     * Действия при созданиии диалога
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.delete_tmc_dialog)
        agreeBtn = findViewById(R.id.custom_remark_agree)
        /**
         * Обработка нажатия на кнопки принять
         */
        agreeBtn.setOnClickListener {
            dismiss()
            agreeListener?.invoke()
        }
        cancelBtn = findViewById(R.id.custom_remark_cancel)
        /**
         * Обработка нажатия на кнопку отмена
         */
        cancelBtn.setOnClickListener { dismiss() }
    }
}
