package ru.digipeople.locotech.core.ui.activity.settings.interactor

import io.reactivex.Single
import ru.digipeople.locotech.core.api.ThingWorxUrlProvider
import ru.digipeople.locotech.core.ui.activity.settings.model.ThingWorxEndpoint
import javax.inject.Inject

/**
 * Загрузчик ссылок
 *
 * @author Sostavkin Grisha
 */
class UrlsLoader @Inject constructor(private val thingWorxUrlProvider: ThingWorxUrlProvider) {
    fun load(): Single<List<ThingWorxEndpoint>> = Single.fromCallable {
        thingWorxUrlProvider.all.map {
            ThingWorxEndpoint().apply {
                title = it.first
                url = it.second
            }
        }
    }
}