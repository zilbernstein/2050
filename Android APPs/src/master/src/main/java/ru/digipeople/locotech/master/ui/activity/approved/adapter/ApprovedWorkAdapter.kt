package ru.digipeople.locotech.master.ui.activity.approved.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.card_approved_work.view.*
import ru.digipeople.locotech.core.data.model.Performer
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.WorkStatus
import ru.digipeople.locotech.master.ui.activity.popup.MenuItemType
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.ui.widget.setForceShowIcon
import javax.inject.Inject

/**
 * Адаптер согласование
 *
 * @author Kashonkov Nikita
 */
class ApprovedWorkAdapter @Inject constructor() :
        BaseItemsRecyclerAdapter<Work, ApprovedWorkAdapter.ViewHolder>() {

    //region Other
    var menuItemClickListener: ((menuItemType: MenuItemType, work: Work) -> Unit)? = null
    //endregion
    /**
     * Операции при создании экрана
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.card_approved_work, parent, false)
        return ViewHolder(view)
    }
    /**
     * Операции при прикреплении экрана
     */
    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(items[position])
    }
    /**
     * Получение имени исполнителя
     */
    private fun getPerformerDisplayedName(performer: Performer): String {
        return performer.name
    }
    /**
     * Холдер работ
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val statusView = view.card_approved_work_status_view
        val title = view.card_approved_work_title
        val menu = view.card_approved_work_menu
        val performerGroup = view.card_approved_performer_group
        val firstPerformer = view.card_approved_first_performer
        val thirdPerformer = view.card_approved_third_performer
        val secondPerformer = view.card_approved_second_performer
        val amountOfOtherPerformers = view.card_approved_amount_of_other_performers
        val comment = view.card_approved_comment
        val commentGroup = view.card_approved_comment_group

        fun bind(work: Work) {
            /**
             * Раскраска в зависимости от статуса
             */
            when (work.status) {
                WorkStatus.APPROVED -> statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.appGreen))
                WorkStatus.IN_APPROVE -> statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.appGray))
                WorkStatus.NOT_APPROVED -> {
                    if (work.performers.size > 0) {
                        statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.appYellow))
                    } else {
                        statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.appRed))
                    }
                }
            }

            title.text = work.title

            val menuBtn = menu
            /**
             * Подключение меню
             */
            menu.setOnClickListener {
                val popupMenu = PopupMenu(context, menuBtn)
                popupMenu.inflate(R.menu.approved_item_menu)
                popupMenu.setForceShowIcon(true)
                /**
                 * в зависимости от статуса работы отображаем или нет пункт меню обходное регение
                 */
                if (work.status.code > WorkStatus.APPROVED.code) {
                    popupMenu.menu.findItem(R.id.workaround).setVisible(false)
                }
                /**
                 * обработка выбора пункта меню
                 */
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.comment -> menuItemClickListener?.invoke(MenuItemType.COMMENT, work)
                        R.id.tmc -> menuItemClickListener?.invoke(MenuItemType.TMC, work)
                        R.id.workaround -> menuItemClickListener?.invoke(MenuItemType.WORKAROUND, work)
                    }
                    true
                }
                popupMenu.show()
            }

            //todo привести в надлежащий вид
            /**
             * управление видимостью исполнителей в зависимости от их числа
             */
            when (work.performers.size) {
                /**
                 * нет исполнителя
                 */
                0 -> performerGroup.visibility = View.GONE
                /**
                 * 1 исполнитель
                 */
                1 -> {
                    firstPerformer.text = getPerformerDisplayedName(work.performers.get(0))
                    firstPerformer.visibility = View.VISIBLE
                    secondPerformer.visibility = View.GONE
                    thirdPerformer.visibility = View.GONE
                    amountOfOtherPerformers.visibility = View.GONE
                }
                /**
                 * 2 исполнителя
                 */
                2 -> {
                    firstPerformer.text = getPerformerDisplayedName(work.performers.get(0))
                    secondPerformer.text = getPerformerDisplayedName(work.performers.get(1))
                    firstPerformer.visibility = View.VISIBLE
                    secondPerformer.visibility = View.VISIBLE
                    thirdPerformer.visibility = View.GONE
                    amountOfOtherPerformers.visibility = View.GONE
                }
                /**
                 * 3 исполнителя
                 */
                3 -> {
                    firstPerformer.text = getPerformerDisplayedName(work.performers.get(0))
                    secondPerformer.text = getPerformerDisplayedName(work.performers.get(1))
                    thirdPerformer.text = getPerformerDisplayedName(work.performers.get(2))
                    firstPerformer.visibility = View.VISIBLE
                    secondPerformer.visibility = View.VISIBLE
                    thirdPerformer.visibility = View.VISIBLE
                    amountOfOtherPerformers.visibility = View.GONE
                }
                /**
                 * больше 3 исполнителей
                 */
                else -> {
                    firstPerformer.text = work.performers.get(0).name
                    secondPerformer.text = work.performers.get(1).name
                    thirdPerformer.text = work.performers.get(2).name
                    amountOfOtherPerformers.text = context.getString(R.string.performance_other_performers_amount, work.performers.size - 3)
                    firstPerformer.visibility = View.VISIBLE
                    secondPerformer.visibility = View.VISIBLE
                    thirdPerformer.visibility = View.VISIBLE
                    amountOfOtherPerformers.visibility = View.VISIBLE
                }
            }
            /**
             * управление отображением комментария
             */
            if (work.comment != null && work.comment!!.text.isNotEmpty()) {
                commentGroup.visibility = View.VISIBLE
                comment.text = work.comment!!.text
            } else {
                commentGroup.visibility = View.GONE
            }
        }
    }
}