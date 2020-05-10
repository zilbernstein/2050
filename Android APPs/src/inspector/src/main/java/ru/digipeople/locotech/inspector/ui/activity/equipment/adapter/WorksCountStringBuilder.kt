package ru.digipeople.locotech.inspector.ui.activity.equipment.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import ru.digipeople.locotech.core.data.model.WorksCounterViewType
/**
 * Класс реализует подсчет работ по вкладкам с экрана инспекционных контроль на экране списка секций
 */
object WorksCountStringBuilder {
    private val worksCountViewTypes = listOf(
            WorksCounterViewType.CYCLIC,
            WorksCounterViewType.REMARKS_SLD,
            WorksCounterViewType.REMARKS_RZD
    )

    fun build(worksCountMap: Map<WorksCounterViewType, Int>, context: Context): Spannable {
        val spannableStringBuilder = SpannableStringBuilder()
        /**
         * Установка значения и цвета элемента
         */
        for ((index, workCountViewType) in worksCountViewTypes.withIndex()) {
            val value = worksCountMap[workCountViewType] ?: 0
            val color = ContextCompat.getColor(context, workCountViewType.color)
            val span: Any = if (workCountViewType == WorksCounterViewType.ALL) {
                StyleSpan(Typeface.BOLD)
            } else ForegroundColorSpan(color)

            spannableStringBuilder.append(value.toString(), span, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            if (index != worksCountViewTypes.lastIndex)
                spannableStringBuilder.append("/")
        }
        return spannableStringBuilder
    }
}