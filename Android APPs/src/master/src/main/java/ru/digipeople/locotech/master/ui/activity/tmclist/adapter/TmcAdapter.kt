package ru.digipeople.locotech.master.ui.activity.tmclist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.TMCInWork
import ru.digipeople.locotech.master.model.TMCStatus
import ru.digipeople.ui.adapter.BaseDataAdapter
import javax.inject.Inject

/**
 * Адаптер списка списка ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcAdapter @Inject constructor(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<TmcAdapter.TmcViewHolder>(), BaseDataAdapter<TMCInWork> {
    //  region Other
    private var dataList: List<TMCInWork> = emptyList()
    var onDeleteClickListener: ((tmc: TMCInWork) -> Unit)? = null
    var onAmountClickListener: ((tmc: TMCInWork) -> Unit)? = null
    var isRolledMode: Boolean = false
    /**
     * Действия при создания холдера
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TmcViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.card_tmc, p0, false)
        return TmcViewHolder(view)
    }
    /**
     * Подсчет числа элементов
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(p0: TmcViewHolder, p1: Int) {
        p0.bind()
    }
    /**
     * Установка данных
     */
    override fun setData(list: List<TMCInWork>) {
        val diffUtilCallback = DiffUtilCallback(dataList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.dataList = list
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * Холдер ТМЦ
     */
    inner class TmcViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val statusView: View = view.findViewById(R.id.status_view)
        private val title: TextView = view.findViewById(R.id.tmc_title)
        private val status: TextView = view.findViewById(R.id.tmc_status)
        private val reciver: TextView = view.findViewById(R.id.name)
        private val askedAmount: TextView = view.findViewById(R.id.amount_asked)
        private val normalAmount: TextView = view.findViewById(R.id.amount_normal)
        private val sectionLeft: TextView = view.findViewById(R.id.section_left)
        private val stockLeft: TextView = view.findViewById(R.id.stock_left)
        private val min: TextView = view.findViewById(R.id.tmc_min)
        private val max: TextView = view.findViewById(R.id.tmc_max)
        private val mnk: TextView = view.findViewById(R.id.tmc_mnk)
        private val deleteBtn: ImageView = view.findViewById(R.id.delete_image_view)
        private val uom: TextView = view.findViewById(R.id.uom)

        init {
            /**
             * Установка обработчиков нажатия
             */
            deleteBtn.setOnClickListener { onDeleteClickListener?.invoke(dataList.get(adapterPosition)) }
            askedAmount.setOnClickListener { onAmountClickListener?.invoke(dataList.get(adapterPosition)) }
        }

        fun bind() {
            val tmc = dataList.get(adapterPosition)
            /**
             * Раскраска по статусам
             */
            when (tmc.status) {
                /**
                 * Заказано
                 */
                TMCStatus.ORDERED -> {
                    statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.appYellow))
                    status.setText(R.string.tmc_list_activity_orered)
                    status.setTextColor(ContextCompat.getColor(context, R.color.appGray))
                    askedAmount.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
                }
                /**
                 * Получено
                 */
                TMCStatus.ISSUED -> {
                    statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.appGreen))
                    status.setText(R.string.tmc_list_activity_received)
                    status.setTextColor(ContextCompat.getColor(context, R.color.appGreen))
                    askedAmount.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
                }
                /**
                 * Собрано
                 */
                TMCStatus.COLLECTED -> {
                    statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.appYellow))
                    status.setText(R.string.tmc_list_activity_compiled)
                    status.setTextColor(ContextCompat.getColor(context, R.color.appGray))
                    askedAmount.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
                }
                /**
                 * Заказано
                 */
                TMCStatus.DENIED -> {
                    statusView.setBackgroundColor(ContextCompat.getColor(context, R.color.appRed))
                    status.setText(R.string.tmc_list_activity_orered)
                    status.setTextColor(ContextCompat.getColor(context, R.color.appRed))
                    askedAmount.setTextColor(ContextCompat.getColor(context, R.color.appRed))
                }
            }

            title.text = tmc.name
            min.text = "${tmc.min}"
            max.text = "${tmc.max}"
            mnk.text = "${tmc.mnk}"

            if (tmc.reciver == null) {
                reciver.visibility = View.GONE
            } else {
                reciver.visibility = View.VISIBLE
                reciver.text = tmc.reciver!!.name
            }
            askedAmount.text = "${tmc.requested}"

            if (tmc.requested == 0.0) {
                /**
                 * Раскраска по сравнению с нормой
                 */
                askedAmount.setTextColor(ContextCompat.getColor(context, R.color.appBlack))
            } else if (tmc.requested <= tmc.normal) {
                askedAmount.setTextColor(ContextCompat.getColor(context, R.color.appGreen))
            } else {
                askedAmount.setTextColor(ContextCompat.getColor(context, R.color.appRed))
            }
            /**
             * Отображение данных
             */
            if (isRolledMode) {
                reciver.maxLines = MAX_LINES
                title.maxLines = MAX_LINES
                askedAmount.maxLines = MAX_LINES
                normalAmount.maxLines = MAX_LINES
                sectionLeft.maxLines = MAX_LINES
                stockLeft.maxLines = MAX_LINES
                min.maxLines = MAX_LINES
                max.maxLines = MAX_LINES
                mnk.maxLines = MAX_LINES
            } else {
                reciver.maxLines = MIN_MAX_LINES
                title.maxLines = MIN_MAX_LINES
                askedAmount.maxLines = MIN_MAX_LINES
                normalAmount.maxLines = MIN_MAX_LINES
                sectionLeft.maxLines = MIN_MAX_LINES
                stockLeft.maxLines = MIN_MAX_LINES
                min.maxLines = MIN_MAX_LINES
                max.maxLines = MIN_MAX_LINES
                mnk.maxLines = MIN_MAX_LINES
            }

            uom.text = tmc.uom
            normalAmount.text = "${tmc.normal}"
            sectionLeft.text = "${tmc.workshop}"
            stockLeft.text = "${tmc.stockRest}"
        }
    }

    companion object {
        private const val MIN_MAX_LINES = 1
        private const val MAX_LINES = 10
    }
}