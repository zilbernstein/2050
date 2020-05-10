package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import ru.digipeople.locotech.master.R
import javax.inject.Inject

/**
 * Адаптер списание ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TMCAdapter @Inject constructor(private val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    //region Other
    private var data = AdapterData()
    var tmcClickListener: ((tmc: TMCData) -> Unit)? = null
    private var isRolledMode: Boolean = false
    /**
     * Выбор типа холдера
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        when (p1) {
            /**
             * Заголовок
             */
            VIEW_TYPE_TITLE -> {
                val viewSection = LayoutInflater.from(p0.context).inflate(R.layout.tmc_before_accept_work_title, p0, false)
                return TitleViewHolder(viewSection)
            }
            /**
             * ТМЦ
             */
            else -> {
                val viewEquipment = LayoutInflater.from(p0.context).inflate(R.layout.item_tmc_before_accept, p0, false)
                return TmcViewHolder(viewEquipment)
            }
        }
    }
    /**
     * Установка данных
     */
    fun setData(list: AdapterData) {
        val diffUtilCallback = DiffUtilCallback(data, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.data = list
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * Получение количества элементов
     */
    override fun getItemCount(): Int {
        return data.size
    }
    /**
     * Определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        if (data.isTitle(position)) {
            return VIEW_TYPE_TITLE
        } else if (data.isTmcData(position)) {
            return VIEW_TYPE_TMC
        } else {
            throw IllegalArgumentException("Unsupported position = " + position)
        }
    }

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        if (data.isTitle(p1)) {
            (p0 as TitleViewHolder).bind()
        } else {
            (p0 as TmcViewHolder).bind()
        }
    }
    /**
     * Холдер заголовка
     */
    inner class TitleViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.tmc_title)
        private var arrow: ImageView = view.findViewById(R.id.tmc_arrow)

        fun bind() {
            /**
             * Привязка данных к шаблону
             */
            title.text = data.getTitle(adapterPosition)
            arrow.setOnClickListener {
                isRolledMode = !isRolledMode
                if (isRolledMode) {
                    arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_up))
                    notifyDataSetChanged()
                } else {
                    arrow.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_down))
                    notifyDataSetChanged()
                }
            }
        }
    }
    /**
     * Холдер ТМЦ
     */
    inner class TmcViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val code: TextView = view.findViewById(R.id.tmc_code)
        private val title: TextView = view.findViewById(R.id.tmc_title)
        private val normal: TextView = view.findViewById(R.id.tmc_normal)
        private val stand: TextView = view.findViewById(R.id.tmc_stand)
        private val acceptorName: TextView = view.findViewById(R.id.tmc_acceptor_name)
        private val min: TextView = view.findViewById(R.id.tmc_min)
        private val max: TextView = view.findViewById(R.id.tmc_max)
        private val mnk: TextView = view.findViewById(R.id.tmc_mnk)
        private val uom: TextView = view.findViewById(R.id.uom)
        /**
         * Привязка данных к шаблону
         */
        fun bind() {
            val tmc = data.getTmc(adapterPosition).tmc
            code.text = tmc.id
            title.text = tmc.name
            normal.text = "${tmc.normal}"
            min.text = "${tmc.min}"
            max.text = "${tmc.max}"
            mnk.text = "${tmc.mnk}"
            uom.text = tmc.uom

            stand.text = "${tmc.assigned}"
            /**
             * Раскраска в зависимости от нормы
             */
            if (tmc.assigned > tmc.normal) {
                stand.setTextColor(ContextCompat.getColor(context, R.color.appRed))
            } else {
                stand.setTextColor(ContextCompat.getColor(context, R.color.appGreen))
            }

            if (isRolledMode) {
                code.maxLines = MAX_LINES
                title.maxLines = MAX_LINES
                normal.maxLines = MAX_LINES
                min.maxLines = MAX_LINES
                max.maxLines = MAX_LINES
                mnk.maxLines = MAX_LINES
            } else {
                code.maxLines = MIN_MAX_LINES
                title.maxLines = MIN_MAX_LINES
                normal.maxLines = MIN_MAX_LINES
                min.maxLines = MIN_MAX_LINES
                max.maxLines = MIN_MAX_LINES
                mnk.maxLines = MIN_MAX_LINES
            }
            stand.setOnClickListener { tmcClickListener?.invoke(data.getTmc(adapterPosition)) }
            /**
             * Установка получателя
             */
            if (tmc.reciver != null) {
                acceptorName.text = tmc.reciver!!.name
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_TITLE = 0
        private const val VIEW_TYPE_TMC = 1
        private const val MIN_MAX_LINES = 1
        private const val MAX_LINES = 10
    }
}