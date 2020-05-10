package ru.digipeople.ui.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import ru.digipeople.ui.R

/**
 * Диалог для работы в статусе "Обходное решение"
 */
class WorkaroundNoticeDialog : DialogFragment() {
    private var onPositiveBtnClickListener: (() -> Unit)? = null
    private var onNegativeBtnClickListener: (() -> Unit)? = null
    /**
     * Действия при создании диалога
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    /**
     * Действия при отрисовке диалога
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_workaround_notice, container, false)

        view.findViewById<Button>(R.id.negative_btn).setOnClickListener {
            onNegativeBtnClickListener?.invoke()
        }
        view.findViewById<Button>(R.id.positive_btn).setOnClickListener {
            onPositiveBtnClickListener?.invoke()
        }

        return view
    }
    /**
     * Обработка нажатия кнопок принять отменить
     */
    fun withListeners(onPositiveClick: () -> Unit, onNegativeClick: () -> Unit): WorkaroundNoticeDialog {
        this.onPositiveBtnClickListener = onPositiveClick
        this.onNegativeBtnClickListener = onNegativeClick
        return this
    }
}