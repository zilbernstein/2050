package ru.digipeople.locotech.master.ui.activity.worklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.item_work_list.view.*
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
import ru.digipeople.ui.dagger.ScreenScope
import javax.inject.Inject
/**
 * Адаптер добавление замечания / работ
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
class WorkListAdapter @Inject constructor() : BaseItemsRecyclerAdapter<Work, WorkListAdapter.ViewHolder>() {
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_work_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val work = items[position]
        vh.bind(work)
    }
    /**
     * Холдер работ
     */
    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val workTitle: AppCompatTextView = view.item_work_list_title
        val firstPerformer: AppCompatTextView = view.item_work_list_first_performer
        val secondPerformer: AppCompatTextView = view.item_work_list_second_performer
        val firdPerformer: AppCompatTextView = view.item_work_list_fird_performer
        val amount: AppCompatTextView = view.item_work_list_amount_of_performer
        /**
         * Привязка данных к шаблону
         */
        fun bind(work: Work) {
            workTitle.text = work.title

            val performerList = work.performers
            /**
             * Установка исполнителей
             */
            when (performerList.size) {
                /**
                 * нет исполниетелей
                 */
                0 -> {
                    firstPerformer.visibility = View.GONE
                    secondPerformer.visibility = View.GONE
                    firdPerformer.visibility = View.GONE
                    amount.visibility = View.GONE
                }
                /**
                 * 1 исполниетель
                 */
                1 -> {
                    firstPerformer.visibility = View.VISIBLE
                    firstPerformer.text = performerList.get(0).name
                    secondPerformer.visibility = View.GONE
                    firdPerformer.visibility = View.GONE
                    amount.visibility = View.GONE
                }
                /**
                 * 2 исполниетеля
                 */
                2 -> {
                    firstPerformer.visibility = View.VISIBLE
                    firstPerformer.text = performerList.get(0).name
                    secondPerformer.visibility = View.VISIBLE
                    secondPerformer.text = performerList.get(1).name
                    firdPerformer.visibility = View.GONE
                    amount.visibility = View.GONE
                }
                /**
                 * 3 исполниетеля
                 */
                3 -> {
                    firstPerformer.visibility = View.VISIBLE
                    firstPerformer.text = performerList.get(0).name
                    secondPerformer.visibility = View.VISIBLE
                    secondPerformer.text = performerList.get(1).name
                    firdPerformer.visibility = View.VISIBLE
                    firdPerformer.text = performerList.get(2).name
                    amount.visibility = View.GONE
                }
                /**
                 * больше 3 исполнителей
                 */
                else -> {
                    firstPerformer.visibility = View.VISIBLE
                    firstPerformer.text = performerList.get(0).name
                    secondPerformer.visibility = View.VISIBLE
                    secondPerformer.text = performerList.get(1).name
                    firdPerformer.visibility = View.VISIBLE
                    firdPerformer.text = performerList.get(2).name
                    amount.visibility = View.VISIBLE
                    amount.text = context.getString(R.string.work_list_performers_amount, work.performers.size - 3)
                }
            }
        }
    }
}