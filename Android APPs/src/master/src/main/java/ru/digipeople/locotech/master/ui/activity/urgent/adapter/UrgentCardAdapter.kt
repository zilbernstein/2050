package ru.digipeople.locotech.master.ui.activity.urgent.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.WorkStatus
import kotlinx.android.synthetic.main.card_urgency.view.*
import ru.digipeople.ui.adapter.BaseDataAdapter
import ru.digipeople.ui.widget.setForceShowIcon
import ru.digipeople.utils.DateUtils
import javax.inject.Inject

/**
 * Адаптер срочно
 * @author Kashonkov Nikita
 */
class UrgentCardAdapter @Inject constructor(val context: Context) :
        androidx.recyclerview.widget.RecyclerView.Adapter<UrgentCardAdapter.UrgentViewHolder>(), BaseDataAdapter<Work> {

    //region Other
    var dataList: List<Work> = emptyList()
    var commentItemClickListener: ((work: Work) -> Unit)? = null
    var startButtonClickedListener: ((work: Work) -> Unit)? = null
    var stopButtonClickedListener: ((work: Work) -> Unit)? = null
    var acceptButtonClickedListener: ((work: Work) -> Unit)? = null
    var returnButtonClickedListener: ((work: Work) -> Unit)? = null
    var tmcClickListener:((work: Work) -> Unit)? = null
    var measurementClickListener: ((work: Work) -> Unit)? = null
    //endregion

    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UrgentViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.card_urgency, p0, false)
        return UrgentViewHolder(view)
    }
    /**
     * Полсчет числа элементов
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: UrgentViewHolder, p1: Int) {
        p0.bind()
    }
    /**
     * Установка данных
     */
    override fun setData(list: List<Work>) {
        val diffUtilCallback = DiffUtilCallback(dataList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.dataList = list
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * Холдер явки
     */
    inner class UrgentViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val statusView = view.card_urgency_remark_status_view
        private val repeatsCount = view.repeats_count
        private val title = view.card_urgency_title
        private val menu = view.card_urgency_menu
        private val equipment = view.card_urgency_equipment_number
        private val firstPerformer = view.card_urgency_first_performer
        private val thirdPerformer = view.card_urgency_third_performer
        private val secondPerformer = view.card_urgency_second_performer
        private val performerAmount = view.card_urgency_amount_performer
        private val comment = view.card_urgency_details_remark_comment
        private val commentGroup = view.card_urgency_remark_comment_group
        private val firstButton = view.card_urgency_first_button
        private val secondButton = view.card_urgency_second_button
        private val measure = view.card_measure_value

        val time = view.card_urgency_time
        val time_remain = view.card_urgency_time_remain
        val performers_title = view.performers_title
        /**
         * Привязка данных к шаблону
         */
        fun bind() {
            val work = dataList.get(adapterPosition)
            repeatsCount.text = "${work.repeats}"
            val timeStr: String = DateUtils.convertToString(work.normalTime)
            val workPartPercent: String = work.workPartPercent.toString()
            val spanneble = SpannableString("Норма: $timeStr ($workPartPercent%)")
            spanneble.setSpan(StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spanneble.setSpan(ForegroundColorSpan(Color.GRAY), 16, spanneble.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            time.setText(spanneble)
            val spannableMeasure = SpannableString("Замеры: ${work.measurements}")
            spannableMeasure.setSpan(StyleSpan(BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            measure.setText(spannableMeasure)
            /**
             * Расчет времени
             */
            val remainsTime: String = DateUtils.convertToString(work.normalTime * work.workPartPercent / 100)
            val timePassed: String = DateUtils.convertToString((work.normalTime * work.workPartPercent / 100) - (work.remainTime * work.workPartPercent / 100))
            val spannebleRemain = SpannableString("$timePassed / $remainsTime")
            time_remain.setText(spannebleRemain)
            val outfitNumber = work.outfitNumber.toString()
            performers_title.setText("$outfitNumber:  ")
            /**
             * Раскраска в зависимости от статуса
             */
            when (work.status) {
                /**
                 * В работе
                 */
                WorkStatus.IN_WORK -> {
                    if (work.isExpired) {
                        statusView.background = ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_yellow)
                        firstButton.visibility = View.VISIBLE
                        firstButton.text = context.getString(R.string.urgent_activity_stop)
                        firstButton.background = ContextCompat.getDrawable(context, R.drawable.yellow_btn)
                        firstButton.setOnClickListener { stopButtonClickedListener?.invoke(work) }

                        secondButton.visibility = View.GONE

                        title.text = context.getString(R.string.urgent_activity_status_expired, work.title)

                    }
                }
                /**
                 * На паузе
                 */
                WorkStatus.PAUSED -> {
                    statusView.background = ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_yellow)

                    firstButton.visibility = View.VISIBLE
                    firstButton.text = context.getString(R.string.urgent_activity_restart)
                    firstButton.background = ContextCompat.getDrawable(context, R.drawable.green_btn)
                    firstButton.setOnClickListener { startButtonClickedListener?.invoke(work) }

                    secondButton.visibility = View.GONE

                    title.text = context.getString(R.string.urgent_activity_status_paused, work.title)
                }
                /**
                 * Выполнена
                 */
                WorkStatus.DONE -> {
                    statusView.background = ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_green)

                    firstButton.visibility = View.VISIBLE
                    firstButton.text = context.getString(R.string.urgent_activity_return)
                    firstButton.background = ContextCompat.getDrawable(context, R.drawable.yellow_btn)
                    firstButton.setOnClickListener { returnButtonClickedListener?.invoke(work) }

                    secondButton.visibility = View.VISIBLE
                    secondButton.text = context.getString(R.string.urgent_activity_accept)
                    secondButton.background = ContextCompat.getDrawable(context, R.drawable.green_btn)
                    secondButton.setOnClickListener { acceptButtonClickedListener?.invoke(work) }

                    title.text = context.getString(R.string.urgent_activity_status_done, work.title)

                }
            }
            /**
             * Обработка нажатия на меню
             */
            menu.setOnClickListener {
                val popupMenu = PopupMenu(context, menu)
                popupMenu.inflate(R.menu.urgent_item_menu)
                popupMenu.setForceShowIcon(true)
                /**
                 * Обработка подпунктов
                 */
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.comment -> commentItemClickListener?.invoke(work)
                        R.id.tmc -> tmcClickListener?.invoke(work)
                        R.id.measurement -> measurementClickListener?.invoke(work)
                    }

                    true
                }
                popupMenu.show()

            }
            val equipName = work.equipmentName
            val spanEquip = SpannableString("Локомотив/секция: $equipName")
            spanEquip.setSpan(StyleSpan(BOLD), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            //equipment.text = context.getString(R.string.urgent_activity_equipment, work.equipmentName)
            equipment.text = spanEquip
            /**
             * Обработка списка исполнителей
             * пустой
             */
            if (work.performers.isEmpty()) {
                firstPerformer.visibility = View.GONE
                secondPerformer.visibility = View.GONE
                thirdPerformer.visibility = View.GONE
                performerAmount.visibility = View.GONE
            } else {
                /**
                 * 1 исполнитель
                 */
                if (work.performers.size == 1) {
                    firstPerformer.text = work.performers.get(0).name
                    firstPerformer.visibility = View.VISIBLE
                    secondPerformer.visibility = View.GONE
                    thirdPerformer.visibility = View.GONE
                    performerAmount.visibility = View.GONE
                    /**
                     * 2 исполнителя
                     */
                } else if (work.performers.size == 2) {
                    firstPerformer.text = work.performers.get(0).name
                    secondPerformer.text = work.performers.get(1).name
                    firstPerformer.visibility = View.VISIBLE
                    secondPerformer.visibility = View.VISIBLE
                    thirdPerformer.visibility = View.GONE
                    performerAmount.visibility = View.GONE
                    /**
                     * 3 исполниетеля
                     */
                } else if (work.performers.size == 3) {
                    firstPerformer.text = work.performers.get(0).name
                    secondPerformer.text = work.performers.get(1).name
                    thirdPerformer.text = work.performers.get(2).name
                    firstPerformer.visibility = View.VISIBLE
                    secondPerformer.visibility = View.VISIBLE
                    thirdPerformer.visibility = View.VISIBLE
                    performerAmount.visibility = View.GONE
                    /**
                     * больше 3 исполнителей
                     */
                } else {
                    firstPerformer.text = work.performers.get(0).name
                    secondPerformer.text = work.performers.get(1).name
                    thirdPerformer.text = work.performers.get(2).name
                    performerAmount.text = context.getString(R.string.urgent_other_performers_amount, work.performers.size - 3)
                    firstPerformer.visibility = View.VISIBLE
                    secondPerformer.visibility = View.VISIBLE
                    thirdPerformer.visibility = View.VISIBLE
                    performerAmount.visibility = View.VISIBLE
                }
            }
            /**
             * Управление видимостью комментариев
             */
            if (work.comment == null || work.comment!!.text.isEmpty()) {
                commentGroup.visibility = View.GONE
            } else {
                commentGroup.visibility = View.VISIBLE
                comment.text = work.comment!!.text
            }

        }
    }

}