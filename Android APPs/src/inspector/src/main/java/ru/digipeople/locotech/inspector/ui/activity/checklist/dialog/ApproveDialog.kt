package ru.digipeople.locotech.inspector.ui.activity.checklist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import ru.digipeople.locotech.inspector.R

/**
 * Класс, создающий диалоговое окно
 *
 * @author Kashonkov Nikita
 */
class ApproveDialog(context: Context) : Dialog(context){
    //region Views
    private lateinit var agreeBtn: Button
    var agreeListener: (() -> Unit)? = null
    //endregion
    /**
     * Действия при создании окна
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.approve_dialog)
        agreeBtn = findViewById(R.id.approve_dialog_ok)
        agreeBtn.setOnClickListener { agreeListener?.invoke() }
    }


}