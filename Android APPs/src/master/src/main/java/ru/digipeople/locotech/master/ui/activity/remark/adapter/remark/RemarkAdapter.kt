package ru.digipeople.locotech.master.ui.activity.remark.adapter.remark

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.card_locomotive_details_remark.view.*
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Remark
import ru.digipeople.locotech.master.model.RemarkStatus
import ru.digipeople.locotech.master.ui.activity.popup.MenuItemType
import ru.digipeople.ui.adapter.BaseDataAdapter
import ru.digipeople.ui.widget.setForceShowIcon
import javax.inject.Inject

/**
 * Адаптер замечаний
 *
 * @author Kashonkov Nikita
 */
class RemarkAdapter @Inject constructor(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<RemarkAdapter.RemarkViewHolder>(), BaseDataAdapter<Remark> {

    //region Other
    var dataList: List<Remark> = listOf()
    private lateinit var currentRemark: Remark
    var menuItemClickListener: ((menuItemType: MenuItemType, remark: Remark) -> Unit)? = null
    var itemClickLocomotiveDetailsRemarkAdapter: ((remark: Remark) -> Unit)? = null
    //endregion

    /**
     * создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_locomotive_details_remark, parent, false)
        return RemarkViewHolder(view)
    }
    /**
     * Определение количесвта элементов
     */
    override fun getItemCount(): Int {
        return dataList.size
    }
    /**
     * привязка значений элементов списка к шаблону
     */
    override fun onBindViewHolder(p0: RemarkViewHolder, p1: Int) {
        val remark = dataList.get(p1)

        if (remark == currentRemark) {
            p0.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.appWhite))
        } else {
            p0.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.inactiveItem))
        }
        /**
         * Раскраска статуса замечания
         */
        p0.statusView.background = when (remark.status) {
            RemarkStatus.NO_WORKERS -> ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_red)
            RemarkStatus.NOT_SEND_TO_TECHNOLOGIST -> ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_yellow)
            RemarkStatus.APPROVED -> ContextCompat.getDrawable(context, R.drawable.ic_icon_check_circle_green)
        }

        p0.title.text = remark.title
        /**
         * обработка нажатия на попап меню
         */
        val menuBtn = p0.menu
        p0.menu.setOnClickListener {
            val popupMenu = PopupMenu(context, menuBtn)
            popupMenu.inflate(R.menu.remark_menu)
            popupMenu.setForceShowIcon(true)
            /**
             * управление пунктами меню
             */
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.comment -> menuItemClickListener?.invoke(MenuItemType.COMMENT, remark)
                    R.id.photo -> menuItemClickListener?.invoke(MenuItemType.PHOTO, remark)
                    R.id.delete -> menuItemClickListener?.invoke(MenuItemType.DELETE, remark)
                }
                true
            }
            popupMenu.show()

        }
        /**
         * управление видимостью автора замечания
         */
        if (!remark.author.isNullOrEmpty()) {
            p0.firstAuthor.text = remark.author
            p0.authorGroup.visibility = View.VISIBLE
        } else {
            p0.authorGroup.visibility = View.GONE
        }
        /**
         * управление видимостью комментария
         */
        if (remark.comment == null || remark.comment!!.text.isEmpty()) {
            p0.commentGroup.visibility = View.GONE
        } else {
            p0.commentGroup.visibility = View.VISIBLE
            p0.comment.text = remark.comment!!.text
        }
    }
    /**
     * установка данных
     */
    override fun setData(list: List<Remark>) {
        val diffUtilCallback = DiffUtilCallback(dataList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.dataList = list
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * выбор замечания
     */
    fun setCurrentRemark(remark: Remark) {
        currentRemark = remark
    }
    /**
     * Холдер замечания
     */
    inner class RemarkViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val cardView = view.card_locomotive_details_card
        val statusView = view.item_locomotive_details_remark_status_view
        val title = view.item_locomotive_details_remark_title
        val menu = view.item_locomotive_details_remark_menu
        val authorGroup = view.item_locomotive_details_remark_author_group
        val firstAuthor = view.item_locomotive_details_remark_author
        val comment = view.item_locomotive_details_remark_comment
        val commentGroup = view.item_locomotive_details_remark_comment_group

        init {
            /**
             * обработка нажатия на замечание
             */
            itemView.setOnClickListener {
                val remark = dataList.get(adapterPosition)
                itemClickLocomotiveDetailsRemarkAdapter?.invoke(remark)
                currentRemark = remark
                notifyDataSetChanged()
            }
        }
    }
}