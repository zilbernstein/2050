package ru.digipeople.ui.activity.base

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ru.digipeople.ui.R
import ru.digipeople.ui.dagger.ActivityScope
import javax.inject.Inject

/**
 * Делегат для обработки тулбара.
 *
 * @author Kashonkov Nikita
 */
@ActivityScope
class ToolbarDelegate @Inject constructor(private val activity: AppCompatActivity) {

    private lateinit var toolbar: Toolbar
    private lateinit var customTitle: TextView
    private lateinit var alertCount: TextView
    private lateinit var navigationIcon: ImageView
    private lateinit var navigationView: View
    /**
     * инициализация делегата
     */
    fun init() {
        toolbar = activity.findViewById(R.id.toolbar)
        activity.setSupportActionBar(toolbar)
        customTitle = activity.findViewById(R.id.customTitle)
        alertCount = activity.findViewById(R.id.toolbar_alert_count)
        navigationIcon = activity.findViewById(R.id.toolbar_nav_icon)
        navigationView = activity.findViewById(R.id.toolbar_nav)
    }
    /**
     * установка заголовка
     */
    fun setTitle(@StringRes resId: Int) {
        activity.setTitle(resId)
        customTitle.setText(resId)
    }
    /**
     * установка заголовка
     */
    fun setTitle(title: CharSequence) {
        activity.title = title
        customTitle.text = title
    }
    /**
     * установка цвета фона
     */
    fun setBackgroundColor(@ColorRes resId: Int) {
        toolbar.setBackgroundResource(resId)
    }
    /**
     * установка иконки перехода назад
     */
    fun setNavigationIcon(@DrawableRes resId: Int) {
        navigationIcon.setImageResource(resId)
    }
    /**
     * установка иконки перехода назад
     */
    fun setNavigationIcon(icon: Drawable?) {
        navigationIcon.setImageDrawable(icon)
    }
    /**
     * отображение числа срочных работ
     */
    fun setAlertCount(count: Int) {
        if (count == 0) {
            alertCount.visibility = View.GONE
        } else {
            alertCount.visibility = View.VISIBLE
            if (count > 99) {
                alertCount.setText(R.string.maximum_alert_count)
            } else {
                alertCount.text = count.toString()
            }
        }
    }
    /**
     * скрытия числа срочных работ
     */
    fun hideAlertCount() {
        alertCount.visibility = View.GONE
    }
    /**
     * обработканажатия на навигацию
     */
    fun setNavigationOnClickListener(listener: (View) -> Unit) {
        navigationView.setOnClickListener(listener)
    }
    /**
     * обработка видимости пунктов меню
     */
    fun setOptionsMenuVisible(visible: Boolean) {
        val menu = toolbar.menu
        for (i in 0 until menu.size()) {
            menu.getItem(i).isVisible = visible
        }
    }
}
