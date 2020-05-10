package ru.digipeople.photo.model.mapper

/**
 * Базовый маппер
 *
 * @author Kashonkov Nikita
 */
abstract class BaseMapper<E, M> {
    /*
        * преобразование сущности в модель
        */
    abstract fun entityToModel(entity: E?): M?
    /*
            * преобразование списка сущностей к списку моделей
            */
    abstract fun entityListToModelList(entities: List<E>): List<M>

}