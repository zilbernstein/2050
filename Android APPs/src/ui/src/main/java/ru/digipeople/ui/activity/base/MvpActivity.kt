package ru.digipeople.ui.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.mvp.MvpDelegate
import ru.digipeople.ui.mvp.view.MvpView

/**
 * Базовый класс для реализации MvpActivity
 *
 * @author Kashonkov Nikita
 */
open class MvpActivity : AppCompatActivity() {
    /**
     * Делегат для хранения/получения тега
     */
    private var mvpDelegate: MvpDelegate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiComponent.get().baseAuthorizationChecker().checkAuthorization(this)
        /**
         * инициализация делегата
         */
        mvpDelegate = MvpDelegate(UiComponent.get().mvpProcessor(), this as MvpView)
        mvpDelegate!!.init(savedInstanceState)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    /**
     * получить делегат
     */
    fun getMvpDelegate(): MvpDelegate {
        return mvpDelegate!!
    }
    /**
     * действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        mvpDelegate!!.bindView()
    }
    /**
     * действия при остановке активити
     */
    override fun onStop() {
        mvpDelegate!!.unbindView()
        super.onStop()
    }

    override protected fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvpDelegate!!.saveState(outState)
    }
    /**
     * действия при уничтожении активити
     */
    override fun onDestroy() {
        mvpDelegate!!.destroy(keepAlive())
        super.onDestroy()
    }

    protected fun keepAlive(): Boolean {
        return !isFinishing
    }
}