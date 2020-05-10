package ru.digipeople.locotech.worker.ui.activity.mytask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.card_equipment.view.*
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.Section
import ru.digipeople.locotech.worker.model.Work
import ru.digipeople.locotech.worker.model.WorkStatus
import javax.inject.Inject

/**
 * Адаптер для работы
 *
 * @author Kashonkov Nikita
 */
class TaskSectionAdapter @Inject constructor(val context: Context) :
        androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    //region Other
    var adapterData = AdapterData()
    var taskClickListener: ((work: Work) -> Unit)? = null
    //endRegion
    /**
     * Создание холдера в зависимости от типа элемента
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (p1) {
            /**
             * Секция
             */
            VIEW_TYPE_SECTION -> {
                val viewSection = LayoutInflater.from(p0.context).inflate(R.layout.item_section, p0, false)
                return SectionViewHolder(viewSection)
            }
            /**
             * Разделитель
             */
            VIEW_TYPE_DIVIDER -> {
                val viewDivider = LayoutInflater.from(p0.context).inflate(R.layout.recycler_divider, p0, false)
                return DividerViewHolder(viewDivider)
            }
            /**
             * Работа
             */
            VIEW_TYPE_WORK -> {
                val viewWork = LayoutInflater.from(p0.context).inflate(R.layout.item_task, p0, false)
                return WorkViewHolder(viewWork)
            }
            /**
             * Оборудование
             */
            VIEW_TYPE_EQUIPMENT -> {
                val view = LayoutInflater.from(p0.context).inflate(R.layout.card_equipment, p0, false)
                return EquipmentViewHolder(view)
            }
            else -> {
                val viewTitle = LayoutInflater.from(p0.context).inflate(R.layout.item_title, p0, false)
                return TitleItemViewHolder(viewTitle)
            }
        }
    }
    /**
     * Подсчет элементов
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }
    /**
     * Определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        if (adapterData.isSection(position)) {
            return VIEW_TYPE_SECTION
        } else if (adapterData.isWork(position)) {
            return VIEW_TYPE_WORK
        } else if (adapterData.isDividerView(position)) {
            return VIEW_TYPE_DIVIDER
        } else if (adapterData.isTitle(position)) {
            return VIEW_TYPE_TITLE
        } else if (adapterData.isEquipment(position)) {
            return VIEW_TYPE_EQUIPMENT
        } else {
            throw IllegalArgumentException("Unsupported position = " + position)
        }
    }

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        if (adapterData.isSection(p1)) {
            val holder = p0 as SectionViewHolder
            holder.bind(adapterData.getSection(p1))
        } else if (adapterData.isWork(p1)) {
            val holder = p0 as WorkViewHolder
            holder.bind(adapterData.getWork(p1))
        } else if (adapterData.isEquipment(p1)) {
            val holder = p0 as EquipmentViewHolder
            holder.bind()
        } else if (adapterData.isTitle(p1)) {
            val holder = p0 as TitleItemViewHolder
            holder.bind()
        }
    }
    /**
     * Установка данных
     */
    fun setData(adapterData: AdapterData) {
        val diffUtilCallback = DiffUtilCallback(this.adapterData, adapterData)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.adapterData = adapterData
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * Холддер секции
     */
    inner class SectionViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val title: TextView

        init {
            title = view.findViewById(R.id.item_section_title)
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(section: Section) {
            title.text = section.sectionName
        }
    }
    /**
     * Холдер работы
     */
    inner class WorkViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val title: TextView
        val status: ImageView
        val repeatsCount: TextView
        val workersList: TextView
        val workersCount: TextView

        init {
            title = view.findViewById(R.id.item_my_tasks_title)
            status = view.findViewById(R.id.item_task_status)
            repeatsCount = view.findViewById(R.id.repeats_count)
            workersList = view.findViewById(R.id.workers)
            workersCount = view.findViewById(R.id.workers_count)


            itemView.setOnClickListener { taskClickListener?.invoke(adapterData.getWork(adapterPosition)) }
        }
        /**
         * Привязка данных к работе
         */
        fun bind(work: Work) {
            title.text = work.workName
            repeatsCount.text = "${work.repeats}"
            val workersNames = work.workers.joinToString(separator = "   ") { it.name }
            workersList.text = workersNames
            workersCount.text = work.workers.size.toString()

            /**
             * Отрисовка иконок по статусам работы
             */
            when (work.workStatus) {
                WorkStatus.IN_WORK -> status.setImageDrawable(
                        ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_icon_in_work
                        )
                )
                WorkStatus.IN_TASK -> status.setImageDrawable(
                        ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_icon_new_work
                        )
                )
                WorkStatus.PAUSED -> status.setImageDrawable(
                        ContextCompat.getDrawable(
                                context,
                                R.drawable.icon_paused_work
                        )
                )
            }
        }

    }
    /**
     * Холдер разделителя
     */
    inner class DividerViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
    /**
     * Холддер оборудования
     */
    inner class EquipmentViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val number = view.card_equipment_number
        val progressBar = view.card_equipment_progress_bar
        /**
         * Привязка данных к шаблону
         */
        fun bind() {
            val equipment = adapterData.getEquipment(adapterPosition)
            number.text = equipment.equipmentName
            /**
             * Раскраска прогрессбара от процента
             */
            when (equipment.equipmentProgress) {
                in 0..90 -> {
                    progressBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.green_progress_bar_drawable)
                }
                in 90..94 -> {
                    progressBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.yellow_progress_bar_drawable)
                }
                else -> {
                    progressBar.progressDrawable = ContextCompat.getDrawable(context, R.drawable.red_progress_bar_drawable)
                }
            }

            progressBar.setProgress(equipment.equipmentProgress)
        }
    }
    /**
     * Холдер заголовка
     */
    inner class TitleItemViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val title: TextView

        init {
            title = view.findViewById(R.id.item_title)
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind() {
            val titleItem = adapterData.getTitleItem(adapterPosition)
            title.text = titleItem.title
        }
    }

    companion object {
        private const val VIEW_TYPE_SECTION = 0
        private const val VIEW_TYPE_WORK = 1
        private const val VIEW_TYPE_DIVIDER = 2
        private const val VIEW_TYPE_TITLE = 3
        private const val VIEW_TYPE_EQUIPMENT = 4
    }
}