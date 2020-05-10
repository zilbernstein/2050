package ru.digipeople.locotech.core.ui.activity.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.core.R
import ru.digipeople.locotech.core.ui.activity.settings.model.ThingWorxEndpoint
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер для ссылок на платформу ThingWorx
 *
 * @author Sostavkin Grisha
 */
class UrlsAdapter @Inject constructor() : BaseItemsRecyclerAdapter<ThingWorxEndpoint, UrlsAdapter.ViewHolder>() {

    var onItemClickListener: ((modelUrl: ThingWorxEndpoint) -> Unit)? = null
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_thingworx_url, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(items[position])
    }
    /**
     * Холдер
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val url = view.findViewById<TextView>(R.id.url)
        private val title = view.findViewById<TextView>(R.id.title)

        init {
            /**
             * Обработка нажатия на элемент списка
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onItemClickListener?.invoke(it)
                }
            }
        }
        /**
         * Привязка данных
         */
        fun bind(thingWorxEndpoint: ThingWorxEndpoint) {
            title.text = thingWorxEndpoint.title
            url.text = thingWorxEndpoint.url
        }
    }
}
