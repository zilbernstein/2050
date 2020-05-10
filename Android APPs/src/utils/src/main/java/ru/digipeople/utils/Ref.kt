package ru.digipeople.utils

/**
 * Класс-холдер для nullable объекта.
 * Используется в основном для RxJava2, т.к. там запрещено
 * возвращение null-объектов в цепочке.
 *
 * @author Aleksandr Brazhkin
 */
class Ref<T>(private val value: T?) {
    /**
     * получение значения
     */
    fun get(): T? {
        return value
    }

    companion object {

        private val NULL_REF = Ref<Any>(null)

        fun <T> nullRef(): Ref<T> {
            @Suppress("UNCHECKED_CAST")
            return NULL_REF as Ref<T>
        }
    }

}