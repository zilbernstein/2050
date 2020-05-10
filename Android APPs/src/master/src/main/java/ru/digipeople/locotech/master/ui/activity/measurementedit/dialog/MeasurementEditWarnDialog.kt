package ru.digipeople.locotech.master.ui.activity.measurementedit.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_dialog_measurement_edit_warn.*
import ru.digipeople.locotech.master.R
/**
 * Диалоговое окно с предупреждением о замене аппаратного замера ручным
 */
class MeasurementEditWarnDialog : DialogFragment() {
    private var onAcceptBtnClickListener: (() -> Unit)? = null
    /**
     * действия при создании окна
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.fragment_dialog_measurement_edit_warn, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /**
         * обработка нажатия кнопки принять
         */
        positive_btn.setOnClickListener {
            onAcceptBtnClickListener?.invoke()
            dismiss()
        }
        /**
         * обработка нажатия кнопки отмена
         */
        negative_btn.setOnClickListener { dismiss() }
    }

    companion object {
        private const val TAG = "measurement_edit_warn_dialog"

        fun show(fragmentManager: FragmentManager, onAcceptListener: () -> Unit) {
            fragmentManager.findFragmentByTag(TAG) ?: MeasurementEditWarnDialog()
                    .apply {
                        onAcceptBtnClickListener = onAcceptListener
                        show(fragmentManager, TAG)
                    }
        }
    }
}