package ru.digipeople.ui.activity.base

/**
 * Базовый класс для фрагментов
 *
 * @author Kashonkov Nikita
 */
abstract class FragmentActivity: MvpActivity() {
    /**
     * управление видимостью лоадера
     */
    abstract fun setLoadingVisibility(isVisible: Boolean)
    /**
     * отображение ошибки
     */
    abstract fun showError(message: String)
}