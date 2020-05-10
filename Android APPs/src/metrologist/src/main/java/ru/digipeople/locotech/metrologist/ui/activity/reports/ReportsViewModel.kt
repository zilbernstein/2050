package ru.digipeople.locotech.metrologist.ui.activity.reports

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.core.ui.base.BaseViewModel
import ru.digipeople.locotech.core.ui.base.SingleEventLiveData
import ru.digipeople.locotech.core.ui.helper.AuthInfoStorage
import ru.digipeople.locotech.metrologist.data.model.Report
import ru.digipeople.locotech.metrologist.ui.activity.reports.interactor.ReportSender
import ru.digipeople.locotech.metrologist.ui.activity.reports.interactor.ReportsLoader
import ru.digipeople.locotech.metrologist.ui.activity.reports.interactor.UserErrorBuilder
import ru.digipeople.utils.AppSchedulers
import ru.digipeople.utils.model.UserError
import java.util.regex.Pattern
import javax.inject.Inject
/**
 * View model отчетов
 */
class ReportsViewModel @Inject constructor(
        private val reportsLoader: ReportsLoader,
        private val reportSender: ReportSender,
        private val userErrorBuilder: UserErrorBuilder,
        private val authInfoStorage: AuthInfoStorage
) : BaseViewModel() {

    //region LiveData
    val reportsLiveData = MutableLiveData<List<Report>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val userErrorLiveData = SingleEventLiveData<UserError>()
    val noDataLiveData = MutableLiveData<Boolean>()
    val authInfoLiveData = MutableLiveData<Pair<String, String>>()
    //endregion
    private lateinit var report: Report
    private var reportsDisposable = Disposables.disposed()
    private var sendReportDisposable = Disposables.disposed()
    /**
     * Действия при старте
     */
    override fun onStart() {
        authInfoLiveData.value = authInfoStorage.loginPassPair
        loadData()
    }
    /**
     * Очистка
     */
    override fun onCleared() {
        super.onCleared()
        reportsDisposable.dispose()
        sendReportDisposable.dispose()
    }
    /**
     * Выбор отчета
     */
    fun onReportSelected(report: Report) {
        this.report = report
    }
    /**
     * Отправить отчет
     */
    fun onSendBtnClicked(emailAddress: String) {
        if (!isEmailValid(emailAddress)) {
            userErrorLiveData.value = userErrorBuilder.invalidEmail
            return
        }

        sendReportDisposable = reportSender.send(report.id, emailAddress)
                .subscribeOn(AppSchedulers.network())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ userError ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    if (userError != UserError.NO_ERROR) {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = userError
                    }
                }, { logger.error(it) })
    }
    /**
     * поверка почты на корректность
     */
    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
    /**
     * Загрузка данных
     */
    private fun loadData() {
        reportsDisposable = reportsLoader.load()
                .subscribeOn(AppSchedulers.network())
                .observeOn(AppSchedulers.ui())
                .doOnSubscribe { loadingLiveData.value = true }
                .subscribe({ result ->
                    /**
                     * Обработка результата
                     */
                    loadingLiveData.value = false
                    noDataLiveData.value = result.reports.isEmpty()
                    reportsLiveData.value = result.reports
                    if (!result.isSuccessful) {
                        /**
                         * Обработка ошибки
                         */
                        userErrorLiveData.value = result.userError
                    }
                }, { logger.error(it) })
    }
}