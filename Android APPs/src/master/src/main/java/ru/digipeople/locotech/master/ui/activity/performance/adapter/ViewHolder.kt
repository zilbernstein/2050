package ru.digipeople.locotech.master.ui.activity.performance.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_performance.*
import ru.digipeople.locotech.core.data.model.Performer
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.popup.MenuItemType
import ru.digipeople.utils.DateUtils
import ru.digipeople.utils.android.AndroidUtils

abstract class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    /**
     * привязка данных полученных к шаблону
     */
    fun bind(work: Work) {
        card_performance_up_separator.visibility = View.VISIBLE
        card_performance_first_button.visibility = View.VISIBLE
        card_performance_second_button.visibility = View.VISIBLE
        card_performance_work_status.text = ""

        card_performance_status_view.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.appTransparent))
        card_performance_card.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.appWhite))

        card_performance_title.text = work.title
        repeats_count.text = work.repeats.toString()
        /**
         * отображение комментария
         */
        if (work.comment != null && work.comment!!.text.isNotEmpty()) {
            expand_collapse.setOnClickListener {
                card_performance_comment.isExpanded
                card_performance_comment.toggle()
                when (card_performance_comment.isExpanded) {
                    true -> expand_collapse.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_arrow_down)
                    false -> expand_collapse.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_arrow_up)
                }
            }
            card_performance_comment_group.visibility = View.VISIBLE
            card_performance_comment.text = work.comment!!.text
        } else card_performance_comment_group.visibility = View.GONE

        bindPerformers(work.performers)
        /**
         * формирование времени
         */
        val timeStr: String = DateUtils.convertToString(work.normalTime)
        val workPartPercent: String = work.workPartPercent.toString()
        val spannable = SpannableString(itemView.context.getString(R.string.card_performance_norm, timeStr, workPartPercent))
        spannable.setSpan(ForegroundColorSpan(Color.GRAY), spannable.indexOf('('), spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        card_performance_time.text = spannable
        card_performance_measure_value.text = itemView.context.getString(R.string.card_performance_measurements, work.measurements)

        val remainsTime: String = DateUtils.convertToString(work.normalTime * work.workPartPercent / 100)
        val timePassed: String = DateUtils.convertToString((work.remainTime))
        card_performance_time_remain.text = itemView.context.getString(R.string.card_performance_time_remain, timePassed, remainsTime)

        performers_title.text = itemView.context.getString(R.string.card_performance_outfit_number, work.outfitNumber)
        /**
         * оображение иконок фильтров
         */
        ic_measurements.isVisible = work.hasMeasurements
        ic_tmc.isVisible = work.hasTmc
        ic_mpi.isVisible = work.hasMpi

        bindInternal(work)
    }

    protected abstract fun bindInternal(work: Work)
    /**
     * формирование меню
     */
    protected fun resolveMenuItemType(item: MenuItem): MenuItemType {
        return when (item.itemId) {
            R.id.comment -> MenuItemType.COMMENT
            R.id.add_performer -> MenuItemType.PERFORMER
            R.id.tmc -> MenuItemType.TMC
            R.id.photo -> MenuItemType.PHOTO
            R.id.divide -> MenuItemType.DIVIDE
            R.id.measurement -> MenuItemType.MEASUREMENT
            R.id.workaround -> MenuItemType.WORKAROUND
            else -> throw IllegalStateException("Unable to determine menu item type with id ${item.itemId}")
        }
    }
    /**
     * установка времени и прогресса
     */
    protected fun setProgressAndTime(work: Work) {
        val doubleTimeForDividing = (work.normalTime - work.remainTime) * 100
        val progress = if (work.normalTime == 0L) 0 else
            ((doubleTimeForDividing / work.normalTime)).toInt()

        if (progress <= MAX_VISIBLE_PROGRESS) {
            card_performance_percent.text = itemView.context.getString(R.string.card_performance_percent, progress)
        } else card_performance_percent.text = itemView.context.getString(R.string.card_performance_max_visible_percent)
        /**
         * раскраска в зависимости о процента выполнения
         */
        when (progress) {
            in 0..90 -> card_performance_progress_bar.progressDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.green_progress_bar_drawable)
            in 90..95 -> card_performance_progress_bar.progressDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.yellow_progress_bar_drawable)
            else -> card_performance_progress_bar.progressDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.red_progress_bar_drawable)
        }

        card_performance_progress_bar.progress = progress

        val remainsTime: String = DateUtils.convertToString(work.normalTime * work.workPartPercent / 100)
        val timePassed: String = DateUtils.convertToString((work.remainTime))
        val spannableRemain = SpannableString("$timePassed / $remainsTime")
        /**
         * раскраска времени
         */
        if (work.remainTime < 0) {
            spannableRemain.setSpan(ForegroundColorSpan(Color.RED), 0, spannableRemain.indexOf('/'), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else spannableRemain.setSpan(ForegroundColorSpan(Color.BLACK), 0, spannableRemain.indexOf('/'), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        card_performance_time_remain.text = spannableRemain
    }
    /**
     * отображение исполниетелей
     */
    private fun bindPerformers(performers: List<Performer>) {
        hidePerformers()
        for (i in performers.indices) {
            if (i >= DISPLAYED_PERFORMERS_MAX_COUNT) break
            val performerView = card_performance_performers.getChildAt(i) as TextView?
                    ?: inflatePerformerView()
            performerView.text = performers[i].name
            performerView.isVisible = true
        }
        /**
         * если исполнителей больш емаксимального числа
         */
        if (performers.size > DISPLAYED_PERFORMERS_MAX_COUNT) {
            card_performance_other_performers_amount.isVisible = true
            val otherPerformersCount = performers.size - DISPLAYED_PERFORMERS_MAX_COUNT
            card_performance_other_performers_amount.text = itemView.context.getString(R.string.performance_other_performers_amount, otherPerformersCount)
        } else card_performance_other_performers_amount.isVisible = false
    }
    /**
     * скрыть исполнителей
     */
    private fun hidePerformers() {
        card_performance_performers.forEach { it.isVisible = false }
    }
    /**
     * параметры шаблона
     */
    private fun inflatePerformerView(): TextView {
        return TextView(itemView.context).apply {
            card_performance_performers.addView(this)
            updateLayoutParams<LinearLayout.LayoutParams> {
                width = LinearLayout.LayoutParams.WRAP_CONTENT
                height = LinearLayout.LayoutParams.WRAP_CONTENT
                setPadding(AndroidUtils.dpToPx(8f, context).toInt())
                marginStart = AndroidUtils.dpToPx(4f, context).toInt()
                background = context.getDrawable(R.drawable.name_text_background)
                setTextColor(ContextCompat.getColor(context, R.color.appBlack))
            }
        }
    }

    companion object {
        private const val DISPLAYED_PERFORMERS_MAX_COUNT = 3
        private const val MAX_VISIBLE_PROGRESS = 999
    }
}