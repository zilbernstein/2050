package ru.digipeople.locotech.inspector.ui.activity.signersearch.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.ui.dagger.ScreenScope
import javax.inject.Inject

/**
 * Адаптер поиска подписантов
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
class SignersAdapter @Inject constructor() :
        BaseItemsRecyclerAdapter<SignerModel, SignersAdapter.SignerViewHolder>() {

    var itemClickListener: ((signer: SignerModel) -> Unit)? = null
    /**
     * созданиие холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_signer_search, parent, false)
        return SignerViewHolder(view)
    }

    override fun onBindViewHolder(vh: SignerViewHolder, position: Int) {
        vh.bind(getItem(position)!!)
    }
    /**
     * Холдер исполниетеля
     */
    inner class SignerViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.name)
        private val selectedMark: ImageView = view.findViewById(R.id.selected_mark)

        init {
            /**
             * обработка нажатия на исполнителя
             */
            itemView.setOnClickListener {
                getItem(adapterPosition)?.let {
                    itemClickListener?.invoke(it)
                }
                notifyItemChanged(adapterPosition)
            }
        }
        /**
         * привязка данных к шаблону
         */
        fun bind(item: SignerModel) {
            name.text = item.signer.name
            selectedMark.isSelected = item.isSelected
        }
    }
}