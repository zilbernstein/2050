package ru.digipeople.locotech.technologist.mapper.base

/**
 * Базовый маппер
 *
 * @author Sostavkin Grisha
 */
abstract class BaseMapper<E, M> {
    /**
     * преобразование сущности в модель
     */
    abstract fun entityToModel(entity: E?): M?
    /**
     * Преобразование списка сущностей в список моделей
     */
    abstract fun entityListToModelList(entities: List<E>): List<M>

}