package ru.digipeople.telephonebook.ui.activity.telephone.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.digipeople.telephonebook.model.Contact

/**
 * Класс для сравнения объектов
 *
 * @author Sostavkin Grisha
 */
class DiffUtilCallback (private val newRows: List<Contact>, private val oldRows: List<Contact>)
: DiffUtil.Callback() {
    /**
     * Проверка повторяющихсмя элементов для ускореия перерисовки
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRow = oldRows[oldItemPosition]
        val newRow = newRows[newItemPosition]
        return oldRow.id == newRow.id
    }

    override fun getOldListSize(): Int = oldRows.size

    override fun getNewListSize(): Int = newRows.size
    /**
     * Сравненеи двух элементов
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldRow = oldRows[oldItemPosition]
        val newRow = newRows[newItemPosition]
        return oldRow == newRow
    }
}