package ru.digipeople.locotech.metrologist.data.model

import ru.digipeople.locotech.core.data.model.Performer
import java.util.*
/**
 * Модель параметров колесной пары
 *
 * @author Michael Solenov
 */
sealed class WheelParam<T> {
    /**
     * идентификатор параметра
     */
    lateinit var id: String
    /**
     * название параметра
     */
    lateinit var name: String
    /**
     * тип данных параметра
     */
    lateinit var type: ParameterType
    /**
     * значение для левого колеса
     */
    var leftValue: T? = null
    /**
     * значение для правого колеса
     */
    var rightValue: T? = null
    /**
     * нормативное значение
     */
    var normValue: T? = null
    /**
     * минимальное значение
     */
    var minValue: T? = null
    /**
     * максимальное значение
     */
    var maxValue: T? = null
    /**
     * дата изменения, в миллисекундах UTC
     */
    var dateChange: Long = 0L
    /**
     * автор изменений
     */
    lateinit var userChange: Performer

    protected fun fill(wheelParam: WheelParam<T>): WheelParam<T> {
        wheelParam.id = id
        wheelParam.name = name
        wheelParam.type = type
        wheelParam.leftValue = leftValue
        wheelParam.rightValue = rightValue
        wheelParam.normValue = normValue
        wheelParam.minValue = minValue
        wheelParam.maxValue = maxValue
        wheelParam.dateChange = dateChange
        wheelParam.userChange = userChange
        return wheelParam
    }

    abstract fun copy(): WheelParam<T>
}

class BooleanWheelParam : WheelParam<Boolean>() {
    override fun copy() = fill(BooleanWheelParam())
}

class DateTimeWheelParam : WheelParam<Date>() {
    override fun copy() = fill(DateTimeWheelParam())
}

class NumericWheelParam : WheelParam<Float>() {
    override fun copy() = fill(NumericWheelParam())
}

class TextWheelParam : WheelParam<String>() {
    override fun copy() = fill(TextWheelParam())
}