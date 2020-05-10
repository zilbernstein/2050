package ru.digipeople.locotech.metrologist.ui.activity.measurementwheelpairs.interactor

import ru.digipeople.locotech.metrologist.data.model.*
import javax.inject.Inject
/**
 * Класс осуществляет проверку соответствия данных норме
 */
class WheelParamValueValidator @Inject constructor() {
    /**
     * Проверка левого параметра
     */
    fun validateLeft(wheelParam: WheelParam<*>) = validate(wheelParam, true)
    /**
     * Проверка правого параметра
     */
    fun validateRight(wheelParam: WheelParam<*>) = validate(wheelParam, false)
    /**
     * Сравнение с нормой параметра
     */
    private fun validate(wheelParam: WheelParam<*>, left: Boolean) = when (wheelParam) {
        is BooleanWheelParam -> validateComparable(wheelParam, left)
        is DateTimeWheelParam -> validateComparable(wheelParam, left)
        is NumericWheelParam -> validateComparable(wheelParam, left)
        is TextWheelParam -> validateComparable(wheelParam, left)
    }
    /**
     * Процедура сравнения
     */
    private fun <T : Comparable<T>> validateComparable(wheelParam: WheelParam<T>, left: Boolean): Result {
        val value = if (left) {
            wheelParam.leftValue
        } else {
            wheelParam.rightValue
        }

        if (value == null) {
            return Result.EMPTY
        }

        val minValue = wheelParam.minValue
        val maxValue = wheelParam.maxValue

        return if (minValue != null && maxValue != null) {
            if (value in minValue..maxValue) {
                Result.VALID
            } else {
                Result.OUT_OF_RANGE
            }
        } else if (minValue != null) {
            if (value >= minValue) {
                Result.VALID
            } else {
                Result.OUT_OF_RANGE
            }
        } else if (maxValue != null) {
            if (value <= maxValue) {
                Result.VALID
            } else {
                Result.OUT_OF_RANGE
            }
        } else {
            val normValue = wheelParam.normValue
            if (value == normValue) {
                Result.VALID
            } else {
                Result.OUT_OF_RANGE
            }
        }
    }
    /**
     * Результаты проверки
     */
    enum class Result {
        EMPTY,
        /**
         * За границами нормы
         */
        OUT_OF_RANGE,
        /**
         * В норме
         */
        VALID,
    }
}


