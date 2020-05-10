package ru.digipeople.locotech.metrologist.data.api.mapper
/**
 * Базовый маппер
 */
interface BaseMapper<E, M> {
    /**
     * Преобразование  сущности в модель
     */
    fun entityToModel(entity: E?): M?
    /**
     * Преобразование списка ущностей в список моделей
     */
    fun entityListToModelList(entities: List<E>): List<M>
}
