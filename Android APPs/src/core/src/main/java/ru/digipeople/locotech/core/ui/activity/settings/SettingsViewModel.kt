package ru.digipeople.locotech.core.ui.activity.settings

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.core.api.ThingWorxUrlProvider
import ru.digipeople.locotech.core.ui.activity.CoreNavigator
import ru.digipeople.locotech.core.ui.activity.settings.interactor.UrlsLoader
import ru.digipeople.locotech.core.ui.activity.settings.model.ThingWorxEndpoint
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import javax.inject.Inject

/**
 * ViewModel для экрана настроек
 */
class SettingsViewModel @Inject constructor(private val urlsLoader: UrlsLoader,
                                            private val thingWorxUrlProvider: ThingWorxUrlProvider,
                                            private val navigator: CoreNavigator) : BaseViewModel() {

    val urlLiveData = MutableLiveData<String>()
    val urlsLiveData = MutableLiveData<List<ThingWorxEndpoint>>()
    private var settingsDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        urlLiveData.value = thingWorxUrlProvider.current
        loadData()
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        settingsDisposable.dispose()
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        settingsDisposable = urlsLoader.load()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    urlsLiveData.value = result
                }, { logger.error(it) })
    }
    /**
     * Обработка нажатия на кнопку принять
     */
    fun onApplyBtnClicked(thingworxUrl: String) {
        thingWorxUrlProvider.current = thingworxUrl
        navigator.navigateBack()
    }

}