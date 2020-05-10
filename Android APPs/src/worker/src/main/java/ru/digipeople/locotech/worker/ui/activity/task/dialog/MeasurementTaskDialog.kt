package ru.digipeople.locotech.worker.ui.activity.task.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_dialog_measurements_task.*
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.worker.R

/**
 * Класс создающий диалог для работы с замерами
 */
class MeasurementTaskDialog : DialogFragment() {
    /**
     * Обработка нажатия на кнопку принять
     */
    private var onAcceptBtnClickListener: ((stage: Stage, sectionNumber: String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.fragment_dialog_measurements_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, Stage.titles)
        measurement_stage_list.apply {
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = spinnerAdapter
        }/**
         * Обработка положительного ответа
         */
        positive_btn.setOnClickListener {
            onAcceptBtnClickListener?.invoke(
                    Stage.of(measurement_stage_list.selectedItem as String),
                    section_number_input.text.toString()
            )
            dismiss()
        }/**
         * Обработка отрицательного ответа
         */
        negative_btn.setOnClickListener { dismiss() }
    }
    /**
     * Создание диалога
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
                .apply {
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    window?.attributes?.gravity = Gravity.TOP
                    window?.attributes?.verticalMargin = 0.2f
                }
    }

    companion object {
        private const val TAG = "measurement_task_dialog"

        fun show(fragmentManager: FragmentManager, onAcceptListener: (stage: Stage, sectionNumber: String) -> Unit) {
            fragmentManager.findFragmentByTag(TAG) ?: MeasurementTaskDialog()
                    .apply {
                        onAcceptBtnClickListener = onAcceptListener
                        show(fragmentManager, TAG)
                    }
        }
    }
}