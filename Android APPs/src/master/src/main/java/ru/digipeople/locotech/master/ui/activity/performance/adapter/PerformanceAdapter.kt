package ru.digipeople.locotech.master.ui.activity.performance.adapter

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.card_performance.*
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.WorkStatus
import ru.digipeople.locotech.master.ui.activity.popup.MenuItemType
import ru.digipeople.ui.adapter.BaseRecyclerAdapter
import ru.digipeople.ui.widget.setForceShowIcon
import javax.inject.Inject

/**
 * Адаптер исполнения
 *
 * @author Kashonkov Nikita
 */
class PerformanceAdapter @Inject constructor() : BaseRecyclerAdapter<ViewHolder>() {
    var items = emptyList<Work>()

    var menuItemClickListener: ((menuItemType: MenuItemType, work: Work) -> Unit)? = null
    var stoppedClickListener: ((work: Work) -> Unit)? = null
    var applyClickListener: ((work: Work) -> Unit)? = null
    var returnClickListener: ((work: Work) -> Unit)? = null
    var startClickListener: ((work: Work) -> Unit)? = null

    // Время последнего обновления данных адаптера.
    // Вместе с AutoRefreshDelegate служит для отсчета таймеров работ
    private var lastTimeUpdate = SystemClock.uptimeMillis()
    private var timeDelta = 0L
    /**
     * холдер
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.card_performance, parent, false)
        /**
         * выбор холдера в зависимости от типа работы
         */
        return when (viewType) {
            VIEW_TYPE_WORK_IN_APPROVE -> InApproveViewHolder(view)
            VIEW_TYPE_WORK_APPROVED -> ApprovedViewHolder(view)
            VIEW_TYPE_WORK_IN_TASK -> InTaskViewHolder(view)
            VIEW_TYPE_WORK_IN_WORK -> InWorkViewHolder(view)
            VIEW_TYPE_WORK_PAUSED -> InWorkViewHolder(view)
            VIEW_TYPE_WORK_STOPPED -> StoppedViewHolder(view)
            VIEW_TYPE_WORK_CANCELED -> InWorkViewHolder(view)
            VIEW_TYPE_WORK_DONE -> DoneViewHolder(view)
            VIEW_TYPE_WORK_ACCEPTED_MASTER -> AcceptedMasterViewHolder(view)
            else -> throw IllegalStateException("Unable to create view holder with view type = $viewType")
        }
    }
    /**
     * определение типа работы
     */
    override fun getItemViewType(position: Int): Int {
        return when (items[position].status) {
            WorkStatus.IN_APPROVE -> VIEW_TYPE_WORK_IN_APPROVE
            WorkStatus.APPROVED -> VIEW_TYPE_WORK_APPROVED
            WorkStatus.IN_TASK -> VIEW_TYPE_WORK_IN_TASK
            WorkStatus.IN_WORK -> VIEW_TYPE_WORK_IN_WORK
            WorkStatus.PAUSED -> VIEW_TYPE_WORK_PAUSED
            WorkStatus.STOPPED -> VIEW_TYPE_WORK_STOPPED
            WorkStatus.CANCELED -> VIEW_TYPE_WORK_CANCELED
            WorkStatus.DONE -> VIEW_TYPE_WORK_DONE
            WorkStatus.ACCEPTED_MASTER -> VIEW_TYPE_WORK_ACCEPTED_MASTER
            /**
             * дргой тип
             */
            else -> throw IllegalStateException("Unable to get item view type at position = $position")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            if (payloads[0] == PAYLOAD_UPDATE_TIME && holder is InWorkViewHolder)
                holder.updateTime(items[position], timeDelta)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    /**
     * получения количества работ
     */
    override fun getItemCount() = items.size
    /**
     * обновление времени
     */
    fun updateTime() {
        val newDate = SystemClock.uptimeMillis()
        timeDelta = newDate - lastTimeUpdate
        lastTimeUpdate = newDate
        notifyItemRangeChanged(0, itemCount, PAYLOAD_UPDATE_TIME)
    }

    //region ApprovedViewHolder
    /**
     * Холдер для новых работ
     */
    inner class ApprovedViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindInternal(work: Work) {
            card_performance_work_status.setText(R.string.performance_activity_status_new)

            card_performance_second_button.visibility = View.GONE
            card_performance_second_button.setText(R.string.performance_activity_in_work)
            card_performance_second_button.setOnClickListener { startClickListener?.invoke(work) }
            card_performance_second_button.background = ContextCompat.getDrawable(context, R.drawable.green_btn)

            card_performance_menu.setOnClickListener {
                val popupMenu = PopupMenu(context, card_performance_menu)
                popupMenu.inflate(R.menu.new_work_menu)
                popupMenu.setForceShowIcon(true)

                if (work.status.code > WorkStatus.APPROVED.code) {
                    popupMenu.menu.findItem(R.id.workaround).isVisible = false
                }
                /**
                 * обработчик меню
                 */
                popupMenu.setOnMenuItemClickListener { item ->
                    menuItemClickListener?.invoke(resolveMenuItemType(item), work)
                    true
                }
                popupMenu.show()
            }

            card_performance_progress_bar.progress = 0
            card_performance_percent.text = "0%"
        }
    }
    //endregion

    //region InApproveViewHolder
    /**
     * Холдер на согласовании
     */
    inner class InApproveViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindInternal(work: Work) {
            card_performance_work_status.setText(R.string.performance_activity_status_in_approve)
            card_performance_second_button.visibility = View.GONE
            card_performance_first_button.visibility = View.GONE
            card_performance_menu.visibility = View.GONE

            card_performance_progress_bar.progress = 0
            card_performance_percent.text = "0%"
        }
    }
    //endregion

    //region InTaskViewHolder
    /**
     * Холдер работ в одидании
     */
    inner class InTaskViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindInternal(work: Work) {
            card_performance_work_status.setText(R.string.performance_activity_status_in_task)

            card_performance_second_button.visibility = View.GONE
            card_performance_first_button.setText(R.string.performance_activity_stop)
            card_performance_first_button.setOnClickListener { stoppedClickListener?.invoke(work) }
            card_performance_first_button.background = ContextCompat.getDrawable(context, R.drawable.yellow_btn)

            card_performance_menu.setOnClickListener {
                val popupMenu = PopupMenu(context, card_performance_menu)
                popupMenu.inflate(R.menu.waiting_menu)
                popupMenu.setForceShowIcon(true)

                popupMenu.setOnMenuItemClickListener { item ->
                    menuItemClickListener?.invoke(resolveMenuItemType(item), work)
                    true
                }
                popupMenu.show()
            }
            /**
             * управление видимостью исполниетелей
             */
            card_performance_status_view.setBackgroundColor(ContextCompat.getColor(context, R.color.appTransparent))
            if (work.performers.size < 0) {
                card_performance_up_separator.visibility = View.GONE
            } else card_performance_up_separator.visibility = View.VISIBLE


            card_performance_progress_bar.progress = 0
            card_performance_percent.text = "0%"
        }
    }
    //endregion

    //region InWorkViewHolder
    /**
     * Холдер работ В статусах В работе,Приостановлена и Отказ работника
     */
    inner class InWorkViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindInternal(work: Work) {
            card_performance_second_button.visibility = View.GONE
            card_performance_first_button.setText(R.string.performance_activity_stop)
            card_performance_first_button.setOnClickListener { stoppedClickListener?.invoke(work) }
            card_performance_first_button.background = ContextCompat.getDrawable(context, R.drawable.yellow_btn)

            when (work.status) {
                WorkStatus.IN_WORK -> {
                    card_performance_status_view.setBackgroundColor(ContextCompat.getColor(context, R.color.appGreen))
                    card_performance_work_status.setText(R.string.performance_activity_status_in_work)
                }
                WorkStatus.PAUSED -> {
                    card_performance_status_view.setBackgroundColor(ContextCompat.getColor(context, R.color.appYellow))
                    card_performance_work_status.setText(R.string.performance_activity_status_paused)
                }
                WorkStatus.CANCELED -> {
                    card_performance_status_view.setBackgroundColor(ContextCompat.getColor(context, R.color.appYellow))
                    card_performance_work_status.setText(R.string.performance_activity_status_cancelled)
                }
            }

            card_performance_menu.setOnClickListener {
                val popupMenu = PopupMenu(context, card_performance_menu)
                popupMenu.inflate(R.menu.in_task_menu)
                popupMenu.setForceShowIcon(true)

                popupMenu.setOnMenuItemClickListener { item ->
                    menuItemClickListener?.invoke(resolveMenuItemType(item), work)
                    true
                }
                popupMenu.show()
            }
            setProgressAndTime(work)
        }
        /**
         * обновление времени
         */
        fun updateTime(work: Work, timeDelta: Long) {
            work.remainTime -= timeDelta
            setProgressAndTime(work)
        }
    }
    //endregion

    //region StoppedViewHolder
    /**
     * Холдер работ остановленных
     */
    inner class StoppedViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindInternal(work: Work) {
            card_performance_status_view.setBackgroundColor(ContextCompat.getColor(context, R.color.appYellow))
            card_performance_work_status.setText(R.string.performance_activity_status_stopped)

            card_performance_first_button.setText(R.string.performance_activity_start)
            card_performance_first_button.setOnClickListener { startClickListener?.invoke(work) }
            card_performance_first_button.background = ContextCompat.getDrawable(context, R.drawable.yellow_btn)

            card_performance_second_button.setText(R.string.performance_activity_accept)
            card_performance_second_button.setOnClickListener { applyClickListener?.invoke(work) }
            card_performance_second_button.background = ContextCompat.getDrawable(context, R.drawable.green_btn)

            card_performance_menu.setOnClickListener {
                val popupMenu = PopupMenu(context, card_performance_menu)
                popupMenu.inflate(R.menu.stopped_work_menu)
                popupMenu.setForceShowIcon(true)

                popupMenu.setOnMenuItemClickListener { item ->
                    menuItemClickListener?.invoke(resolveMenuItemType(item), work)
                    true
                }
                popupMenu.show()
            }
            setProgressAndTime(work)
        }
    }
    //endregion

    //region DoneViewHolder
    /**
     * Холдер работ завершенных
     */
    inner class DoneViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindInternal(work: Work) {
            card_performance_status_view.setBackgroundColor(ContextCompat.getColor(context, R.color.appGreen))
            card_performance_work_status.setText(R.string.performance_activity_status_done)

            card_performance_first_button.setText(R.string.card_performance_apply)
            card_performance_first_button.setOnClickListener { applyClickListener?.invoke(work) }
            card_performance_first_button.background = ContextCompat.getDrawable(context, R.drawable.green_btn)

            card_performance_second_button.setText(R.string.card_performance_return)
            card_performance_second_button.setOnClickListener { returnClickListener?.invoke(work) }
            card_performance_second_button.background = ContextCompat.getDrawable(context, R.drawable.red_button)

            card_performance_menu.setOnClickListener {
                val popupMenu = PopupMenu(context, card_performance_menu)
                popupMenu.inflate(R.menu.finished_work_menu)
                popupMenu.setForceShowIcon(true)

                popupMenu.setOnMenuItemClickListener { item ->
                    menuItemClickListener?.invoke(resolveMenuItemType(item), work)
                    true
                }
                popupMenu.show()
            }
            setProgressAndTime(work)
        }
    }
    //endregion

    //region AcceptedMasterViewHolder
    /**
     * Холдер принятых работ
     */
    inner class AcceptedMasterViewHolder(itemView: View) : ViewHolder(itemView) {
        override fun bindInternal(work: Work) {
            card_performance_work_status.setText(R.string.performance_activity_status_accepted)

            card_performance_status_view.setBackgroundColor(ContextCompat.getColor(context, R.color.appTransparent))
            card_performance_card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.inactiveItem))
            card_performance_second_button.visibility = View.GONE
            card_performance_first_button.visibility = View.GONE


            card_performance_menu.setOnClickListener {
                val popupMenu = PopupMenu(context, card_performance_menu)
                popupMenu.inflate(R.menu.accepted_work_menu)
                popupMenu.setForceShowIcon(true)

                popupMenu.setOnMenuItemClickListener { item ->
                    menuItemClickListener?.invoke(resolveMenuItemType(item), work)
                    true
                }
                popupMenu.show()
            }
            setProgressAndTime(work)
        }
    }
    //endregion

    private companion object {
        private const val VIEW_TYPE_WORK_IN_APPROVE = 0
        private const val VIEW_TYPE_WORK_APPROVED = 1
        private const val VIEW_TYPE_WORK_IN_TASK = 2
        private const val VIEW_TYPE_WORK_IN_WORK = 3
        private const val VIEW_TYPE_WORK_PAUSED = 4
        private const val VIEW_TYPE_WORK_STOPPED = 5
        private const val VIEW_TYPE_WORK_CANCELED = 6
        private const val VIEW_TYPE_WORK_DONE = 7
        private const val VIEW_TYPE_WORK_ACCEPTED_MASTER = 8
        private const val PAYLOAD_UPDATE_TIME = "PAYLOAD_UPDATE_TIME"
    }
}