package ru.digipeople.locotech.inspector.ui.activity.print.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import ru.digipeople.locotech.inspector.R

/**
 * Диалоговое окно очистки списка подписантов
 *
 * @author Kashonkov Nikita
 */
class ClearAllSignersDialog (context: Context): Dialog(context) {
    //region Views
    private lateinit var agreeBtn: Button
    private lateinit var cancelBtn: Button
    var agreeListener: (() -> Unit)? = null
    //endregion
    //region Other
    //endregion
    /**
     * действия пи создании диалогового окна
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.delete_signers_dialog)
        agreeBtn = findViewById(R.id.delete_signers)
        agreeBtn.setOnClickListener {
            agreeListener?.invoke()
            dismiss()
        }
        cancelBtn = findViewById(R.id.cancel)
        cancelBtn.setOnClickListener { dismiss() }
    }


}