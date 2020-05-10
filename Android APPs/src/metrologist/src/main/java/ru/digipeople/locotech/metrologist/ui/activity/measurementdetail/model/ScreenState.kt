package ru.digipeople.locotech.metrologist.ui.activity.measurementdetail.model

import ru.digipeople.locotech.metrologist.data.model.Measurement
import ru.digipeople.locotech.metrologist.data.model.Parameter

/**
 * Класс - состояние экрана
 * @author Aleksandr Brazhkin
 */
class ScreenState(val measurement: Measurement, val parameters: List<Parameter<*>>)