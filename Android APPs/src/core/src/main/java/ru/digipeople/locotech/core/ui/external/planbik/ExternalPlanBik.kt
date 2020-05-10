package ru.digipeople.locotech.core.ui.external.planbik

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Browser
import androidx.core.content.ContextCompat.startActivity
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.data.PlanBikUrlProvider
import ru.digipeople.locotech.core.ui.helper.AuthInfoStorage
import ru.digipeople.utils.HttpUtils
import javax.inject.Inject

/**
 * Класс инициализирующий переход на План Бик
 *
 * @author Michael Solenov
 */
class ExternalPlanBik {
    //region Di
    private val component by lazy {
        DaggerPlanBikComponent.builder()
                .coreAppComponent(CoreAppComponent.get())
                .build()
    }
    @Inject
    lateinit var planBikUrlProvider: PlanBikUrlProvider
    @Inject
    lateinit var authInfoStorage: AuthInfoStorage
    //endregion

    init {
        component.inject(this)
    }
    /**
     * Навигация
     */
    fun navigate(activity: Activity) {

        val browserIntent = Intent(Intent.ACTION_VIEW, planBikUrlProvider.get())
        val bundle = Bundle()
        val (login, password) = authInfoStorage.loginPassPair
        val header = HttpUtils.buildBasicAuthHeader(login, password)
        /**
         * добавление данных в  интент
         */
        bundle.putString("Authorization", header)
        browserIntent.putExtra(Browser.EXTRA_HEADERS, bundle)
        startActivity(activity, browserIntent, null)
    }
}