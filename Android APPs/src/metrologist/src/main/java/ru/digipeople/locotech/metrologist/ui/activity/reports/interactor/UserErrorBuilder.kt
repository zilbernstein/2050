package ru.digipeople.locotech.metrologist.ui.activity.reports.interactor

import android.content.Context
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.utils.model.UserErrorImpl
import javax.inject.Inject

/**
 * Класс обработчик ошибок отчетов
 *
 * @author Aleksandr Brazhkin
 */
class UserErrorBuilder @Inject constructor(context: Context) {
    val invalidEmail = UserErrorImpl(context.getString(R.string.reports_invalid_email))
}