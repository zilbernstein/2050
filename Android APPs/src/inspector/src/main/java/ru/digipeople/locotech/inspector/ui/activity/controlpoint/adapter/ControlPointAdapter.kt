package ru.digipeople.locotech.inspector.ui.activity.controlpoint.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер контрольных точек
 * @author Kashonkov Nikita
 */
class ControlPointAdapter @Inject constructor() : BaseItemsRecyclerAdapter<ControlPoint, ControlPointAdapter.ViewHolder>() {
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_control_point, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(getItem(position)!!)
    }
    /**
     * Холдер контрольной точки
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.control_point_title)
        private val amount: TextView = view.findViewById(R.id.control_point_amount)
        /**
         * Привязка данных к шаблону
         */
        fun bind(controlPoint: ControlPoint) {
            title.text = controlPoint.name
            amount.text = controlPoint.mark
        }
    }
}