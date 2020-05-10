package ru.digipeople.locotech.worker.ui.activity.task.adapter

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_tmc.view.*
import ru.digipeople.ui.adapter.BaseDataAdapter
import ru.digipeople.ui.dagger.ScreenScope
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.TMCInWork
import javax.inject.Inject

/**
 * Адаптер для TMC
 *
 * @author Kashonkov Nikita
 */
@ScreenScope
class TMCAdapter @Inject constructor(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<TMCAdapter.TMCViewHolder>(), BaseDataAdapter<TMCInWork> {

    private var dataList: List<TMCInWork> = emptyList()
    /**
     * Холдер ТМЦ
     */
    class TMCViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val title = view.item_tmc_title
        val qauntity = view.item_tmc_quantity
        val uom = view.uom
    }
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TMCViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_tmc, p0, false)
        return TMCViewHolder(view)
    }
    /**
     * Получение числа элементов
     */
    override fun getItemCount(): Int {
        return dataList.size
    }
    /**
     * Привязка джанных к шаблону
     */
    override fun onBindViewHolder(p0: TMCViewHolder, p1: Int) {
        val tmc = dataList.get(p1)

        p0.title.text = tmc.name
        p0.uom.text = tmc.uom
        val req = tmc.tmcRequested
        print(req)

        if (tmc.tmcRequested == Math.floor(tmc.tmcRequested)) {
            var amount = tmc.tmcRequested.toInt()
            p0.qauntity.text = context.getString(R.string.task_activity_tmc_amount, amount)
        } else {
            p0.qauntity.text = context.getString(R.string.task_activity_tmc_amount_float, tmc.tmcRequested.toInt())
        }
    }
    /**
     * Установка данных
     */
    override fun setData(list: List<TMCInWork>) {
        val diffUtilCallback = DiffUtilCallback(dataList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.dataList = list
        diffResult.dispatchUpdatesTo(this)
    }
}