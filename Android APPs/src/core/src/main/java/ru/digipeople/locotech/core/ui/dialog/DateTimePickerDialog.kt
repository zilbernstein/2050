package ru.digipeople.locotech.core.ui.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.*

/**
 * Диалог выбора времени и даты.
 * Реализован через последовательное отображение [DatePickerDialog] и [TimePickerDialog].
 * Обновляет поля входящего [calendar].
 *
 * @author Aleksandr Brazhkin
 */
class DateTimePickerDialog(private val calendar: Calendar) {

    private var onDateTimeSetListener: ((calendar: Calendar) -> Unit)? = null
    /**
     * установка времени и даты
     */
    fun onDateTimeSet(onDateTimeSetListener: (calendar: Calendar) -> Unit): DateTimePickerDialog {
        this.onDateTimeSetListener = onDateTimeSetListener
        return this
    }
    /**
     * показать диалог
     */
    fun show(context: Context) {
        showDatePickerDialog(context)
    }
    /**
     * Диалог выбора даты
     */
    private fun showDatePickerDialog(context: Context) {
        DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    showTimePickerDialog(context, year, month, dayOfMonth)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    /**
     * Диалог выбора времени
     */
    private fun showTimePickerDialog(context: Context, year: Int, month: Int, dayOfMonth: Int) {
        TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    onDateTimeSetListener?.invoke(calendar)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show()
    }
}