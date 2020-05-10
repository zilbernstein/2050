package ru.digipeople.locotech.worker

import android.content.Context
import ru.digipeople.locotech.worker.api.ThingsWorxWorker
import ru.digipeople.locotech.worker.data.task.CurrentTaskProvider
import ru.digipeople.locotech.worker.helper.AuthWorkerProxyImpl
import ru.digipeople.locotech.worker.helper.ClientProvider
import ru.digipeople.locotech.worker.ui.activity.ActivityNavigator
import ru.digipeople.locotech.worker.ui.activity.AppNavigator
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.base.MainDrawerDelegateFactoryImpl
import ru.digipeople.locotech.worker.ui.activity.choosereason.ChooseReasonScreenComponent
import ru.digipeople.locotech.worker.ui.activity.comment.CommentScreenComponent
import ru.digipeople.locotech.worker.ui.activity.measurements.MeasurementsComponent
import ru.digipeople.locotech.worker.ui.activity.mytask.MyTaskScreenComponent
import ru.digipeople.locotech.worker.ui.activity.shift.ShiftScreenComponent
import ru.digipeople.locotech.worker.ui.activity.splash.SplashScreenComponent
import ru.digipeople.locotech.worker.ui.activity.task.TaskScreenComponent
import ru.digipeople.locotech.worker.ui.activity.tmcshortage.TmcShortageScreenComponent
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.mvp.core.MvpProcessor

/**
 * Интерфейс МП Сотрудник
 *
 * @author Kashonkov Nikita
 */
interface AppComponent {
    fun inject(appInitProvider: AppInitProvider)

    fun context(): Context
    /**
     * Класс, управляющий презентерами.
     */
    fun mvpProcessor(): MvpProcessor
    /**
     * Класс реализующий навигацию к активити МП Сотрудник
     */
    fun activityNavigator(): ActivityNavigator
    /**
     * Класс дл работы с системой BaseThingWorx.
     */
    fun baseThingWorx(): BaseThingWorx
    /**
     * Интерфейс по работе с Апи МП Сотрудник
     */
    fun thingWorxWorker(): ThingsWorxWorker
    /**
     * Провайдер текущего пользователя
     */
    fun clientProvider(): ClientProvider
    /**
     * Класс для хранения текущей смены
     */
    fun taskProvider(): CurrentTaskProvider
    /**
     * Компонент экрана структуры выбора причины
     */
    fun chooseReasonScreenComponent(): ChooseReasonScreenComponent
    /**
     * Экранный компонент структуры задания
     */
    fun taskScreenComponent(): TaskScreenComponent
    /**
     * Экранный компонент структуры смены
     */
    fun shiftScreenComponent(): ShiftScreenComponent
    /**
     * Экранный компонент структуры работы
     */
    fun myTaskScreenComponent(): MyTaskScreenComponent
    /**
     * Экранный компонент структуры комментария
     */
    fun commentScreenComponent(): CommentScreenComponent
    /**
     * Экранный компонент структуры ТМЦ
     */
    fun tmcShortageScreenComponent(): TmcShortageScreenComponent
    /**
     * Экранный компонент структуры сплэш
     */
    fun splashScreenComponent(): SplashScreenComponent
    /**
     * Компонент для замеров
     */
    fun measurementsComponent(activityModule: ActivityModule): MeasurementsComponent
    /**
     * Передает [ClientProvider] текущего пользователя
     */
    fun authWorkerProxy(): AuthWorkerProxyImpl
    /**
     * Реализация интерфейса [MainDrawerDelegateFactory]
     */
    fun mainDrawerDelegateFactory(): MainDrawerDelegateFactoryImpl
    /**
     * Класс осуществляющий навигацию между экранами
     */
    fun appNavigator(): AppNavigator
    /**
     * Навигация для МП Сотрудник
     */
    fun navigator(): Navigator

    companion object {
        private lateinit var instance: AppComponent

        fun get(): AppComponent = instance

        fun set(appComponent: AppComponent) {
            instance = appComponent
        }
    }
}