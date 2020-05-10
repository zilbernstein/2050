package ru.digipeople.locotech.inspector.ui.activity.print.adapter

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер категорий подписантов
 *
 * @author Kashonkov Nikita
 */
class SignersCategoriesAdapter @Inject constructor() : BaseItemsRecyclerAdapter<SignersCategoryModel, SignersCategoriesAdapter.ViewHolder>() {

    var onAddSignerClickListener: ((category: SignersCategoryModel) -> Unit)? = null
    var onRemoveSignerClickListener: ((category: SignersCategoryModel, signer: Signer) -> Unit)? = null
    var onRemoveAllSignersClickListener: ((category: SignersCategoryModel) -> Unit)? = null
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.signers_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(getItem(position)!!)
    }
    /**
     * Холдер категории подписантов
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val addSigner: ImageView = view.findViewById(R.id.add_signer)
        private val removeAllSigners: ImageView = view.findViewById((R.id.remove_signer))
        private val chipGroup: ChipGroup = view.findViewById(R.id.chip_group)
        private val hint: TextView = view.findViewById(R.id.hint)

        init {
            /**
             * установить обработку добавления/удаления подписантов
             */
            addSigner.setOnClickListener {
                getItem(adapterPosition)?.let { signerCategory ->
                    onAddSignerClickListener?.invoke(signerCategory)
                }
            }
            removeAllSigners.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onRemoveAllSignersClickListener?.invoke(it)
                }
            }
            hint.setOnClickListener {
                getItem(adapterPosition)?.let {
                    onAddSignerClickListener?.invoke(it)
                }
            }
        }
        /**
         * привязка данных к шаблону
         */
        fun bind(signersCategory: SignersCategoryModel) {
            title.text = signersCategory.title
            chipGroup.removeAllViews()
            signersCategory.signers.forEach { signer ->
                val chip = Chip(chipGroup.context)
                chip.text = signer.name
                chip.isClickable = true
                chip.isCloseIconVisible = true
                chip.setOnCloseIconClickListener {
                    onRemoveSignerClickListener?.invoke(signersCategory, signer)
                }
                chipGroup.addView(chip)
            }
            updateEmptyDataVisibility()
        }

        private fun updateEmptyDataVisibility() {
            if (chipGroup.childCount == 0) {
                removeAllSigners.isEnabled = false
                chipGroup.visibility = View.GONE
                hint.visibility = View.VISIBLE
            } else {
                removeAllSigners.isEnabled = true
                chipGroup.visibility = View.VISIBLE
                hint.visibility = View.GONE
            }
        }
    }
}