package ru.digipeople.locotech.inspector.ui.activity.remarksearch

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import ru.digipeople.locotech.inspector.R

/**
 * Диалоговое окно создания нового замечания
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
     * Действия при создании диалога
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_remark_dialog)
        title = findViewById(R.id.message_title)
        title.text = titleText
        agreeBtn = findViewById(R.id.custom_remark_agree)
        agreeBtn.setOnClickListener { agreeListener?.invoke(titleText) }
        cancelBtn = findViewById(R.id.custom_remark_cancel)
        cancelBtn.setOnClickListener { cancelListener?.invoke() }
    }


}