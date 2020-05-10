package ru.digipeople.locotech.master.helper

import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import ru.digipeople.logger.LoggerFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 * Обработчик [UndeliverableException] для RX.
 */
class RxErrorHandler : Consumer<Throwable> {

    private val logger = LoggerFactory.getLogger(RxErrorHandler::class.java)

    override fun accept(throwable: Throwable) {
        val original = if (throwable is UndeliverableException) {
            throwable.cause
        } else {
            throwable
        }
        when (original) {
            is UnknownHostException -> {
                /**
                 * ошибка неизвестный хост
                 */
                logger.trace("skipping UnknownHostException")
            }
            is ConnectException -> {
                /**
                 * ошибка соединения
                 */
                logger.trace("skipping ConnectException")
            }
            is SSLHandshakeException -> {
                /**
                 * ошибка подтверждения ssl
                 */
                logger.trace("skipping SSLHandshakeException")
            }
            is SocketTimeoutException -> {
                /**
                 * ошибка таймаута соединения
                 */
                logger.trace("skipping SocketTimeoutException")
            }
            is TimeoutException -> {
                // Прилетает от ThingWorx SDK при вызове Thread.interrupt()
                // CommunicationEndpoint.sendRequest(CommunicationEndpoint.java:533)
                logger.trace("skipping TimeoutException")
            }
            else -> {
                throw RuntimeException(throwable)
            }
        }
    }
}