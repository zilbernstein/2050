package ru.digipeople.ui.delegate

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.ui.R
import javax.inject.Inject

/**
 * Делегат для разрешения звонков
 */
class CallPhoneRtPermissionDelegate @Inject constructor(activity: AppCompatActivity) : BaseRtPermissionsDelegate(activity) {
    /**
     * Получить спискок наимениований требуемых разрешений
     */
    override fun getPermissionNames(): Array<String> = arrayOf(Manifest.permission.CALL_PHONE)
    /**
     * Простой диалог (оповещение)
     */
    override fun getRationaleLight(): Int = R.string.call_phone_permission_rationale_light
    /**
     * Более сложный
     */
    override fun getRationaleHard(): Int = R.string.call_phone_permission_rationale_hard
}