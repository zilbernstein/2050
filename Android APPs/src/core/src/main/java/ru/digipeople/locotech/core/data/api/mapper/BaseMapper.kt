package ru.digipeople.locotech.core.data.api.mapper

/**
 * Базовый маппер
 *
 * @author Kashonkov Nikita
 */
abstract class BaseMapper<E, M> {
    /**
     * маппер сущность - модель
     */
    abstract fun entityToModel(entity: E?): M?
    /**
     * маппер список сущностей - список моделей
     */
    abstract fun entityListToModelList(entities: List<E>): List<M>

}