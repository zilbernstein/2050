package ru.digipeople.locotech.metrologist.data.model

import java.util.*
/**
 * Модель параметра
 */
sealed class Parameter<T> {
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
     * значение параметра
     */
    var value: T? = null
    /**
     * порядок для сортировки
     */
    var order: Int = 0
}

class BooleanParameter : Parameter<Boolean>()

class DateTimeParameter : Parameter<Date>()

class NumericParameter : Parameter<Float>()

class TextParameter : Parameter<String>()
