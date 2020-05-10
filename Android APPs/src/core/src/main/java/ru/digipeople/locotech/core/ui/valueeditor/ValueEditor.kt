package ru.digipeople.locotech.core.ui.valueeditor

/**
 * Интерфейс для редактирования значения
 *
 * @author Aleksandr Brazhkin
 */
interface ValueEditor<T> {
    var value: T?
    var onValueChangedListener: ((value: T?) -> Unit)?
    var format: ((value: T?) -> String)
}