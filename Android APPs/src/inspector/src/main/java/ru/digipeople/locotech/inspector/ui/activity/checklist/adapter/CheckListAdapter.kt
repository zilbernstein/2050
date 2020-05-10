package ru.digipeople.locotech.inspector.ui.activity.checklist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.model.ControlServiceOperation
import ru.digipeople.locotech.inspector.model.WorkStatus
import javax.inject.Inject

/**
 * Адаптер для чеклиста
 *
 * @author Kashonkov Nikita
 */
class CheckListAdapter @Inject constructor(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapterData = AdapterData()
    var checkOtkClickListener: ((operation: OperationData, workPosition: Int) -> Unit)? = null
    var checkRzdClickListener: ((operation: OperationData, workPosition: Int) -> Unit)? = null
    var commentClickListener: ((operation: ControlServiceOperation) -> Unit)? = null
    var photoClickListener: ((operation: ControlServiceOperation) -> Unit)? = null
    /**
     * созданеи холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            /**
             * оборудование
             */
            VIEW_TYPE_EQUIPMENT -> {
                val viewSection = LayoutInflater.from(parent.context).inflate(R.layout.item_equipment_remark, parent, false)
                return RemarkViewHolder(viewSection)
            }
            /**
             * секция
             */
            else -> {
                val viewWork = LayoutInflater.from(parent.context).inflate(R.layout.item_check_list_work, parent, false)
                return WorkViewHolder(viewWork)
            }
        }
    }
    /**
     * получить число элементов в списке
     */
    override fun getItemCount(): Int {
        return adapterData.size
    }
    /**
     * определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        if (adapterData.isEquipmentCsoData(position)) {
            return VIEW_TYPE_EQUIPMENT
        } else if (adapterData.isOperation(position)) {
            return VIEW_TYPE_OPERATION
        } else {
            throw IllegalArgumentException("Unsupported position = " + position)
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        if (adapterData.isEquipmentCsoData(position)) {
            val holder = vh as RemarkViewHolder
            holder.bind()
        } else if (adapterData.isOperation(position)) {
            val holder = vh as WorkViewHolder
            holder.bind()
        }
    }
    /**
     * установка данных
     */
    fun setData(adapterData: AdapterData) {
        this.adapterData = adapterData
        notifyDataSetChanged()
    }

    fun workItemChanged(operationData: OperationData, position: Int) {
        //Обновляем отображения работы
        notifyItemChanged(position)
        //Обновляем отображение замечания, в которе входи данная работа
        notifyItemChanged(position - operationData.position)
    }
    /**
     * Холдер замечания
     */
    inner class RemarkViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView
        private val masterCheckCount: TextView
        private val remarkOtkCheckCount: TextView
        private val remarkRzdCheckCount: TextView

        init {
            title = view.findViewById(R.id.remark_title)
            masterCheckCount = view.findViewById(R.id.master_check)
            remarkOtkCheckCount = view.findViewById(R.id.otk_check)
            remarkRzdCheckCount = view.findViewById(R.id.rzd_check)
        }
        /**
         * привязка данных к шаблону
         */
        fun bind() {
            val equipmentData = adapterData.getEquipmentCsoData(adapterPosition)

            title.text = equipmentData.equipment.title

            setCount(masterCheckCount, equipmentData.masterUnCheckCount)
            setCount(remarkOtkCheckCount, equipmentData.sldUnCheckCount)
            setCount(remarkRzdCheckCount, equipmentData.rzdUnCheckCount)
        }
        /**
         * установка количества
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
        val workNormal: TextView

        init {
            workTitle = view.findViewById(R.id.work_title)
            workStatus = view.findViewById(R.id.work_status)
            workNormal = view.findViewById(R.id.work_normal)

            commentIcon = view.findViewById(R.id.comment_icon)
            commentIcon.setOnClickListener { commentClickListener?.invoke(adapterData.getOperationData(adapterPosition).operation) }

            photoCount = view.findViewById(R.id.photo_count)
            photoCount.setOnClickListener { photoClickListener?.invoke(adapterData.getOperationData(adapterPosition).operation) }

            photoIcon = view.findViewById(R.id.photo_icon)
            photoIcon.setOnClickListener { photoClickListener?.invoke(adapterData.getOperationData(adapterPosition).operation) }

            otkCheck = view.findViewById(R.id.otk_check)
            otkCheck.setOnClickListener { checkOtkClickListener?.invoke(adapterData.getOperationData(adapterPosition), adapterPosition) }

            rzdCheck = view.findViewById(R.id.rzd_check)
            rzdCheck.setOnClickListener { checkRzdClickListener?.invoke(adapterData.getOperationData(adapterPosition), adapterPosition) }

            masterCheck = view.findViewById(R.id.master_check)
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind() {
            val operationData = adapterData.getOperationData(adapterPosition)

            workTitle.text = operationData.operation.title
            workNormal.text = context.getString(R.string.check_list_activity_rate_value, operationData.operation.rateValue)

            commentIcon.isActivated = operationData.operation.comment.text.isNotEmpty()

            when (operationData.operation.status) {
                /**
                 * статус работы - выполнена
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
                 * статус работы - принята мастером
                 */
                WorkStatus.ACCEPTED_MASTER -> {
                    workStatus.setText(R.string.work_status_master_accepted)
                    workStatus.setTextColor(ContextCompat.getColor(context, R.color.greenText))

                    masterCheck.isEnabled = false
                    masterCheck.isActivated = true

                    otkCheck.isEnabled = operationData.isOtkCheckEnable
                    otkCheck.isActivated = false

                    rzdCheck.isEnabled = operationData.isRzdCheckEnable
                    rzdCheck.isActivated = false
                }
                /**
                 * статус работы - принята отк
                 */
                WorkStatus.ACCEPTED_SLD -> {
                    workStatus.setText(R.string.work_status_sld_accepted)
                    workStatus.setTextColor(ContextCompat.getColor(context, R.color.greenText))

                    masterCheck.isEnabled = false
                    masterCheck.isActivated = true

                    otkCheck.isEnabled = operationData.isOtkCheckEnable
                    otkCheck.isActivated = true

                    rzdCheck.isEnabled = operationData.isRzdCheckEnable
                    rzdCheck.isActivated = false
                }
                /**
                 * статус работы - принята ржд
                 */
                WorkStatus.ACCEPTED_RZD -> {
                    workStatus.setText(R.string.work_status_rzd_accepted)
                    workStatus.setTextColor(ContextCompat.getColor(context, R.color.greenText))

                    masterCheck.isEnabled = false
                    masterCheck.isActivated = true

                    otkCheck.isEnabled = operationData.isOtkCheckEnable
                    otkCheck.isActivated = true

                    rzdCheck.isEnabled = operationData.isRzdCheckEnable
                    rzdCheck.isActivated = true
                }
                else -> {
                    /**
                     * статус работы выполняется
                     */
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

            photoCount.text = "${operationData.operation.photoAmount}"
        }
    }

    companion object {
        private const val VIEW_TYPE_EQUIPMENT = 0
        private const val VIEW_TYPE_OPERATION = 1
    }
}