package ru.digipeople.locotech.technologist.ui.activity.remarks.dialog.approve

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_approve_dialog.*
import ru.digipeople.locotech.technologist.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter

/**
 * Адаптер для подтверждения 
 */
class ApproveAdapter : BaseItemsRecyclerAdapter<String, ApproveAdapter.ViewHolder>() {
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(layoutInflater.inflate(R.layout.item_approve_dialog, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    /**
     * Холдер
     */
    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        /**
         * Привязка данных к шаблону
         */
        fun bind(item: String) {
            remark_text_title.text = item
        }
    }
}