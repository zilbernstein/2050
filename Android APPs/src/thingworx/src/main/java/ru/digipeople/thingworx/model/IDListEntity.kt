package ru.digipeople.thingworx.model

/**
 * Модель обертка для передачи на сервера списка ID
 *
 * @author Kashonkov Nikita
 */
data class IDListEntity(val idList: Collection<String>)