package ru.digipeople.locotech.master.ui.activity.remark.adapter.work

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_locomotive_details_work.view.*
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.model.WorkStatus
import ru.digipeople.locotech.master.ui.activity.popup.MenuItemType
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.ui.widget.setForceShowIcon
import javax.inject.Inject


/**
 * Адаптер работ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
class WorkAdapter @Inject constructor(val context: Context) : RecyclerView.Adapter<WorkAdapter.WorkViewHolder>() {

    //region Other
    var dataList: List<Work> = listOf()
    var menuItemClickListener: ((menuItemType: MenuItemType, work: Work) -> Unit)? = null
    //endregion
    /**
     * Холдер работы
     */
    class WorkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusView = view.item_locomotive_details_work_status_view
        val repeatsCount = view.repeats_count
        val title = view.item_locomotive_details_work_title
        val menu = view.item_locomotive_details_work_menu
        val performerGroup = view.item_locomotive_details_performer_group
        val firstPerformer = view.item_locomotive_details_first_performer
        val thirdPerformer = view.item_locomotive_details_third_performer
        val secondPerformer = view.item_locomotive_details_second_performer
        val otherPerformersAmount = view.item_locomotive_amount_of_other_performers
        val comment = view.item_locomotive_details_comment
        val commentGroup = view.item_locomotive_details_comment_group
    }
    /**
     * создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_locomotive_details_work, parent, false)
        return WorkViewHolder(view)
    }
    /**
     * получение числа элемепнтов
     */
    override fun getItemCount(): Int {
        return dataList.size
    }
    /**
     * привязка данных элементов к полям шаблона
     */
    override fun onBindViewHolder(vh: WorkViewHolder, position: Int) {
        val work = dataList.get(position)

        vh.repeatsCount.text = "${work.repeats}"
        /**
         * раскраска статуса
         */
        if (work.status == WorkStatus.NOT_APPROVED ||
                ((work.status == WorkStatus.APPROVED || work.status == WorkStatus.IN_APPROVE)
                        && work.performers.isEmpty())) {
            vh.statusView.background = ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_red)
        } else if (work.status == WorkStatus.APPROVED || work.status == WorkStatus.IN_APPROVE) {
            vh.statusView.background = ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_yellow)
        } else {
            vh.statusView.background = ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_green)
        }

        vh.title.text = work.title
        /**
         * обработка нажатия на попап меню
         */
        val menuBtn = vh.menu
        vh.menu.setOnClickListener {
            val popupMenu = PopupMenu(context, menuBtn)
            popupMenu.inflate(R.menu.locomotive_detail_item_menu)
            popupMenu.setForceShowIcon(true)

            if (work.status.code > WorkStatus.APPROVED.code) {
                popupMenu.menu.findItem(R.id.workaround).setVisible(false)
            }
            /**
             * обрабоока пунктов меню
             */
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> menuItemClickListener?.invoke(MenuItemType.DELETE, work)
                    R.id.comment -> menuItemClickListener?.invoke(MenuItemType.COMMENT, work)
                    R.id.tmc -> menuItemClickListener?.invoke(MenuItemType.TMC, work)
                    R.id.workaround -> menuItemClickListener?.invoke(MenuItemType.WORKAROUND, work)
                }

                true
            }
            popupMenu.show()

        }
        /**
         * управление видимостью блока исполнителей
         */
        if (work.performers.isEmpty()) {
            /**
             * нет исполнителей
             */
            vh.performerGroup.visibility = View.GONE
        } else {
            vh.performerGroup.visibility = View.VISIBLE
            /**
             * 1 исполниетель
             */
            if (work.performers.size == 1) {
                vh.firstPerformer.text = work.performers.get(0).name
                vh.firstPerformer.visibility = View.VISIBLE
                vh.secondPerformer.visibility = View.GONE
                vh.thirdPerformer.visibility = View.GONE
                vh.otherPerformersAmount.visibility = View.GONE
                /**
                 * 2 исполниетля
                 */
            } else if (work.performers.size == 2) {
                vh.firstPerformer.text = work.performers.get(0).name
                vh.secondPerformer.text = work.performers.get(1).name
                vh.firstPerformer.visibility = View.VISIBLE
                vh.secondPerformer.visibility = View.VISIBLE
                vh.thirdPerformer.visibility = View.GONE
                vh.otherPerformersAmount.visibility = View.GONE
                /**
                 * 3 исполниетля
                 */
            } else if (work.performers.size == 3) {
                vh.firstPerformer.text = work.performers.get(0).name
                vh.secondPerformer.text = work.performers.get(1).name
                vh.thirdPerformer.text = work.performers.get(2).name
                vh.firstPerformer.visibility = View.VISIBLE
                vh.secondPerformer.visibility = View.VISIBLE
                vh.thirdPerformer.visibility = View.VISIBLE
                vh.otherPerformersAmount.visibility = View.GONE
                /**
                 * больше 3 исполнителей
                 */
            } else {
                vh.firstPerformer.text = work.performers.get(0).name
                vh.secondPerformer.text = work.performers.get(1).name
                vh.thirdPerformer.text = work.performers.get(2).name
                vh.otherPerformersAmount.text = context.getString(R.string.performance_other_performers_amount, work.performers.size - 3)
                vh.firstPerformer.visibility = View.VISIBLE
                vh.secondPerformer.visibility = View.VISIBLE
                vh.thirdPerformer.visibility = View.VISIBLE
                vh.otherPerformersAmount.visibility = View.VISIBLE
            }
        }
        /**
         * управление видимостью комментариев
         */
        if (work.comment?.text.isNullOrBlank()) {
            vh.commentGroup.visibility = View.GONE
        } else {
            vh.commentGroup.visibility = View.VISIBLE
            vh.comment.text = work.comment!!.text
        }
    }
    /**
     * установка данных
     */
    fun setData(list: List<Work>) {
        val diffUtilCallback = DiffUtilCallback(dataList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.dataList = list
        diffResult.dispatchUpdatesTo(this)
    }
}