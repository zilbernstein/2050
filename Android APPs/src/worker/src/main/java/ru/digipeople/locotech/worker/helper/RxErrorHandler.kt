package ru.digipeople.locotech.worker.helper

import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import ru.digipeople.logger.LoggerFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * Обработчик [UndeliverabeException] для RX
 *
 * @author Kashonkov Nikita
 */
class RxErrorHandler: Consumer<Throwable> {
    /**
     * Создание логгера
     */
    private val logger = LoggerFactory.getLogger(RxErrorHandler::class.java)

    override fun accept(throwable: Throwable) {
        val original: Throwable
        if (throwable is UndeliverableException) {
            original = throwable.cause!!
        } else {
            original = throwable
        }
        if (original is UnknownHostException) {
            logger.trace("skipping UnknownHostException")
        } else if (original is ConnectException) {
            logger.trace("skipping ConnectException")
        } else if (original is SSLHandshakeException) {
            logger.trace("skipping SSLHandshakeException")
        } else if (original is SocketTimeoutException) {
            logger.trace("skipping SocketTimeoutException")
        } else {
            throw RuntimeException(throwable)
        }
    }
}