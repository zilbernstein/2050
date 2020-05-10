package ru.digipeople.telephonebook.model

import com.volcaniccoder.volxfastscroll.ValueArea

/**
 * Модель контакт
 *
 * @author Sostavkin Grisha
 */
class Contact {
    /**
     * Id пользователя
     *
     */
    lateinit var id: String
    /**
     * Имя абонента
     *
     */
    @ValueArea
    lateinit var name: String
    /**
     * Телефон абонента
     *
     */
    lateinit var phoneNumber: String
    /**
     * Должность
     *
     */
    lateinit var job: String
}