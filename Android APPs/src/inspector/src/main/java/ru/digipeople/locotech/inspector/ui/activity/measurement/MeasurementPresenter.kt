package ru.digipeople.locotech.inspector.ui.activity.measurement

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.locotech.core.data.model.Measurement
import ru.digipeople.locotech.core.data.model.Stage
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.measurement.adapter.DividerModel
import ru.digipeople.locotech.inspector.ui.activity.measurement.adapter.MeasurementModel
import ru.digipeople.locotech.inspector.ui.activity.measurement.adapter.TitleModel
import ru.digipeople.locotech.inspector.ui.activity.measurement.interactor.MeasurementsLoader
import ru.digipeople.logger.Logger
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.ui.mvp.presenter.BaseMvpViewStatePresenter
import javax.inject.Inject

/**
 * Презентер замеров
 *
 * @author Sostavkin Grisha
 */
class MeasurementPresenter @Inject constructor(viewState: MeasurementViewState,
                                               private val measurementsLoader: MeasurementsLoader,
                                               private val navigator: Navigator)
    : BaseMvpViewStatePresenter<MeasurementView, MeasurementViewState>(viewState) {

    private val logger: Logger = LoggerFactory.getLogger(MeasurementPresenter::class)
    private var measurementDisposable: Disposable = Disposables.disposed()

    override fun onInitialize() {}

    fun onScreenShow(workId: String) {
        loadData(workId)
    }

    override fun destroy() {
        measurementDisposable.dispose()
        super.destroy()
    }

    fun loadData(workId: String) {
        measurementDisposable.dispose()
        measurementDisposable = measurementsLoader.loadMeasurements(workId)
                .map { result ->
                    if (result.isSuccessful) {
                        val data = mutableListOf<Any>()
                        val controlList = mutableListOf<MeasurementModel>()
                        val beforeRepairList = mutableListOf<MeasurementModel>()
                        val afterRepairList = mutableListOf<MeasurementModel>()

                        //Distribute measurements by stage
                        result.items.forEach { measurement ->
                            when (measurement.stage) {
                                Stage.CONTROL -> controlList.add(MeasurementModel(measurement))
                                Stage.BEFORE_REPAIR -> beforeRepairList.add(MeasurementModel(measurement))
                                Stage.AFTER_REPAIR -> afterRepairList.add(MeasurementModel(measurement))
                            }
                        }

                        data.apply {
                            if (controlList.isNotEmpty()) {
                                add(TitleModel(Stage.CONTROL.title))
                                addAll(controlList)
                                add(DividerModel)
                            }
                            if (beforeRepairList.isNotEmpty()) {
                                add(TitleModel(Stage.BEFORE_REPAIR.title))
                                addAll(beforeRepairList)
                                add(DividerModel)
                            }
                            if (afterRepairList.isNotEmpty()) {
                                add(TitleModel(Stage.AFTER_REPAIR.title))
                                addAll(afterRepairList)
                                add(DividerModel)
                            }
                        }

                        return@map Pair(data, result.userError)
                    } else return@map Pair(null, result.userError)
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { view.setLoading(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    val (data, userError) = result
                    view.setLoading(false)
                    if (data != null) view.setData(data)
                    else view.showError(userError.message)
                }, {
                    view.setLoading(false)
                    logger.error(it)
                })
    }

    fun onBackBtnClicked() {
        navigator.navigateBack()
    }

    fun onCommentCLicked(measure: Measurement) {
        navigator.navigateToReadOnlyComment(measure.measurementComment.text)
    }


}