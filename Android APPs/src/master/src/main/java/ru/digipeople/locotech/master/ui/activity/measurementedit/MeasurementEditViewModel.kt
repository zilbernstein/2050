package ru.digipeople.locotech.master.ui.activity.measurementedit

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.master.model.Indicator
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.measurement.MeasureParams
import ru.digipeople.locotech.master.ui.activity.measurementedit.interactor.MeasurementUploader
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * View model редактирования замера
 */
class MeasurementEditViewModel @Inject constructor(private val navigator: Navigator,
                                                   private val measurementUploader: MeasurementUploader) : BaseViewModel() {

    //region Params
    private var measureDisposables: Disposable = Disposables.disposed()
    //endregion
    //region LiveData
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    //endregion
    override fun onStart() {}
    /**
     * обработка нажатия на кнопку принять
     */
    fun onApplyBtnClicked(measureParams: MeasureParams, value: String) {
        /**
         * контейнер для отправки измениния замера
         */
        val indicator = Indicator(measureParams.measureId, measureParams.characteristicId, measureParams.measureStage.value, value, measureParams.commentText)
        /**
         * изменение значения замера
         */
        measureDisposables = measurementUploader.editMeasure(measureParams.workId, indicator)
                .subscribeOn(AppSchedulers.io())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * обработка результата
                     */
                    loadingLiveData.value = false
                    if (result.isSuccessful) {
                        navigator.navigateToMeasurement(measureParams.workId, measureParams.workName, measureParams.workStatus)
                    } else {
                        /**
                         * отправка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
}