package ru.digipeople.locotech.core.ui.activity

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import ru.digipeople.locotech.core.R
import ru.digipeople.locotech.core.ui.activity.auth.AuthActivity
import ru.digipeople.locotech.core.ui.activity.settings.SettingsActivity
import ru.digipeople.locotech.core.ui.external.planbik.ExternalPlanBik
import ru.digipeople.locotech.core.ui.helper.AbstractActivityLifecycleCallbacks
import java.net.URLEncoder
import java.util.*

/**
 * Базовый навигатор
 *
 * @author Aleksandr Brazhkin
 */
abstract class CoreNavigator(protected val app: Application) {
    private var _currentActivity: Activity? = null
    protected val currentActivity: Activity
        get() = _currentActivity!!

    private val queue = LinkedList<Command>()

    fun init() {
        app.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    protected fun addCommands(vararg commands: Command) {
        queue.addAll(commands)
        pollQueue()
    }

    private fun pollQueue() {
        if (this._currentActivity == null) {
            return
        }
        while (queue.isNotEmpty()) {
            queue.poll().apply()
        }
    }
    /**
     * Переход к предыдущему экрану
     */
    open fun navigateBack() {
        addCommands(BackCommand())
    }
    /**
     * Переход к экрану авторизации
     */
    open fun navigateToAuth() {
        addCommands(ToAuthCommand())
    }
    /**
     * Переход к экрану настроек
     */
    open fun navigateSettings() {
        addCommands(ToSettingsCommand())
    }
    /**
     * Переход к план БИК
     */
    open fun navigateToPlanBik() {
        addCommands(ToPlanBikCommand())
    }
    /**
     * Переход к pdf по ссылке
     */
    open fun navigateToPdfLink(url: String) {
        addCommands(ToPdfLinkCommand(url))
    }

    protected fun animForward(activity: Activity) {
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.zero_animation)
    }

    protected fun animBack(activity: Activity) {
        activity.overridePendingTransition(R.anim.zero_animation, R.anim.slide_to_right)
    }

    protected interface Command {
        fun apply()
    }

    private inner class BackCommand : Command {
        override fun apply() {
            currentActivity.finish()
        }
    }

    private inner class ToAuthCommand : Command {
        override fun apply() {
            /**
         * интент(для авторизации)
         */
            val intent = AuthActivity.getCallingIntent(currentActivity)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            currentActivity.startActivity(intent)
            animForward(currentActivity)
        }
    }

    private inner class ToSettingsCommand : Command {
        override fun apply() {
            /**
             * интент (для настроек)
             */
            val intent = SettingsActivity.getCallingIntent(currentActivity)
            currentActivity.startActivity(intent)
        }
    }

    private inner class ToPlanBikCommand : Command {
        override fun apply() {
            ExternalPlanBik().navigate(currentActivity)
        }
    }

    private inner class ToPdfLinkCommand(private val url: String) : Command {
        override fun apply() {
            /**
             * Интент для ссылок на pdf
             */
            val localViewerIntent = Intent(Intent.ACTION_VIEW)
            localViewerIntent.setDataAndType(Uri.parse(url), "application/pdf")

            try {
                currentActivity.startActivity(localViewerIntent)
                animForward(currentActivity)
            } catch (ex: ActivityNotFoundException) {
                val googleViewerIntent = Intent(Intent.ACTION_VIEW)
                val encodedUrl = URLEncoder.encode(url)
                googleViewerIntent.data = Uri.parse("https://docs.google.com/gview?embedded=true&url=$encodedUrl")
                try {
                    currentActivity.startActivity(googleViewerIntent)
                    animForward(currentActivity)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(currentActivity, R.string.core_navigator_no_web_client, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val lifecycleCallbacks = object : AbstractActivityLifecycleCallbacks() {

        override fun onActivityStarted(activity: Activity) {
            _currentActivity = activity
            pollQueue()
        }

        override fun onActivityStopped(activity: Activity) {
            if (_currentActivity === activity) {
                _currentActivity = null
            }
        }
    }
}