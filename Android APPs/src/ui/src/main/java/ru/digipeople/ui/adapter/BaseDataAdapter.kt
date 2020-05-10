package ru.digipeople.ui.adapter

/**
 * Базовый интерфейс для адаптера
 *
 * @author Kashonkov Nikita
 */
interface BaseDataAdapter<T>{
    fun setData(list: List<T>)
}