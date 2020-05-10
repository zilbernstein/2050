package ru.digipeople.locotech.inspector.helper

import android.content.Context
import ru.digipeople.locotech.inspector.R
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Конструктор пользовательских сообщений об ошибке
 */
class UserErrorBuilder @Inject constructor(protected val context: Context) {
    /**
     * ошибка - Должен быть выбран хотя бы один документ
     */
    val noDocumentsError: UserError
        get() = Error(context.getString(R.string.print_activity_no_documents))
    /**
     * ошибка - Должен быть выбран назначен хотя бы один ответственный
     */
    val noSignersError: UserError
        get() = Error(context.getString(R.string.print_activity_no_signers))
    /**
     * ошибка
     */
    private class Error(override val message: String) : UserError
}
