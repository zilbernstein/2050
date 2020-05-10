package ru.digipeople.locotech.master.ui.activity.remarksearch.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.RemarkFromCatalog
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер добавления / создания замечания
 *
 * @author Kashonkov Nikita
 */
class RemarkSearchAdapter @Inject constructor() :
        BaseItemsRecyclerAdapter<RemarkFromCatalog, RemarkSearchAdapter.ViewHolder>() {

    //region Other
    var itemClickListener: ((remark: RemarkFromCatalog) -> Unit)? = null
    //endregion
    /**
     * Холдер
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val itemTitle: TextView = view.findViewById(R.id.item_remark_title)

        init {
            /**
             * Обрабокта нажатия на элемент
             */
            itemTitle.setOnClickListener { itemClickListener?.invoke(items[adapterPosition]) }
        }
    }
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_remark_search, parent, false)
        return ViewHolder(view)
    }
    /**
     * Привязка данных к шаблону
     */
    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.itemTitle.text = items[position].title
    }
}