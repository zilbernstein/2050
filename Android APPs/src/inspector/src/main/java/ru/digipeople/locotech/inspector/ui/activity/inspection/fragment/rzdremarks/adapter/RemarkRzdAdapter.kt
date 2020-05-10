package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter

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
import ru.digipeople.locotech.inspector.model.Remark
import ru.digipeople.locotech.inspector.model.Work
import java.text.SimpleDateFormat
import javax.inject.Inject

/**
 * адаптер замечаний РЖД
 * @author Kashonkov Nikita
 */
class RemarkRzdAdapter @Inject constructor(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val DF = SimpleDateFormat("dd-MM-yyyy HH:mm")
    var adapterData = AdapterData()
    var acceptAllClickListener: ((remark: RemarkData, remarkPosition: Int) -> Unit)? = null
    var checkOtkClickListener: ((work: WorkData, workPosition: Int) -> Unit)? = null
    var checkRzdClickListener: ((work: WorkData, workPosition: Int) -> Unit)? = null
    var commentWorkClickListener: ((work: Work) -> Unit)? = null
    var photoClickListener: ((work: Work) -> Unit)? = null
    var commentRemarkClickListener: ((remark: Remark) -> Unit)? = null
    var photoRemarkClickListener: ((remark: Remark) -> Unit)? = null
    var deleteRemarkClickListener: ((remark: Remark) -> Unit)? = null
    /**
     * создание холдера в зависимости о типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            /**
             * замечание
             */
            VIEW_TYPE_REMARK -> {
                val viewSection = LayoutInflater.from(parent.context).inflate(R.layout.item_remark, parent, false)
                return RemarkViewHolder(viewSection)
            }
            /**
             * разделитель
             */
            VIEW_TYPE_WORK_SPLASH -> {
                val viewSplash = LayoutInflater.from(parent.context).inflate(R.layout.item_work_splash, parent, false)
                return WorkSplashViewHolder(viewSplash)
            }
            /**
             * работа
             */
            else -> {
                val viewWork = LayoutInflater.from(parent.context).inflate(R.layout.item_non_cyclic_work, parent, false)
                return WorkViewHolder(viewWork)
            }
        }
    }
    /**
     * получить кол-во элементов в списке
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }
    /**
     * определить тип элемента
     */
    override fun getItemViewType(position: Int): Int {
        if (adapterData.isRemark(position)) {
            return VIEW_TYPE_REMARK
        } else if (adapterData.isWork(position)) {
            return VIEW_TYPE_WORK
        } else if (adapterData.isWorkSplash(position)) {
            return VIEW_TYPE_WORK_SPLASH
        } else {
            throw IllegalArgumentException("Unsupported position = " + position)
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        if (adapterData.isRemark(position)) {
            val holder = vh as RemarkViewHolder
            holder.bind()
        } else if (adapterData.isWork(position)) {
            val holder = vh as WorkViewHolder
            holder.bind()
        }
    }
    /**
     * установить данные
     */
    fun setData(adapterData: AdapterData) {
        this.adapterData = adapterData
        notifyDataSetChanged()
    }
    /**
     * изменение замечания
     */
    fun remarkItemChanged(position: Int) {
        val remark = adapterData.getRemark(position)
        notifyItemChanged(position)
        for (n in position..position + remark.remark.works.size) {
            notifyItemChanged(n)
        }
    }

    fun workItemChanged(workData: WorkData, position: Int) {
        //Обновляем отображения работы
        notifyItemChanged(position)
        //Обновляем отображение замечания, в которе входи данная работа
        notifyItemChanged(position - workData.position - 1)
    }
    /**
     * Холдер замечания
     */
    inner class RemarkViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView
        private val info: TextView
        private val acceptBtn: Button
        private val commentIcon: ImageView
        private val photoIcon: ImageView
        private val photoCount: TextView
        private val deleteIcon: ImageView
        private val remarkMasterCheckCount: TextView
        private val remarkOtkCheckCount: TextView
        private val remarkRzdCheckCount: TextView

        init {
            title = view.findViewById(R.id.remark_title)
            info = view.findViewById(R.id.remark_info)
            acceptBtn = view.findViewById(R.id.accept_all_btn)
            remarkMasterCheckCount = view.findViewById(R.id.master_check)
            remarkOtkCheckCount = view.findViewById(R.id.otk_check)
            remarkRzdCheckCount = view.findViewById(R.id.rzd_check)

            commentIcon = view.findViewById(R.id.comment_icon)
            commentIcon.setOnClickListener { commentRemarkClickListener?.invoke(adapterData.getRemark(adapterPosition).remark) }

            photoIcon = view.findViewById(R.id.photo_icon)
            photoIcon.setOnClickListener { photoRemarkClickListener?.invoke(adapterData.getRemark(adapterPosition).remark) }

            photoCount = view.findViewById(R.id.photo_count)
            photoCount.setOnClickListener { photoRemarkClickListener?.invoke(adapterData.getRemark(adapterPosition).remark) }

            acceptBtn.setOnClickListener { acceptAllClickListener?.invoke(adapterData.getRemark(adapterPosition), adapterPosition) }

            deleteIcon = view.findViewById(R.id.delete_remark)
            deleteIcon.setOnClickListener { deleteRemarkClickListener?.invoke(adapterData.getRemark(adapterPosition).remark) }
        }
        /**
         * привязка данных к шаблону
         */
        fun bind() {
            val remarkData = adapterData.getRemark(adapterPosition)
            val remark = remarkData.remark

            title.text = remark.title
            info.text = context.getString(R.string.inspection_activity_remark_info, remark.works.size, remark.author, DF.format(remark.date))
            commentIcon.isActivated = remarkData.remark.comment.text.isNotEmpty()
            acceptBtn.isEnabled = remarkData.isAcceptBtnEnable

            setWorkCount(remarkMasterCheckCount, remark, remarkData.masterUnCheckCount)
            setWorkCount(remarkOtkCheckCount, remark, remarkData.sldUnCheckCount)
            setWorkCount(remarkRzdCheckCount, remark, remarkData.rzdUnCheckCount)

            photoCount.text = "${remark.photoAmount}"
            if (remarkData.isDeleteBtnEnable) {
                deleteIcon.visibility = View.VISIBLE
            } else {
                deleteIcon.visibility = View.GONE
            }
        }

        /**
         * Method which sets circle's color is up on state and works count in it
         */
        private fun setWorkCount(textView: TextView, remark: Remark, count: Int) {
            val noWorks = remark.works.isEmpty()
            if (noWorks) {
                textView.background = context.getDrawable(R.drawable.gray_round_frame)
                textView.text = ""
            } else if (count == 0) {
                textView.background = context.getDrawable(R.drawable.ic_icon_check_circle_green)
                textView.text = ""
            } else {
                textView.background = context.getDrawable(R.drawable.green_round_frame)
                textView.text = count.toString()
            }
        }
    }
    /**
     * холдер работы
     */
    inner class WorkViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val workTitle: TextView
        val workStatus: TextView
        val commentIcon: ImageView
        val photoIcon: ImageView
        val photoCount: TextView
        val masterCheck: ImageView
        val otkCheck: ImageView
        val rzdCheck: ImageView

        init {
            workTitle = view.findViewById(R.id.work_title)
            workStatus = view.findViewById(R.id.work_status)

            commentIcon = view.findViewById(R.id.comment_icon)
            commentIcon.setOnClickListener { commentWorkClickListener?.invoke(adapterData.getWorkData(adapterPosition).work) }

            photoCount = view.findViewById(R.id.photo_count)
            photoCount.setOnClickListener { photoClickListener?.invoke(adapterData.getWorkData(adapterPosition).work) }

            photoIcon = view.findViewById(R.id.photo_icon)
            photoIcon.setOnClickListener { photoClickListener?.invoke(adapterData.getWorkData(adapterPosition).work) }

            otkCheck = view.findViewById(R.id.otk_check)
            otkCheck.setOnClickListener { checkOtkClickListener?.invoke(adapterData.getWorkData(adapterPosition), adapterPosition) }

            rzdCheck = view.findViewById(R.id.rzd_check)
            rzdCheck.setOnClickListener { checkRzdClickListener?.invoke(adapterData.getWorkData(adapterPosition), adapterPosition) }

            masterCheck = view.findViewById(R.id.master_check)
        }
        /**
         * привязка данных к шаблону
         */
        fun bind() {
            val workData = adapterData.getWorkData(adapterPosition)
            workTitle.text = workData.work.title

            commentIcon.isActivated = workData.work.comment.text.isNotEmpty()

            when (workData.work.status) {
                /**
                 * Выполнена
                 */
                ru.digipeople.locotech.inspector.model.WorkStatus.DONE -> {
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
                 * принята мастером
                 */
                ru.digipeople.locotech.inspector.model.WorkStatus.ACCEPTED_MASTER -> {
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
                 * принята ОТК
                 */
                ru.digipeople.locotech.inspector.model.WorkStatus.ACCEPTED_SLD -> {
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
                 * принята РЖД
                 */
                ru.digipeople.locotech.inspector.model.WorkStatus.ACCEPTED_RZD -> {
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

                    otkCheck.isEnabled = workData.isOtkCheckEnable
                    otkCheck.isActivated = false

                    rzdCheck.isEnabled = workData.isRzdCheckEnable
                    rzdCheck.isActivated = false
                }
            }

            photoCount.text = "${workData.work.photosCount}"
        }
    }
    /**
     * Холдер разделителя
     */
    inner class WorkSplashViewHolder(private val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_REMARK = 0
        private const val VIEW_TYPE_WORK = 1
        private const val VIEW_TYPE_WORK_SPLASH = 2
    }
}