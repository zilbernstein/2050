package ru.digipeople.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import ru.digipeople.ui.R

/**
 * Вертикальный SortView
 *
 * @author Kashonkov Nikita
 */
class VerticalSortView: BaseSortView {
    /**
     * Конструкторы
     */
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context) : this(context, null)

    override fun inflateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.sort_view_vertical, this, true)
    }
}