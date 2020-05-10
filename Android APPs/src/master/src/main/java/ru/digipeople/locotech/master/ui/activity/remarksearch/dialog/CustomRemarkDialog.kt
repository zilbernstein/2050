package ru.digipeople.locotech.master.ui.activity.remarksearch.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import ru.digipeople.locotech.master.R

/**
 * Диалог подтверждения нового замечания
 *
 * @author Kashonkov Nikita
 */
class CustomRemarkDialog(context: Context): Dialog(context) {
    //region Views
    private lateinit var title: TextView
    private lateinit var agreeBtn: Button
    private lateinit var cancelBtn: Button
    var agreeListener: ((text: String) -> Unit)? = null
    var cancelListener: (() -> Unit)? = null
    //endregion
    //region Other
    lateinit var titleText: String
    //endregion

    /**
     * Действия при создании диалогового окна
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_remark_dialog)
        title = findViewById(R.id.message_title)
        title.text = titleText
        agreeBtn = findViewById(R.id.custom_remark_agree)
        /**
         * Обработчик нажатия на кнопку согласия
         */
        agreeBtn.setOnClickListener { agreeListener?.invoke(titleText) }
        cancelBtn = findViewById(R.id.custom_remark_cancel)
        /**
         * Обработчик нажатия на кнопку отмены
         */
        cancelBtn.setOnClickListener { cancelListener?.invoke() }
    }


}