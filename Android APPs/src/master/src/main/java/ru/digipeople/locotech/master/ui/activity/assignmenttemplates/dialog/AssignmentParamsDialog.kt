package ru.digipeople.locotech.master.ui.activity.assignmenttemplates.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_assignment_params.*
import ru.digipeople.locotech.master.R
import java.text.SimpleDateFormat
import java.util.*
/**
 * Диалоговое окно установки параметров назначения шаблона исполнитей
 */
class AssignmentParamsDialog : DialogFragment() {
    private var onAcceptBtnClickListener: ((date: Date, nightShift: Boolean) -> Unit)? = null

    private val defaultTimeZone = TimeZone.getTimeZone("UTC")
    private val calendar = Calendar.getInstance(defaultTimeZone)
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .apply { timeZone = defaultTimeZone }
    private val nightShift
        get() = assignment_night_shift_flag.isChecked
    private val date
        get() = assignment_date_input.text.toString()
    /**
     * Операции при создании окна
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_assignment_params, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Установка обработчиков нажатий на кнопки и выбора даты
         */
        assignment_accept_btn.setOnClickListener {
            onAcceptBtnClickListener?.invoke(dateFormatter.parse(date), nightShift)
            dismiss()
        }
        assignment_cancel_btn.setOnClickListener { dismiss() }
        assignment_date_input.setOnClickListener { dateInputView ->
            showDatePickerDialog { pickedDate ->
                (dateInputView as EditText).setText(dateFormatter.format(pickedDate))
            }
        }
        //Set current date as default
        assignment_date_input.setText(dateFormatter.format(Date()))
    }
    /**
     * Создание диалога с пользователем
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
                .apply {
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    window?.attributes?.gravity = Gravity.TOP
                    window?.attributes?.verticalMargin = MARGIN_20_PERCENT
                }
    }
    /**
     * Создание диалога выбора даты
     */
    private fun showDatePickerDialog(onDatePicked: (Date) -> Unit) {
        DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val date = calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }.time
            onDatePicked.invoke(date)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show()
    }

    companion object {
        private const val TAG = "assignment_params_dialog"
        private const val MARGIN_20_PERCENT = 0.2f
        /**
         * Функция показа диалога
         */
        fun show(fragmentManager: FragmentManager, onAcceptListener: (date: Date, nightShift: Boolean) -> Unit) {
            fragmentManager.findFragmentByTag(TAG) ?: AssignmentParamsDialog()
                    .apply {
                        onAcceptBtnClickListener = onAcceptListener
                        show(fragmentManager, TAG)
                    }
        }
    }
}