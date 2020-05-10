package ru.digipeople.locotech.metrologist.ui.activity.measurementconfirmation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_wheel_pair_short.*
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.WheelPairShort
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
/**
 * адаптер колесных пар
 */
class WheelPairsAdapter : BaseItemsRecyclerAdapter<WheelPairShort, WheelPairsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_wheel_pair_short, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val wheelPair = items[position]
        vh.bind(wheelPair)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(wheelPair: WheelPairShort) {
            kp_title.text = context.getString(R.string.measurement_confirmation_wheel_pair_number, wheelPair.pairNumber)
            axis_number_value.text = wheelPair.axisNumber
            axis_count_value.text = wheelPair.pairNumber
            flange_left_number_value.text = wheelPair.flangeLeftNumber
            flange_right_number_value.text = wheelPair.flangeRigthNumber
            if (wheelPair.mustRepair) {
                tuning_label.text = context.getString(R.string.measurement_confirmation_turning_required)
                tuning_label.setBackgroundColor(ActivityCompat.getColor(context, R.color.pure_red))
                tuning_value.text = wheelPair.repairReasonName
            } else {
                tuning_label.text = context.getString(R.string.measurement_confirmation_turning_not_required)
                tuning_label.setBackgroundColor(ActivityCompat.getColor(context, R.color.pure_green))
                tuning_value.text = context.getString(R.string.measurement_conformation_tuning_not_required)
            }
        }
    }
}