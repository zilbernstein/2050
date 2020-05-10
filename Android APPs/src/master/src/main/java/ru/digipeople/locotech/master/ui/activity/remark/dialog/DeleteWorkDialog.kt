package ru.digipeople.locotech.master.ui.activity.tmclist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import ru.digipeople.locotech.master.R

/**
 * Диалог удаления работы из замечания
 *
 * @author Kashonkov Nikita
 */
class DeleteWorkDialog(context: Context) : Dialog(context) {
    //region Views
    private lateinit var agreeBtn: Button
    private lateinit var cancelBtn: Button
    var agreeListener: (() -> Unit)? = null
    //endregion

    /**
     * действия при созаднии диалогового окна
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.delete_work_dialog)
        agreeBtn = findViewById(R.id.custom_remark_agree)
        /**
         * обработка нажаия на кнопку принять
         */
        agreeBtn.setOnClickListener {
            dismiss()
            agreeListener?.invoke()
        }
        cancelBtn = findViewById(R.id.custom_remark_cancel)
        /**
         * обработка нажатия на кнопку отмена
         */
        cancelBtn.setOnClickListener { dismiss() }
    }
}
