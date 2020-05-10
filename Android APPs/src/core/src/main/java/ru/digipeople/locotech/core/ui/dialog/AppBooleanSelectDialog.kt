package ru.digipeople.locotech.core.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.RadioGroup
import android.widget.TextView
import ru.digipeople.locotech.core.R

/**
 * Диалог для выбора значений
 */
class AppBooleanSelectDialog(context: Context) : AlertDialog(context) {

    //region Views
    private lateinit var radioGroup: RadioGroup
    //endregion
    //region Other
    var title: String = ""
    var value: Boolean? = null
    var onOkBtnClickListener: ((Boolean?) -> Unit)? = null
    var onCancelBtnClickListener: (() -> Unit)? = null
    //endregion
    /**
     * Действия при созданиии диалога
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_app_boolean_select)

        findViewById<TextView>(R.id.title).text = title

        radioGroup = findViewById(R.id.radioGroup)
        val checkedId = valueToId(value)
        radioGroup.check(checkedId)
        /**
         * Обработка нажатия ок
         */
        findViewById<View>(R.id.ok_button).setOnClickListener {
            val value = idToValue(radioGroup.checkedRadioButtonId)
            onOkBtnClickListener?.invoke(value)
            dismiss()
        }
        /**
         * Обработка нажатия cancel
         */
        findViewById<View>(R.id.cancel_button).setOnClickListener {
            onCancelBtnClickListener?.invoke()
            dismiss()
        }
    }
    /**
     * Преобразования значений в id и наоборот
     */
    private fun idToValue(viewId: Int) = when (viewId) {
        R.id.trueRadioBtn -> true
        R.id.falseRadioBtn -> false
        else -> null
    }

    private fun valueToId(value: Boolean?) = when (value) {
        true -> R.id.trueRadioBtn
        false -> R.id.falseRadioBtn
        null -> R.id.nullRadioBtn
    }
}