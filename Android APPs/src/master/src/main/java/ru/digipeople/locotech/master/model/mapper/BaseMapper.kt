package ru.digipeople.locotech.master.model.mapper

/**
 * Базовый маппер
 *
 * @author Kashonkov Nikita
 */
abstract class BaseMapper<E, M> {
    abstract fun entityToModel(entity: E?): M?

    abstract fun entityListToModelList(entities: List<E>): List<M>

}