package ru.digipeople.ui.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * Базовый класс для RecyclerView
 */
abstract class BaseItemsRecyclerAdapter<T, VH : RecyclerView.ViewHolder> : BaseRecyclerAdapter<VH>() {
    var items: List<T> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    /**
     * признак пустого набора
     */
    val isEmpty: Boolean
        get() = itemCount == 0
    /**
     * определение количества элементов в списке
     */
    override fun getItemCount(): Int {
        return items.size
    }
    /**
     * получить элемент по его позиции
     */
    fun getItem(position: Int): T? {
        return if (position in items.indices) {
            items[position]
        } else {
            null
        }
    }
}
