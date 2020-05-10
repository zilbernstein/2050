package ru.digipeople.locotech.inspector.ui.activity.print.adapter
/**
 * Модель категории подписантов
 */
class SignersCategoryModel {
    /**
     * Id категории
     */
    var id: String = ""
    /**
     * Наименование категории
     */
    var title: String = ""
    /**
     * Список подписантов в категории
     */
    var signers = mutableListOf<Signer>()
}

class Signer {
    /**
     * Id подписанта
     */
    var id: String = ""
    /**
     * Имя подписанта
     */
    var name: String = ""
}