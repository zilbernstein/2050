package ru.digipeople.locotech.master.ui.activity.groupassignment.adapter.brig

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import ru.digipeople.locotech.master.R
import ru.digipeople.utils.adapter.BaseDiffUtilCallback
import javax.inject.Inject

/**
 * Адаптер списка бригад и исполнителей
 *
 * @author Sostavkin Grisha
 */
class BrigAssignmentAdapter @Inject constructor(var context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    //region Other
    private var adapterData = BrigadeAdapterData()
    var itemBrigTitleOnClickListener: ((brigView: BrigView) -> Unit)? = null
    var itemWorkerOnClickListener: ((workerView: WorkerView) -> Unit)? = null
    //endregion
    /**
     * холдер
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            /**
             * выбор типа элемента
             * бригада
             */
            VIEW_TYPE_BRIGADE -> {
                val viewTitle = LayoutInflater.from(parent.context).inflate(R.layout.item_group_assignment_title, parent, false)
                BrigadeViewHolder(viewTitle)
            }/**
             * исполнитель
             */
            VIEW_TYPE_WORKER -> {
                val viewGroup = LayoutInflater.from(parent.context).inflate(R.layout.item_group_assignment_brig, parent, false)
                WorkerViewHolder(viewGroup)
            }
            /**
             * другие
             */
            else -> {
                val viewTitle = LayoutInflater.from(parent.context).inflate(R.layout.item_group_assignment_title, parent, false)
                BrigadeViewHolder(viewTitle)
            }
        }
    }
    /**
     * определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        if (adapterData.isBrigadeView(position)) {
            return VIEW_TYPE_BRIGADE
        } else if (adapterData.isWorkerView(position)) {
            return VIEW_TYPE_WORKER
        } else {
            throw IllegalArgumentException("Unsupported position = $position")
        }
    }
    /**
     * определение кол-ва элементов
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }
    /**
     * получение id элемента
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }
    /**
     * выбор холдра в зависимости от типа элемента
     */
    override fun onBindViewHolder(vh: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (adapterData.isBrigadeView(position)) {
            val holder = vh as BrigadeViewHolder
            holder.bind(adapterData.getBrigadeView(position))
        } else if (adapterData.isWorkerView(position)) {
            val holder = vh as WorkerViewHolder
            holder.bind(adapterData.getWorkerView(position))
        }
    }
    /**
     * установка данных
     */
    fun setData(data: BrigadeAdapterData) {
        val diffUtilCallback = DiffUtilCallback(adapterData, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.adapterData = data
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * Холдер исполнитедя
     */
    inner class WorkerViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val checkBox = view.findViewById<ImageView>(R.id.item_performer_group_check)
        private val name = view.findViewById<AppCompatTextView>(R.id.item_performer_name)
        private val percent = view.findViewById<AppCompatTextView>(R.id.item_performer_percent)
        private val progress = view.findViewById<ProgressBar>(R.id.progress)
        /**
         * заполнение элементов данными
         */
        fun bind(worker: WorkerView) {
            name.text = worker.worker.performer.name
            checkBox.isActivated = worker.worker.isChecked
            if (worker.worker.newLoadPercent == 0f) {
                val workLoad = Math.max(0, worker.worker.workLoad)
                progress.progress = workLoad
                percent.text = context.getString(R.string.group_assignment_percent, workLoad.toString())
                /**
                 * раскраска програсс в зависимости от процента загрузки исполнителя
                 */
                when {
                    /**
                     * зеленый
                     */
                    workLoad <= 100 -> {
                        progress.progressDrawable = ContextCompat.getDrawable(context, R.drawable.green_progress_bar_group_assignment)
                    }
                    /**
                     * желтый
                     */
                    workLoad in 101..149 -> progress.progressDrawable = ContextCompat.getDrawable(context, R.drawable.yellow_progress_bar_group_assignment)
                    /**
                     * красный
                     */
                    workLoad >= 150 -> {
                        progress.progressDrawable = ContextCompat.getDrawable(context, R.drawable.red_progress_bar_group_assignment)
                    }
                }
            } else {
                val newLoadPercent = Math.max(0f, worker.worker.newLoadPercent)
                progress.progress = newLoadPercent.toInt()
                percent.text = context.getString(R.string.group_assignment_percent, newLoadPercent.toInt().toString())
                /**
                 * раскраска прогресса в зависимости от расчетной нагрузки исполнителя
                 */
                when {
                    /**
                     * зеленый
                     */
                    newLoadPercent <= 100 -> {
                        progress.progressDrawable = ContextCompat.getDrawable(context, R.drawable.green_progress_bar_group_assignment)
                    }
                    /**
                     * желтый
                     */
                    newLoadPercent in 101f..149f -> progress.progressDrawable = ContextCompat.getDrawable(context, R.drawable.yellow_progress_bar_group_assignment)
                    /**
                     * красный
                     */
                    newLoadPercent >= 150 -> {
                        progress.progressDrawable = ContextCompat.getDrawable(context, R.drawable.red_progress_bar_group_assignment)
                    }
                }
            }

            itemView.setOnClickListener {
                itemWorkerOnClickListener?.invoke(worker)
            }
        }
    }
    /**
     * Холдер брригады
     */
    inner class BrigadeViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        private val title = view.findViewById<AppCompatTextView>(R.id.item_performer_name)
        private val brigCheck = view.findViewById<ImageView>(R.id.item_performer_group_check)
        /**
         * привязка данных к макету
         */
        fun bind(brigView: BrigView) {
            title.text = brigView.brigade.brigadeName
            brigCheck.isActivated = brigView.brigade.isSelected

            itemView.setOnClickListener {
                itemBrigTitleOnClickListener?.invoke(brigView)
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_BRIGADE = 0
        private const val VIEW_TYPE_WORKER = 1
    }
    /**
     * определение повторноиспользщуемых элементов чтобы их не перерисовывать
     */
    class DiffUtilCallback(oldItems: List<Any>, newItems: List<Any>) : BaseDiffUtilCallback<Any>(oldItems, newItems) {
        override fun areItemsTheSame(oldItem: Any?, newItem: Any?): Boolean {
            return when {
                oldItem is BrigView && newItem is BrigView -> oldItem.brigade.id == newItem.brigade.id &&
                        oldItem.brigade.isSelected == newItem.brigade.isSelected
                oldItem is WorkerView && newItem is WorkerView -> oldItem.worker.performer.id == newItem.worker.performer.id &&
                        oldItem.worker.isChecked == newItem.worker.isChecked
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Any?, newItem: Any?): Boolean = oldItem == newItem
    }
}