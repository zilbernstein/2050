package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.WheelPair
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject
import javax.inject.Provider

/**
 * Адаптер колесных пар
 */
class WheelPairsAdapter @Inject constructor(private val wheelParamsAdapterProvider: Provider<WheelParamsAdapter>) : BaseItemsRecyclerAdapter<WheelPair, WheelPairsAdapter.ViewHolder>() {

    var onToggleTurningListener: ((wheelPair: WheelPair, position: Int) -> Unit)? = null
    var onEditBtnClickListener: ((wheelPair: WheelPair, position: Int) -> Unit)? = null
    /**
     * Создане холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_wheel_pair, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val item = items[position]
        vh.bind(item)
    }
    /**
     * Холдер замера колесной пары
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //region Views
        private val pairNumber = view.findViewById<TextView>(R.id.pair_number)
        private val axisNumber = view.findViewById<TextView>(R.id.axis_number_value)
        private val axisValue = view.findViewById<TextView>(R.id.axis_value)
        private val flangeLeftNumberValue = view.findViewById<TextView>(R.id.flange_left_number_value)
        private val flangeRightNumberValue = view.findViewById<TextView>(R.id.flange_right_number_value)
        private val tuningCheckBox = view.findViewById<TextView>(R.id.tuning_cb)
        private val reasonValue = view.findViewById<TextView>(R.id.reason_value)
        private val editButton = view.findViewById<Button>(R.id.edit_btn)
        private val paramsRecyclerView = view.findViewById<RecyclerView>(R.id.wheel_params_recycler)
        //endregion
        private val wheelParamsAdapter = wheelParamsAdapterProvider.get()
        /**
         * Инициализация
         */
        init {
            paramsRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            paramsRecyclerView.addItemDecoration(HeaderDecoration(paramsRecyclerView, context.resources.getDimensionPixelOffset(R.dimen.offset_item_wheel_param_header)))

            paramsRecyclerView.adapter = wheelParamsAdapter

            tuningCheckBox.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onToggleTurningListener?.invoke(it, adapterPosition)
                }
            }
            /**
             * Обработка редактирования
             */
            editButton.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onEditBtnClickListener?.invoke(it, adapterPosition)
                }
            }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(wheelPair: WheelPair) {
            pairNumber.text = context.getString(R.string.measurement_wheel_pair_number, wheelPair.number)
            axisNumber.text = wheelPair.axisNumber
            axisValue.text = wheelPair.number
            flangeLeftNumberValue.text = wheelPair.flangeLeftNumber
            flangeRightNumberValue.text = wheelPair.flangeRightNumber
            reasonValue.text = if (wheelPair.repairReasonId.isEmpty()) {
                context.getString(R.string.measurement_wheel_no_reason)
            } else {
                wheelPair.repairReasonName
            }
            tuningCheckBox.isSelected = wheelPair.repairReasonId.isNotEmpty()
            wheelParamsAdapter.items = wheelPair.wheelParams
        }
    }
}