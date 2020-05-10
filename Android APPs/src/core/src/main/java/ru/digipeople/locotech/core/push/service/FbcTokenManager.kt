package ru.digipeople.locotech.core.push.service

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import ru.digipeople.locotech.core.data.CorePrefs
import ru.digipeople.logger.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Класс, управляющий обновлением FirebaseCloud токена.
 */
@Singleton
class FbcTokenManager @Inject constructor(private val corePrefs: CorePrefs) {
    /**
     * создание логгера
     */
    private val logger = LoggerFactory.getLogger(FbcTokenManager::class)

    val token: String
        get() = corePrefs.fbcToken

    /**
     * Флажок - происходят ли работы по обновлению токена в данный момент
     */
    private var isUpdating = AtomicBoolean()

    fun updateFbcTokenAsync() {
        if (isUpdating.getAndSet(true)) {
            return
        }
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { instanceIdResult ->
            if (!instanceIdResult.isSuccessful) {
                logger.error(instanceIdResult.exception)
                return@OnCompleteListener
            }
            val newToken = instanceIdResult.result?.token ?: ""
            /**
             * логирование и сохранение токкена
             */
            logger.trace("token is set to $newToken")
            corePrefs.fbcToken = newToken

            isUpdating.set(false)
        })
    }
}