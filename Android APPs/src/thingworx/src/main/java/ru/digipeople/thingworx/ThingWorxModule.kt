package ru.digipeople.thingworx

import dagger.Module
import dagger.Provides
import ru.digipeople.thingworx.subscription.DefaultSubscriptionThingWorker
import ru.digipeople.thingworx.subscription.SubscriptionThing
import ru.digipeople.thingworx.subscription.SubscriptionThingWorker
import javax.inject.Singleton

/**
 * DI модуль для ThingWorx
 *
 * @author Kashonkov Nikita
 */
@Module
class ThingWorxModule(private val thingWorxConfigProvider: () -> ThingWorxConfigProvider) {

    @Provides
    @Singleton
    fun subscriptionThingWorker(baseThingWorx: BaseThingWorx, subscriptionThing: SubscriptionThing): SubscriptionThingWorker {
        return DefaultSubscriptionThingWorker(baseThingWorx, subscriptionThing)
    }

    @Provides
    fun thingWorxConfigProvider(): ThingWorxConfigProvider {
        return thingWorxConfigProvider.invoke()
    }
}