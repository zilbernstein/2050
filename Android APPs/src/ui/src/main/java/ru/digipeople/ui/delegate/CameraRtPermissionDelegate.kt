package ru.digipeople.ui.delegate

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import ru.digipeople.ui.R
import javax.inject.Inject

/**
 * Делегат для разрешения к камере
 *
 * @author Kashonkov Nikita
 */
class CameraRtPermissionDelegate @Inject constructor(activity: AppCompatActivity) : BaseRtPermissionsDelegate(activity) {
    /**
     * Получить спискок наимениований требуемых разрешений
     */
    override fun getPermissionNames(): Array<String> {
        return arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    /**
     * Простой диалог (оповещение)
     */
    override fun getRationaleLight(): Int {
        return R.string.create_photo_permission_rationale_light
    }
    /**
     * Более сложный
     */
    override fun getRationaleHard(): Int {
        return R.string.create_photo_permission_rationale_hard
    }
}