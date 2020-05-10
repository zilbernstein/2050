package ru.digipeople.locotech.technologist.ui.activity.work.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_work_comment.view.*
import kotlinx.android.synthetic.main.item_work_parameter_value.view.*
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.model.Parameter
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter

/**
 * Адаптер для параметров
 */
class ParametersAdapter : BaseItemsRecyclerAdapter<Parameter, RecyclerView.ViewHolder>() {

    var onCommentClickListener: (() -> Unit)? = null
    var comment: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    /**
     * Создание холдера в зависимости о типа элемента
     */
    override fun onCreateViewHolder(recyclerView: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            /**
             * Параметр работы
             */
            VIEW_TYPE_DEFAULT -> {
                val view = layoutInflater.inflate(R.layout.item_work_parameter_value, recyclerView, false)
                DefaultViewHolder(view)
            }
            VIEW_TYPE_COMMENT -> {
                /**
                 * комментарий работы
                 */
                val view = layoutInflater.inflate(R.layout.item_work_comment, recyclerView, false)
                CommentViewHolder(view)
            }
            else -> {
                throw IllegalArgumentException("Unknown view type")
            }
        }
    }
    /**
     * Определение числа элементов
     */
    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_DEFAULT -> {
                val vh = holder as DefaultViewHolder
                vh.bind(items[position])
            }
            VIEW_TYPE_COMMENT -> {
                val vh = holder as CommentViewHolder
                vh.bind()
            }
        }
    }
    /**
     * Определение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) {
            VIEW_TYPE_COMMENT
        } else {
            VIEW_TYPE_DEFAULT
        }
    }
    /**
     * Холдер параметра работы
     */
    inner class DefaultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textTitle: TextView = view.work_parameter_value_title
        private val valueText: TextView = view.work_parameter_nomination_value
        /**
         * Привязка данных к шаблону
         */
        fun bind(parameter: Parameter) {
            textTitle.text = parameter.parameterName
            valueText.text = parameter.parameterValue
        }
    }
    /**
     * Холдер комментария
     */
    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textComment: TextView = view.work_parameter_comment_value

        init {
            textComment.setOnClickListener { onCommentClickListener?.invoke() }
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind() {
            textComment.text = comment
        }
    }

    companion object {
        private const val VIEW_TYPE_COMMENT = 1
        private const val VIEW_TYPE_DEFAULT = 0
    }
}