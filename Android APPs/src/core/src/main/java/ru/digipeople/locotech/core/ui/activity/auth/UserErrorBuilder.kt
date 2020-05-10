package ru.digipeople.locotech.core.ui.activity.auth

import android.content.Context
import ru.digipeople.locotech.core.R
import ru.digipeople.thingworx.helper.SimpleApiUserErrorBuilder
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Билдер для ошибок
 *
 * @author Nikita Sychev
 **/
class UserErrorBuilder @Inject constructor(override val context: Context) : SimpleApiUserErrorBuilder(context) {
    /**
     * Отображение некотрых типов ошибок
     */
    val userNameMatchError: UserError
        get() = Error(context.getString(R.string.authoriztion_activity_username_symbol_error))

    val userPasswordMatchError: UserError
        get() = Error(context.getString(R.string.authoriztion_activity_password_symbol_error))

    val userNameLengthError: UserError
        get() = Error(context.getString(R.string.authoriztion_activity_username_length_error))

    val userPasswordLengthError: UserError
        get() = Error(context.getString(R.string.authoriztion_activity_password_length_error))



    private class Error(override val message: String) : UserError
}
