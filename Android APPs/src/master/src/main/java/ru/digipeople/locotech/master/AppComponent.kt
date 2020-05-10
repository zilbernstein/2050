package ru.digipeople.locotech.master

import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.master.api.ThingsWorxWorker
import ru.digipeople.locotech.master.helper.AuthWorkerProxyImpl
import ru.digipeople.locotech.master.helper.session.SessionManager
import ru.digipeople.locotech.master.ui.activity.ActivityNavigator
import ru.digipeople.locotech.master.ui.activity.AppNavigator
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.allworklist.AllWorkScreenComponent
import ru.digipeople.locotech.master.ui.activity.approved.ApprovedScreenComponent
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.AssignmentTemplatesComponent
import ru.digipeople.locotech.master.ui.activity.base.MainDrawerDelegateFactoryImpl
import ru.digipeople.locotech.master.ui.activity.checkwork.CheckWorkScreenComponent
import ru.digipeople.locotech.master.ui.activity.comment.CommentScreenComponent
import ru.digipeople.locotech.master.ui.activity.divide.DivideScreenComponent
import ru.digipeople.locotech.master.ui.activity.equipment.EquipmentScreenComponent
import ru.digipeople.locotech.master.ui.activity.groupassignment.GroupAssignmentScreenComponent
import ru.digipeople.locotech.master.ui.activity.measurement.MeasurementComponent
import ru.digipeople.locotech.master.ui.activity.partlyaccept.PartlyAcceptScreenComponent
import ru.digipeople.locotech.master.ui.activity.performance.PerformanceScreenComponent
import ru.digipeople.locotech.master.ui.activity.remark.RemarkScreenComponent
import ru.digipeople.locotech.master.ui.activity.remarksearch.RemarkSearchScreenComponent
import ru.digipeople.locotech.master.ui.activity.searchperformer.SearchPerformerScreenComponent
import ru.digipeople.locotech.master.ui.activity.settings.SettingsScreenComponent
import ru.digipeople.locotech.master.ui.activity.splash.SplashScreenComponent
import ru.digipeople.locotech.master.ui.activity.status.StatusScreenComponent
import ru.digipeople.locotech.master.ui.activity.tmcamount.TmcAmountScreenComponent
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.TmcBeforeAcceptScreenComponent
import ru.digipeople.locotech.master.ui.activity.tmclist.TmcListScreenComponent
import ru.digipeople.locotech.master.ui.activity.tmcsearch.TmcSearchScreenComponent
import ru.digipeople.locotech.master.ui.activity.urgent.UrgentScreenComponent
import ru.digipeople.locotech.master.ui.activity.workerspresence.WorkersPresenceComponent
import ru.digipeople.locotech.master.ui.activity.worklist.WorkListScreenComponent
import ru.digipeople.locotech.master.ui.activity.writeofftmcamount.WriteOffTmcAmountScreenComponent
import ru.digipeople.message.MessagesComponent
import ru.digipeople.photo.PhotoComponent
import ru.digipeople.qrscanner.ScannerComponent
import ru.digipeople.telephonebook.TelephoneBookComponent
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.ThingWorxComponent
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.mvp.core.MvpProcessor
import javax.inject.Singleton
/**
 * Интерфейс МП Мониторинг КП
 *
 * @author Kashonkov Nikita
 */
@Singleton
@Component(
        modules = [
            AppModule::class
        ],
        dependencies = [
            UiComponent::class,
            ThingWorxComponent::class,
            TelephoneBookComponent::class,
            MessagesComponent::class,
            PhotoComponent::class,
            ScannerComponent::class,
            CoreAppComponent::class
        ])
