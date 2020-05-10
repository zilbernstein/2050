package ru.digipeople.locotech.master.ui.activity.measurement.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_dialog_measurements_task.*
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.master.R
/**
 * Диалоговое окно ввода параметров при запросе аппаратных замеров
 */
class MeasurementTaskDialog : DialogFragment() {
    private val sectionNumber
        get() = section_number_input.text.toString()
    private var onAcceptBtnClickListener: ((stage: Stage, sectionNumber: String) -> Unit)? = null
    /**
     * действия при создании окна
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.fragment_dialog_measurements_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spinnerAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Stage.titles)
        measurement_stage_list.apply {
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = spinnerAdapter
        }
        /**
         * обработка нажатия на кнопку подтверждения
         */
        positive_btn.setOnClickListener {
            onAcceptBtnClickListener?.invoke(Stage.of(measurement_stage_list.selectedItem as String), sectionNumber)
            dismiss()
        }
        /**
         * обработка нажатия на кнопку отмены
         */
        negative_btn.setOnClickListener { dismiss() }
    }
    /**
     * создание диалога
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
                .apply {
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    window?.attributes?.gravity = Gravity.TOP
                    window?.attributes?.verticalMargin = MARGIN_20_PERCENT
                }
    }

    companion object {
        private const val TAG = "measurement_task_dialog"
        private const val MARGIN_20_PERCENT = 0.2f
        /**
         * отображение диалогового окна
         */
        fun show(fragmentManager: FragmentManager, onAcceptListener: (stage: Stage, sectionNumber: String) -> Unit) {
            fragmentManager.findFragmentByTag(TAG) ?: MeasurementTaskDialog()
                    .apply {
                        onAcceptBtnClickListener = onAcceptListener
                        show(fragmentManager, TAG)
                    }
        }
    }
}