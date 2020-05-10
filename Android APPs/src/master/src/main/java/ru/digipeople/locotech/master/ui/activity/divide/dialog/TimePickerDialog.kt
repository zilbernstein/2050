package ru.digipeople.locotech.master.ui.activity.divide.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import ru.digipeople.locotech.master.R
import mobi.upod.timedurationpicker.TimeDurationPicker
import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment

/**
 * Диалог установки времени при разделения работы
 *
 * @author Kashonkov Nikita
 */
class TimePickerDialog : TimeDurationPickerDialogFragment() {
    private var initTime = 0L
    private var maxTime = 0L
    var setTimeListener: ((time: Long) -> Unit)? = null
    var dismissListener: (() -> Unit)? = null
    /**
     * операции при создании диалога
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTime = arguments!!.getLong(INIT_TIME)
        maxTime = arguments!!.getLong(MAX_TIME)
    }
    /**
     * начальная установка времени
     */
    override fun getInitialDuration(): Long {
        return initTime
    }
    /**
     * установка отображения времени
     */
    override fun setTimeUnits(): Int {
        return TimeDurationPicker.HH_MM
    }

    override fun onDurationSet(view: TimeDurationPicker, duration: Long) {
        /**
         * проверка установки времени
         */
        if(duration < maxTime){
            /**
             * установить выбранное время
             */
            setTimeListener?.invoke(duration)
        } else{
            /**
             * сообщаем об ошибке
             */
            showOverTimeError()
        }
    }
    /**
     * отображение ошибки
     */
    private fun showOverTimeError(){
        Toast.makeText(activity, R.string.divide_overtime_error, Toast.LENGTH_LONG).show()
    }
    /**
     * отмена выбора времени
     */
    override fun onDismiss(dialog: DialogInterface?) {
        dismissListener?.invoke()
        super.onDismiss(dialog)
    }

    companion object {
        //region Extra
        private const val INIT_TIME = "INIT_TIME"
        private const val MAX_TIME = "MAX_TIME"
        //end Region
        fun newInstance(initTime: Long, maxTime: Long): TimePickerDialog {
            return TimePickerDialog().apply {
                arguments = Bundle().apply {
                    putLong(INIT_TIME, initTime)
                    putLong(MAX_TIME, maxTime)
                }
            }
        }
    }
}
