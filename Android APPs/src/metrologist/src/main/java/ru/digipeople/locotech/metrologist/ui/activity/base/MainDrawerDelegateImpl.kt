package ru.digipeople.locotech.metrologist.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.ui.fragment.maindrawer.MainDrawerFragment
import ru.digipeople.ui.UiMainDrawerView
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import javax.inject.Inject

/**
 * Абстракция над [MainDrawerDelegate]
 *
 * @author Aleksandr Brazhkin
 */
class MainDrawerDelegateImpl @Inject constructor(
        activity: AppCompatActivity,
        toolbarDelegate: ToolbarDelegate
) : MainDrawerDelegate(activity, toolbarDelegate) {
    override fun findMainDrawerView(): UiMainDrawerView {
        return activity.supportFragmentManager.findFragmentById(R.id.mainDrawerFragment) as MainDrawerFragment
    }
}