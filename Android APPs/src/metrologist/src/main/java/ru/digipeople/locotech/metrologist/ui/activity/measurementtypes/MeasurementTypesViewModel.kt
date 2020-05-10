package ru.digipeople.locotech.metrologist.ui.activity.measurementtypes

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.metrologist.data.model.MeasurementCategory
import ru.digipeople.locotech.metrologist.helper.session.SessionManager
import ru.digipeople.locotech.metrologist.ui.activity.AppNavigator
import ru.digipeople.locotech.metrologist.ui.activity.measurementtypes.interactor.CategoriesLoader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View Model типов замеров
 */
class MeasurementTypesViewModel @Inject constructor(
        private val categoriesLoader: CategoriesLoader,
        private val navigator: AppNavigator,
        private val sessionManager: SessionManager
) : BaseViewModel() {
    //region LiveData
    val measurementsCategoriesLiveData = MutableLiveData<List<MeasurementCategory>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val noDataLiveData = MutableLiveData<Boolean>()
    val sectionNameLiveData = MutableLiveData<String>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    //endregion
    private var loadDataDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        loadData()
        sectionNameLiveData.value = sessionManager.currentSectionName
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        loadDataDisposable.dispose()
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        loadDataDisposable = categoriesLoader.load()
                .subscribeOn(AppSchedulers.network())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    noDataLiveData.value = result.categories.isEmpty()
                    measurementsCategoriesLiveData.value = result.categories
                    if (!result.isSuccessful) {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }

    fun onCategoryClicked(category: MeasurementCategory) {
        /**
         * переход к деталке
         */
        navigator.navigateToMeasurementDetail(category.id)
    }
}