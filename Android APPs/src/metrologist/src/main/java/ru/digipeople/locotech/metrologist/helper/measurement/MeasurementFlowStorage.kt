package ru.digipeople.locotech.metrologist.helper.measurement

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.MeasurementInfo
import ru.digipeople.locotech.metrologist.data.model.Parameter
import ru.digipeople.locotech.metrologist.data.model.WheelPair
import javax.inject.Inject
import javax.inject.Singleton
/**
 * Хранилище для значения замера при передаче его между экранами
 *
 * @author Aleksandr Brazhkin
 */
@Singleton
class MeasurementFlowStorage @Inject constructor() {

    private var _measurementInfo: MeasurementInfo? = null
    var measurementInfo: MeasurementInfo?
        get() = _measurementInfo
        set(value) {
            _measurementInfo = value
            _measurementInfoChanges.onNext(Unit)
        }
    /**
     * Изменение информации о замере
     */
    private val _measurementInfoChanges = PublishSubject.create<Unit>()
    val measurementInfoChanges: Observable<Unit>
        get() = _measurementInfoChanges
    /**
     * Замер
     */
    val measurement: Measurement
        get() = measurementInfo!!.measurement
    val parameters: List<Parameter<*>>
        get() = measurementInfo!!.parameters

    private var _wheelPairs: List<WheelPair>? = null
    var wheelPairs: List<WheelPair>
        get() = _wheelPairs!!
        set(value) {
            _wheelPairs = value
            _wheelPairsChanges.onNext(Unit)
        }
    /**
     * Изменение колесной пары
     */
    private val _wheelPairsChanges = PublishSubject.create<Unit>()
    val wheelPairsChanges: Observable<Unit>
        get() = _wheelPairsChanges

    fun clear() {
        _measurementInfo = null
        _wheelPairs = null
    }
}