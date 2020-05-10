package ru.digipeople.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

/**
 * Базовый класс для RecyclerView
 */
abstract class BaseRecyclerAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var _context: Context? = null
    protected open val context
        get() = _context!!
    private var _layoutInflater: LayoutInflater? = null
    protected val layoutInflater
        get() = _layoutInflater!!
    /**
     * прикрепление к ресайклеру
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        _context = recyclerView.context
        _layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
    /**
     * открепление от ресайклера
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _context = null
        _layoutInflater = null
    }
}
