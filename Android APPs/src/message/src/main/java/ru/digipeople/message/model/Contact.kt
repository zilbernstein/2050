package ru.digipeople.message.model

/**
 * Модель контакта
 *
 * @author Kashonkov Nikita
 */
class Contact {
    /**
     * Id контакта
     */
    lateinit var id: String
    /**
     * ФИО контакта
     */
    lateinit var name: String
    /**
     * Флаг выбранности
     */
    var isSelected = false
    /**
     * Перегрузка метода сравнения
     */
    override fun equals(other: Any?): Boolean {
        if (other !is Contact) return false
        if (other.id == this.id) return true
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}