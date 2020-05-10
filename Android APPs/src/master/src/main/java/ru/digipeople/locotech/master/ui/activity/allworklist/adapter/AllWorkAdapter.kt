package ru.digipeople.locotech.master.ui.activity.allworklist.adapter

import androidx.appcompat.widget.AppCompatTextView
import android.view.View
import android.view.ViewGroup
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.WorkFromCatalog
import kotlinx.android.synthetic.main.item_work_search.view.*
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.ui.dagger.ScreenScope
import javax.inject.Inject

/**
 * Адаптер списка работ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
class AllWorkAdapter @Inject constructor() :
        BaseItemsRecyclerAdapter<WorkFromCatalog, AllWorkAdapter.ViewHolder>(){

    //region Other
    var itemClickListener: ((work: WorkFromCatalog) -> Unit)? = null
    //endregion

    /*
    * Переопределенная функция создания вьюхолдера
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_work_search, parent, false)
        return ViewHolder(view)
    }

    /*
    * Переопределенная функция привязки вьюхолдера
    */
    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.itemName.text = items[position].title
    }

    /*
    * Вьюхолдер
    */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val itemName: AppCompatTextView = view.item_work_search_title
        /*
    * Инициализация
    */
        init {
            /*
            * Создание обработчика нажатия
            */
            itemView.setOnClickListener {
                val work = items[adapterPosition]
                itemClickListener?.invoke(work)
            }
        }
    }
}