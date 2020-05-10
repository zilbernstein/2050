package ru.digipeople.locotech.inspector

import dagger.Component
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.inspector.api.ThingsWorxWorker
import ru.digipeople.locotech.inspector.helper.AuthWorkerProxyImpl
import ru.digipeople.locotech.inspector.support.SessionManager
import ru.digipeople.locotech.inspector.ui.activity.ActivityNavigator
import ru.digipeople.locotech.inspector.ui.activity.AppNavigator
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.base.MainDrawerDelegateFactoryImpl
import ru.digipeople.locotech.inspector.ui.activity.checklist.CheckListScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.comment.CommentScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.controlpoint.ControlPointScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.createremarkphoto.CreateRemarkPhotoScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.declinereason.DeclineReasonScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.inspection.InspectionScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.measurement.MeasurementScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.print.PrintScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.remarksearch.RemarkSearchScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.splash.SplashScreenComponent
import ru.digipeople.locotech.inspector.ui.activity.summary.SummaryScreenComponent
import ru.digipeople.locotech.inspector.ui.helper.ClientProvider
import ru.digipeople.locotech.master.ui.activity.searchperformer.SignerSearchScreenComponent
import ru.digipeople.message.MessagesComponent
import ru.digipeople.photo.PhotoComponent
import ru.digipeople.telephonebook.TelephoneBookComponent
import ru.digipeople.thingworx.BaseThingWorx
import ru.digipeople.thingworx.ThingWorxComponent
import ru.digipeople.thingworx.subscription.SubscriptionProvider
import ru.digipeople.thingworx.subscription.SubscriptionThing
import ru.digipeople.ui.UiComponent
import javax.inject.Singleton
/**
 * Интерфейс МП Выпуск на линию
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
            CoreAppComponent::class
        ]
)
interface AppComponent {
    fun inject(appInitProvider: AppInitProvider)

    fun activityNavigator(): ActivityNavigator

    fun splashScreenComponent(): SplashScreenComponent

    fun inspectionScreenComponent(): InspectionScreenComponent

    fun declineReasonScreenComponent(): DeclineReasonScreenComponent

    fun controlPointScreenComponent(): ControlPointScreenComponent

    fun remarkSearchScreenComponent(): RemarkSearchScreenComponent

    fun checkListScreenComponent(): CheckListScreenComponent

    fun createRemarkPhotoScreenomponent(): CreateRemarkPhotoScreenComponent

    fun printScreenComponent(): PrintScreenComponent

    fun summaryScreenComponent(): SummaryScreenComponent

    fun commentScreenComponent(): CommentScreenComponent

    fun signerSearchScreeenComponent(): SignerSearchScreenComponent

    fun baseThingWorx(): BaseThingWorx

    fun thingWorxWorker(): ThingsWorxWorker

    fun authWorkerProxy(): AuthWorkerProxyImpl

    fun measurementComponent(): MeasurementScreenComponent

    fun mainDrawerDelegateFactory(): MainDrawerDelegateFactoryImpl

    fun navigator(): Navigator

    fun appNavigator(): AppNavigator

    fun sessionManager(): SessionManager

    fun subscriptionProvider(): SubscriptionProvider

    fun subscriptionThing(): SubscriptionThing

    companion object {
        private lateinit var instance: AppComponent

        fun get(): AppComponent = instance

        fun set(appComponent: AppComponent) {
            instance = appComponent
        }
    }

}