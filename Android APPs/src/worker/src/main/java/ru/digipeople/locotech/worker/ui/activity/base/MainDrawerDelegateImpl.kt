package ru.digipeople.locotech.worker.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.ui.UiMainDrawerView
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.ui.fragment.maindrawer.MainDrawerFragment
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