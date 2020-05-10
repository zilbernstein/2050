package ru.digipeople.locotech.core.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.ui.R

/**
 * Базовый класс для активити
 *
 * @author Aleksandr Brazhkin
 */
abstract class AppActivity : AppCompatActivity() {
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.zero_animation, R.anim.slide_to_right)
    }
}