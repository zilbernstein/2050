package ru.digipeople.ui.activity.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import ru.digipeople.ui.R
import ru.digipeople.ui.UiMainDrawerView

/**
 * Базовый интерфейс MainDrawerDelegate
 *
 * @author Sostavkin Grisha
 */

open class MainDrawerDelegate(protected val activity: AppCompatActivity, val toolbarDelegate: ToolbarDelegate) {
    /**
     * Боковое меню
     */
    private lateinit var mainDrawerView: UiMainDrawerView
    /**
     * Флаг, что отображается иконка "Назад" вместо гамбургера
     */
    var iconBack: Boolean = false
    /**
     * Количество срочно
     */
    var urgentAmount: Int = 0
    /**
     * DrawerLayout
     */
    protected lateinit var drawerLayout: DrawerLayout
    /**
     * Отложенная операция перехода на другой экран
     */
    private var navigateRunnable: Runnable? = null
    /**
     * Слушатель изменения состояния [DrawerLayout]
     */
    private var drawerStateChangeListener: DrawerStateChangeListener? = null
    /**
     * Инициализация боковог меню
     */
    fun init(iconBack: Boolean) {
        this.iconBack = iconBack
        setNavigationIcon()
        drawerLayout = activity.findViewById(R.id.drawerLayout)
        drawerLayout.addDrawerListener(drawerListener)

        mainDrawerView = findMainDrawerView()
        mainDrawerView.init(this)

        toolbarDelegate.setNavigationOnClickListener(toolbarNavigationListener)
    }

    open fun findMainDrawerView(): UiMainDrawerView {
        return (activity.findViewById<View>(R.id.mainDrawer)) as UiMainDrawerView
    }
    /**
     * обрабокта нажатия назад
     */
    fun onBackPressed(): Boolean {
        return closeDrawer()
    }
    /**
     * закрыть меню
     */
    fun closeDrawer(): Boolean {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
        return false
    }
    /**
     * установки иконки навигации
     */
    private fun setNavigationIcon() {
        if (iconBack) {
            toolbarDelegate.hideAlertCount();
            toolbarDelegate.setNavigationIcon(R.drawable.ic_toolbar_back);
        } else {
            toolbarDelegate.setNavigationIcon(R.drawable.menu);
            if (urgentAmount > 0) {
                if (!iconBack) {
                    toolbarDelegate.setAlertCount(urgentAmount);
                }
            } else {
                toolbarDelegate.hideAlertCount()
            }
        }
    }
    /**
     * действия при окрытии меню
     */
    protected open fun onDrawerOpened(drawerView: View) {
        // nop
    }

    private val drawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

        }

        override fun onDrawerOpened(drawerView: View) {
            this@MainDrawerDelegate.onDrawerOpened(drawerView)
        }

        override fun onDrawerClosed(drawerView: View) {
            navigateRunnable?.run {
                run()
                navigateRunnable = null
            }
        }

        override fun onDrawerStateChanged(newState: Int) {
            drawerStateChangeListener?.onDrawerStateChanged(newState)
        }
    }
    /**
     * обрабокткаоткрытия меню при навигации
     */
    private val toolbarNavigationListener = { _: View ->
        if (iconBack) {
            mainDrawerView.navigateBack()
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    /**
     * бработка измененя состояния
     */
    interface DrawerStateChangeListener {
        fun onDrawerStateChanged(newState: Int)
    }
}