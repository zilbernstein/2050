package ru.digipeople.locotech.master.ui.activity.checkwork.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import kotlinx.android.synthetic.main.card_check_work.view.*
import ru.digipeople.locotech.core.data.model.Performer
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import javax.inject.Inject

/**
 * Адаптер проверки выбранных работ
 *
 * @author Kashonkov Nikita
 */
class CheckWorkAdapter @Inject constructor() : BaseItemsRecyclerAdapter<Work, CheckWorkAdapter.ViewHolder>() {
    // region Other
    var isPhotoVisible: Boolean = false
    var isPerformersVisible = true
    var deleteClickListener: ((work: Work) -> Unit)? = null
    var photoClickListener: ((work: Work) -> Unit)? = null
    //endregion
    /**
     * Холдер работы
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val workTitle: AppCompatTextView = view.check_work_title
        val firstPerformer: AppCompatTextView = view.check_work_first_performer
        val secondPerformer: AppCompatTextView = view.check_work_second_performer
        val thirdPerformer: AppCompatTextView = view.check_work_third_performer
        val amountOfOtherPerformers: AppCompatTextView = view.check_work_amount_of_other_performers
        val photoAmount: TextView = view.photo_ammount
        val photoGroup: ConstraintLayout = view.photo_group
        val icon: ImageView = view.check_work_icon_image
        /**
         * инициализация значений списка
         */
        init {
            photoGroup.setOnClickListener { photoClickListener?.invoke(items[adapterPosition]) }
        }

        /**
         * управление видимостью исполнителей
         */
        fun hidePerformers() {
            firstPerformer.visibility = View.GONE
            secondPerformer.visibility = View.GONE
            thirdPerformer.visibility = View.GONE
            amountOfOtherPerformers.visibility = View.GONE
        }
    }
    /**
     * Операции при создании экрана
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.card_check_work, parent, false)
        return ViewHolder(view)
    }
    /**
     * Операции при прикреплении экрана
     */
    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val work = items[position]

        vh.workTitle.text = work.title

        val performerList = work.performers
        /**
         * Управление видимостью и числом отображаемых исполнителей
         */
        if (isPerformersVisible) {
            when (performerList.size) {
                0 -> {
                    /**
                     * нет исполниетелей
                     */
                    vh.hidePerformers()
                }
                1 -> {
                    /**
                     * 1 исполнитель
                     */
                    vh.firstPerformer.visibility = View.VISIBLE
                    vh.firstPerformer.text = getPerformerDisplayedName(performerList[0])
                    vh.secondPerformer.visibility = View.GONE
                    vh.thirdPerformer.visibility = View.GONE
                    vh.amountOfOtherPerformers.visibility = View.GONE
                }
                2 -> {
                    /**
                     * 2 исполнител
                     */
                    vh.firstPerformer.visibility = View.VISIBLE
                    vh.firstPerformer.text = getPerformerDisplayedName(performerList[0])
                    vh.secondPerformer.visibility = View.VISIBLE
                    vh.secondPerformer.text = getPerformerDisplayedName(performerList[1])
                    vh.thirdPerformer.visibility = View.GONE
                    vh.amountOfOtherPerformers.visibility = View.GONE
                }
                3 -> {
                    /**
                     * 3 исполнителя
                     */
                    vh.firstPerformer.visibility = View.VISIBLE
                    vh.firstPerformer.text = getPerformerDisplayedName(performerList[0])
                    vh.secondPerformer.visibility = View.VISIBLE
                    vh.secondPerformer.text = getPerformerDisplayedName(performerList[1])
                    vh.thirdPerformer.visibility = View.VISIBLE
                    vh.thirdPerformer.text = getPerformerDisplayedName(performerList[2])
                    vh.amountOfOtherPerformers.visibility = View.GONE
                }
                else -> {
                    /**
                     * больше 3 исполнителей
                     */
                    vh.firstPerformer.visibility = View.VISIBLE
                    vh.firstPerformer.text = getPerformerDisplayedName(performerList[0])
                    vh.secondPerformer.visibility = View.VISIBLE
                    vh.secondPerformer.text = getPerformerDisplayedName(performerList[1])
                    vh.thirdPerformer.visibility = View.VISIBLE
                    vh.thirdPerformer.text = getPerformerDisplayedName(performerList[2])
                    vh.amountOfOtherPerformers.visibility = View.VISIBLE
                    vh.amountOfOtherPerformers.text = context.getString(R.string.check_work_performers_amount, performerList.size - 3)
                }
            }
        } else {
            /**
             * скрыть всех исполнителей
             */
            vh.hidePerformers()
        }
        /**
         * Управление видимостью фотографий
         */
        if (isPhotoVisible) {
            vh.photoGroup.visibility = View.VISIBLE
            vh.photoAmount.text = "${work.photoCount}"
        } else {
            vh.photoGroup.visibility = View.GONE
        }
        /**
         * обработчик удаления
         */
        vh.icon.setOnClickListener { deleteClickListener?.invoke(work) }
    }
    /**
     * установка имени исполнителя
     */
    private fun getPerformerDisplayedName(performer: Performer): String {
        return performer.name
    }
}