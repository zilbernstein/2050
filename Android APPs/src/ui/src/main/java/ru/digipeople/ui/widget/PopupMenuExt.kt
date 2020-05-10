package ru.digipeople.ui.widget

import android.widget.PopupMenu
import ru.digipeople.logger.LoggerFactory
/**
 * Расширение попап меню
 */
/**
 * Подключение логгирования
 */
private val logger = LoggerFactory.getLogger("PopupMenuExt")
/**
 * Установка иконок для менню
 */
fun PopupMenu.setForceShowIcon(forceShowIcon: Boolean) {
    try {
        val fields = javaClass.declaredFields
        for (field in fields) {
            if ("mPopup" == field.getName()) {
                field.isAccessible = true
                val menuPopupHelper = field.get(this)
                val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                setForceIcons.invoke(menuPopupHelper, forceShowIcon)
                break
            }
        }
    } catch (e: Exception) {
        logger.error(e)
    }
}