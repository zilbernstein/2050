package ru.digipeople.ui.activity.base

import android.app.Activity
import ru.digipeople.ui.R

/**
 * Базовый класс для навигации активити
 *
 * @author Kashonkov Nikita
 */
abstract class BaseActivityNavigator {
    /**
     * переход к предыдущему экрану
     */
    fun navigateBack(activity: Activity) {
        activity.finish()
    }

    fun animForward(activity: Activity) {
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.zero_animation)
    }

    fun animBack(activity: Activity) {
        activity.overridePendingTransition(R.anim.zero_animation, R.anim.slide_to_right)
    }
}