interface AppComponent {
    fun inject(appInitProvider: AppInitProvider)
    /**
     * Класс, управляющий презентерами.
     */
    fun mvpProcessor(): MvpProcessor
    /**
     * Навигатор для активити
     */
    fun activityNavigator(): ActivityNavigator
    /**
     * Компонент срочно
     */
    fun urgentScreenComponent(): UrgentScreenComponent
    /**
     * Экранный компонент согласование
     */
    fun approvedScreenComponent(): ApprovedScreenComponent
    /**
     *Экранный компонент исполнения
     */
    fun perfomanceScreenComponent(): PerformanceScreenComponent
    /**
     * Компонент статуса работ
     */
    fun statusScreenComponent(): StatusScreenComponent
    /**
     * Компонент настроек
     */
    fun settingsScreenComponent(): SettingsScreenComponent
    /**
     * Экранный компонент локомотивов на учатске
     */
    fun locomotiveScreenComponent(): EquipmentScreenComponent
    /**
     * Экранный компонент экрана заставки
     */
    fun splashScreenComponent(): SplashScreenComponent
    /**
     * Экранный компонент замечаний
     */
    fun locomotiveDetailScreenComponent(): RemarkScreenComponent
    /**
     * Компонент добавления / создания замечания
     */
    fun remarkSearchScreenComponent(): RemarkSearchScreenComponent
    /**
     * Компонент выбора сотрудника / исполнителя
     */
    fun searchPerformerScreenComponent(): SearchPerformerScreenComponent
    /**
     * Экранный компонент добавления замечания / работ
     */
    fun workListScreenComponent(): WorkListScreenComponent
    /**
     * Экранный компонент проверки выбранных работ
     */
    fun checkWorkScreenComponent(): CheckWorkScreenComponent
    /**
     * Экранный компонент списка работ
     */
    fun allWorkScreenComponent(): AllWorkScreenComponent
    /**
     * Экранный компонент комментариев
     */
    fun commentScreenComponent(): CommentScreenComponent
    /**
     * Экранный компонент поиска ТМЦ
     */
    fun tmcSearchScreenComponent(): TmcSearchScreenComponent
    /**
     * Экранный компонент списка ТМЦ
     */
    fun tmcListScreenComponent(): TmcListScreenComponent
    /**
     * Экранный компонент ввода/изменения количества ТМЦ
     */
    fun tmcAmountScreenComponent(): TmcAmountScreenComponent
    /**
     * Экранный компонент списание ТМЦ
     */
    fun tmcBeforeAcceptScreenComponent(): TmcBeforeAcceptScreenComponent
    /**
     * Экранный компонент списания ТМЦ
     */
    fun writeOffTmcAmountScreenComponent(): WriteOffTmcAmountScreenComponent
    /**
     * Экранный компонент разделения работы
     */
    fun divideScreenComponent(): DivideScreenComponent
    /**
     * Экранный компонентчастичной приемки
     */
    fun partlyAcceptScreenComponent(): PartlyAcceptScreenComponent
    /**
     * Экранный компонент группового назначения исполнителей
     */
    fun groupAssignmentScreenComponent(): GroupAssignmentScreenComponent
    /**
     * Компонент выбора шаблона исполнителей
     */
    fun assignmentTemplatesComponent(activityModule: ActivityModule): AssignmentTemplatesComponent
    /**
     * Компонент замеров
     */
    fun measurementComponent(activityModule: ActivityModule): MeasurementComponent
    /**
     * Компонент явки сотрудников
     */
    fun workersPresenceComponent(activityModule: ActivityModule): WorkersPresenceComponent
    /**
     * Класс дл работы с системой BaseThingWorx.
     */
    fun baseThingWorx(): BaseThingWorx
    /**
     * Передает [ClientProvider] текущего пользователя
     **/
    fun authWorkerProxy(): AuthWorkerProxyImpl
    /**
     * Интерфейс для осуществления подписки/отписки на обновлении данных
     */
    fun subscriptionProvider(): SubscriptionProvider
    /**
     * Интерфейс для [ThingsWorxWorker]
     */
    fun thingWorxWorker(): ThingsWorxWorker
    /**
     * Реализация интерфейса [MainDrawerDelegateFactory]
     */
    fun mainDrawerDelegateFactory(): MainDrawerDelegateFactoryImpl
    /**
     * Класс осуществляющий навигацию между экранами
     */
    fun appNavigator(): AppNavigator
    /**
     * Менеджер пользователя, хранящий изменения id и названия выбранной секции
     */
    fun sessionManager(): SessionManager
    /**
     * Навигация для МП Мастер
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