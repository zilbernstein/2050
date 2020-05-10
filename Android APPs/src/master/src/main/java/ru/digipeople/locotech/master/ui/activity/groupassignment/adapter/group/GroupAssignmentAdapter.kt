package ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.group

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import ru.digipeople.locotech.master.R
import ru.digipeople.utils.DateUtils
import ru.digipeople.utils.adapter.BaseDiffUtilCallback
import javax.inject.Inject

/**
 * Адаптер списка работ для группового назначения
 *
 * @author Sostavkin Grisha
 */
class GroupAssignmentAdapter @Inject constructor(var context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    //region Other
    private var adapterData = GroupAdapterData()
    var itemGroupCheckClickListener: ((groupView: GroupView) -> Unit)? = null
    var itemCheckClickListener: ((workView: WorkView) -> Unit)? = null
    var currentGroupView: GroupView? = null
    //endregion
    /**
     * создание холдера группы
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        /**
         * определение типа элемента
         */
        return when (viewType) {
            /**
             * заголовок
             */
            VIEW_TYPE_TITLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_assignment_title, parent, false)
                TitleViewHolder(view)
            }
            /**
             * группа
             */
            VIEW_TYPE_GROUP -> {
                val viewGroup = LayoutInflater.from(parent.context).inflate(R.layout.item_group_assignment_work, parent, false)
                GroupViewHolder(viewGroup)
            }
            /**
             * другой
             */
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group_assignment_title, parent, false)
                TitleViewHolder(view)
            }
        }
    }
    /**
     * определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            adapterData.isTitleView(position) -> VIEW_TYPE_TITLE
            adapterData.isGroupView(position) -> VIEW_TYPE_GROUP
            else -> throw IllegalArgumentException("Unsupported position = $position")
        }
    }
    /**
     * получение количества элементов
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }
    /**
     * получение ID элемента
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    /**
     * выбор холдера в зависимости от типа элемента
     */
    override fun onBindViewHolder(vh: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (adapterData.isTitleView(position)) {
            val holder = vh as TitleViewHolder
            holder.bind(adapterData.getTitleView(position))
        } else if (adapterData.isGroupView(position)) {
            val holder = vh as GroupViewHolder
            holder.bind(adapterData.getGroupView(position))
        }
    }
    /**
     * заполнение адаптера данными
     */
    fun setData(data: GroupAdapterData) {
        val diffUtilCallback = DiffUtilCallback(adapterData, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.adapterData = data
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * Холдер группы
     */
    inner class GroupViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val checkBox = view.findViewById<ImageView>(R.id.item_group_assignment_check)
        private val name = view.findViewById<AppCompatTextView>(R.id.item_group_assignment_name)
        private val repeat = view.findViewById<TextView>(R.id.item_group_assignment_repeat_count)
        private val timeNorm = view.findViewById<TextView>(R.id.item_group_assignment_time_norm)
        /**
         * отображение данных в шаблоне
         */
        fun bind(workView: WorkView) {
            checkBox.isActivated = workView.workForWorker.isSelected
            name.text = workView.workForWorker.workName
            repeat.text = workView.workForWorker.repeat.toString()

            val formattedTimeNorm = DateUtils.convertToString(workView.workForWorker.timeLimit)
            timeNorm.text = itemView.context.getString(R.string.group_assignment_time_norm, formattedTimeNorm)

            itemView.setOnClickListener {
                itemCheckClickListener?.invoke(workView)
            }
        }
    }
    /**
     * Холдер заголовка
     */
    inner class TitleViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<AppCompatTextView>(R.id.item_performer_name)
        private val groupCheck = view.findViewById<ImageView>(R.id.item_performer_group_check)
        /**
         * отображение данных в шаблоне
         */
        fun bind(groupView: GroupView) {
            title.text = groupView.group.workGroupName

            groupCheck.isActivated = groupView.group.isSelected

            currentGroupView = groupView

            itemView.setOnClickListener {
                itemGroupCheckClickListener?.invoke(groupView)
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_TITLE = 0
        private const val VIEW_TYPE_GROUP = 1
    }
    /**
     * проверка повторояющихся данных при перерисовке
     */
    class DiffUtilCallback(oldItems: List<Any>, newItems: List<Any>) : BaseDiffUtilCallback<Any>(oldItems, newItems) {
        override fun areItemsTheSame(oldItem: Any?, newItem: Any?): Boolean {
            return when {
                oldItem is WorkView && newItem is WorkView -> oldItem.workForWorker.id == newItem.workForWorker.id &&
                        oldItem.workForWorker.isSelected == newItem.workForWorker.isSelected
                oldItem is GroupView && newItem is GroupView -> oldItem.group.id == newItem.group.id &&
                        oldItem.group.isSelected == newItem.group.isSelected
                else -> false
            }
        }
        /**
         * сравнение двух элементов
         */
        override fun areContentsTheSame(oldItem: Any?, newItem: Any?): Boolean = oldItem == newItem
    }
}