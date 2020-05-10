package ru.digipeople.locotech.master.ui.activity.equipment.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import ru.digipeople.locotech.core.data.model.WorksCounterViewType
/**
 * класс определяет количество работ по вкладкам
 */
object WorksCountStringBuilder {
    /**
     * типы вкладок в МП Мастер
     */
    private val worksCountViewTypes = listOf(
            WorksCounterViewType.NO_PERFORMER,
            WorksCounterViewType.IN_MATCHING,
            WorksCounterViewType.ASSIGNED,
            WorksCounterViewType.IN_PROGRESS,
            WorksCounterViewType.COMPLETED,
            WorksCounterViewType.ACCEPTED_MASTER,
            WorksCounterViewType.ALL
    )

    fun build(worksCountMap: Map<WorksCounterViewType, Int>, context: Context): Spannable {
        val spannableStringBuilder = SpannableStringBuilder()
        /**
         * установка значения, цвета и жирности
         */
        for ((index, workCountViewType) in worksCountViewTypes.withIndex()) {
            val value = worksCountMap[workCountViewType] ?: 0
            val color = ContextCompat.getColor(context, workCountViewType.color)
            val span: Any = if (workCountViewType == WorksCounterViewType.ALL) {
                StyleSpan(Typeface.BOLD)
            } else ForegroundColorSpan(color)

            spannableStringBuilder.append(value.toString(), span, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            /**
             * добавление разделителя
             */
            if (index != worksCountViewTypes.lastIndex)
                spannableStringBuilder.append("/")
        }
        return spannableStringBuilder
    }
}
