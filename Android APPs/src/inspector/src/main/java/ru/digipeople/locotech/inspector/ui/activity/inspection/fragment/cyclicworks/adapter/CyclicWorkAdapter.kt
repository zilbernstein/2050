package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.model.Work
import ru.digipeople.locotech.inspector.model.WorkStatus
import ru.digipeople.locotech.inspector.ui.helper.ClientProvider
import javax.inject.Inject

/**
 * Адаптер цикловых работ
 *
 * @author Kashonkov Nikita
 */
class CyclicWorkAdapter @Inject constructor(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapterData = AdapterData()
    var acceptAllClickListener: ((remark: CyclicGroupData, remarkPosition: Int) -> Unit)? = null
    var checkOtkClickListener: ((work: WorkData, workPosition: Int) -> Unit)? = null
    var checkRzdClickListener: ((work: WorkData, workPosition: Int) -> Unit)? = null
    var controlPointClickListener: ((work: Work) -> Unit)? = null
    var commentClickListener: ((work: Work) -> Unit)? = null
    var photoClickListener: ((work: Work) -> Unit)? = null
    var measurementClickListener: ((work: Work) -> Unit)? = null
    /**
     * Создане холдера в зависимости от типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            /**
             * группа работ
             */
            VIEW_TYPE_CYCLIC_REMARK -> {
                val viewSection = LayoutInflater.from(parent.context).inflate(R.layout.item_cyclic_remark, parent, false)
                GroupViewHolder(viewSection)
            }
            /**
             * разделитель
             */
            VIEW_TYPE_WORK_SPLASH -> {
                val viewSplash = LayoutInflater.from(parent.context).inflate(R.layout.item_work_splash, parent, false)
                WorkSplashViewHolder(viewSplash)
            }
            /**
             * работа
             */
            else -> {
                val viewWork = LayoutInflater.from(parent.context).inflate(R.layout.item_work, parent, false)
                WorkViewHolder(viewWork)
            }
        }
    }
    /**
     * получить число элементов
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }
    /**
     * определени типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        if (adapterData.isCyclicGroup(position)) {
            return VIEW_TYPE_CYCLIC_REMARK
        } else if (adapterData.isWork(position)) {
            return VIEW_TYPE_WORK
        } else if (adapterData.isWorkSplash(position)) {
            return VIEW_TYPE_WORK_SPLASH
        } else {
            throw IllegalArgumentException("Unsupported position = " + position)
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        if (adapterData.isCyclicGroup(position)) {
            val holder = vh as GroupViewHolder
            holder.bind()
        } else if (adapterData.isWork(position)) {
            val holder = vh as WorkViewHolder
            holder.bind()
        }
    }
    /**
     * Установка данных
     */
    fun setData(adapterData: AdapterData) {
        this.adapterData = adapterData
        notifyDataSetChanged()
    }
    /**
     * Изменение элемента замечаия
     */
    fun remarkItemChanged(position: Int) {
        val cyclicGroup = adapterData.getCyclicGroup(position)
        notifyItemChanged(position)
        for (n in position..position + cyclicGroup.cyclicGroup.works.size) {
            notifyItemChanged(n)
        }
    }
    /**
     * изменение элемента работы
     */
    fun workItemChanged(workData: WorkData, position: Int) {
        //Обновляем отображения работы
        notifyItemChanged(position)
        //Обновляем отображение замечания, в которе входи данная работа
        notifyItemChanged(position - workData.position - 1)
    }
    /**
     * Холдер группы работ
     */
    inner class GroupViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.remark_title)
        private val workCount: TextView = view.findViewById(R.id.remark_count)
        private val acceptBtn: Button = view.findViewById(R.id.accept_all_btn)
        private val masterCheckCount: TextView = view.findViewById(R.id.master_check)
        private val remarkOtkCheckCount: TextView = view.findViewById(R.id.otk_check)
        private val remarkRzdCheckCount: TextView = view.findViewById(R.id.rzd_check)

        init {
            acceptBtn.setOnClickListener {
                acceptAllClickListener?.invoke(adapterData.getCyclicGroup(adapterPosition), adapterPosition)
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind() {
            val cyclicGroup = adapterData.getCyclicGroup(adapterPosition)

            title.text = cyclicGroup.cyclicGroup.groupName
            workCount.text = context.getString(R.string.cyclic_work_fragment_work_count, cyclicGroup.cyclicGroup.works.size)
            acceptBtn.isEnabled = cyclicGroup.isAcceptBtnEnable

            setCount(masterCheckCount, cyclicGroup.masterUnCheckCount)
            setCount(remarkOtkCheckCount, cyclicGroup.sldUnCheckCount)
            setCount(remarkRzdCheckCount, cyclicGroup.rzdUnCheckCount)
        }
        /**
         * Установка количества
         */
        private fun setCount(textView: TextView, count: Int) {
            if (count == 0) {
                textView.background = context.getDrawable(R.drawable.ic_icon_check_circle_green)
                textView.text = ""
            } else {
                textView.background = context.getDrawable(R.drawable.green_round_frame)
                textView.text = count.toString()
            }
        }
    }
    /**
     * Холдер работы
     */
    inner class WorkViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val workTitle: TextView = view.findViewById(R.id.work_title)
        private val workStatus: TextView = view.findViewById(R.id.work_status)
        private val controlPointIcon: ImageView = view.findViewById(R.id.control_point_icon)
        private val measurementsIcon: ImageView = view.findViewById(R.id.measurements_icon)
        private val commentIcon: ImageView
        private val photoIcon: ImageView
        private val photoCount: TextView
        private val masterCheck: ImageView
        private val otkCheck: ImageView
        private val rzdCheck: ImageView

        init {
            val work = { adapterData.getWorkData(adapterPosition).work }

            controlPointIcon.setOnClickListener { controlPointClickListener?.invoke(work()) }
            measurementsIcon.setOnClickListener { measurementClickListener?.invoke(work()) }
            commentIcon = view.findViewById(R.id.comment_icon)
            commentIcon.setOnClickListener { commentClickListener?.invoke(work()) }
            photoIcon = view.findViewById(R.id.photo_icon)
            photoIcon.setOnClickListener { photoClickListener?.invoke(work()) }
            photoCount = view.findViewById(R.id.photo_count)
            photoCount.setOnClickListener { photoClickListener?.invoke(work()) }

            otkCheck = view.findViewById(R.id.otk_check)
            otkCheck.setOnClickListener {
                checkOtkClickListener?.invoke(adapterData.getWorkData(adapterPosition), adapterPosition)
            }

            rzdCheck = view.findViewById(R.id.rzd_check)
            rzdCheck.setOnClickListener {
                checkRzdClickListener?.invoke(adapterData.getWorkData(adapterPosition), adapterPosition)
            }

            masterCheck = view.findViewById(R.id.master_check)
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind() {
            val workData = adapterData.getWorkData(adapterPosition)
            workTitle.text = workData.work.title

            commentIcon.isActivated = workData.work.comment.text.isNotEmpty()

            when (workData.work.status) {
                /**
                 * Выполнена
                 */
                WorkStatus.DONE -> {
                    workStatus.setText(R.string.work_status_done)
                    workStatus.setTextColor(ContextCompat.getColor(context, R.color.greenText))

                    masterCheck.isEnabled = false
                    masterCheck.isActivated = false

                    otkCheck.isEnabled = false
                    otkCheck.isActivated = false

                    rzdCheck.isEnabled = false
                    rzdCheck.isActivated = false
                }
                /**
                 * Принята мастером
                 */
                WorkStatus.ACCEPTED_MASTER -> {
                    workStatus.setText(R.string.work_status_master_accepted)
                    workStatus.setTextColor(ContextCompat.getColor(context, R.color.greenText))

                    masterCheck.isEnabled = false
                    masterCheck.isActivated = true

                    otkCheck.isEnabled = workData.isOtkCheckEnable
                    otkCheck.isActivated = false

                    rzdCheck.isEnabled = workData.isRzdCheckEnable
                    rzdCheck.isActivated = false
                }
                /**
                 * Приянта ОТК
                 */
                WorkStatus.ACCEPTED_SLD -> {
                    workStatus.setText(R.string.work_status_sld_accepted)
                    workStatus.setTextColor(ContextCompat.getColor(context, R.color.greenText))

                    masterCheck.isEnabled = false
                    masterCheck.isActivated = true

                    otkCheck.isEnabled = workData.isOtkCheckEnable
                    otkCheck.isActivated = true

                    rzdCheck.isEnabled = workData.isRzdCheckEnable
                    rzdCheck.isActivated = false
                }
                /**
                 * Принята РЖД
                 */
                WorkStatus.ACCEPTED_RZD -> {
                    workStatus.setText(R.string.work_status_rzd_accepted)
                    workStatus.setTextColor(ContextCompat.getColor(context, R.color.greenText))

                    masterCheck.isEnabled = false
                    masterCheck.isActivated = true

                    otkCheck.isEnabled = workData.isOtkCheckEnable
                    otkCheck.isActivated = true

                    rzdCheck.isEnabled = workData.isRzdCheckEnable
                    rzdCheck.isActivated = true
                }
                /**
                 * Выполняется
                 */
                else -> {
                    workStatus.setText(R.string.work_status_in_work)
                    workStatus.setTextColor(ContextCompat.getColor(context, R.color.blueText))

                    masterCheck.isEnabled = false
                    masterCheck.isActivated = false

                    otkCheck.isEnabled = false
                    otkCheck.isActivated = false

                    rzdCheck.isEnabled = false
                    rzdCheck.isActivated = false
                }
            }

            if (workData.work.hasCheckpoints) {
                controlPointIcon.visibility = View.VISIBLE
            } else {
                controlPointIcon.visibility = View.GONE
            }

            photoCount.text = "${workData.work.photosCount}"
        }
    }
    /**
     * Холдер разделителя
     */
    inner class WorkSplashViewHolder(private val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_CYCLIC_REMARK = 0
        private const val VIEW_TYPE_WORK = 1
        private const val VIEW_TYPE_WORK_SPLASH = 2
    }
}