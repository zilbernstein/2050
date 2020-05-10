package ru.digipeople.ui.delegate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import ru.digipeople.logger.Logger;
import ru.digipeople.logger.LoggerFactory;
import ru.digipeople.ui.R;

/**
 * Базовый класс для делегатов для проверки Runtime Permissions.
 *
 * @author Kashonkov Nikita
 */
public abstract class BaseRtPermissionsDelegate {
    /**
     * Подключение логгирования
     */
    private static final Logger logger = LoggerFactory.getLogger(BaseRtPermissionsDelegate.class);

    //region Request codes
    private static final int RC_PERMISSION_AFTER_RATIONALE = 102;
    private static final int RC_PERMISSION = 103;
    //endregion
    //region SavedState
    private static final String SS_PERMISSION_DELEGATE_BUNDLE = "SS_PERMISSION_DELEGATE_BUNDLE";
    private static final String SS_REQUEST_IN_PROGRESS = "SS_REQUEST_IN_PROGRESS";
    private static final String SS_LIGHT_RATIONALE_SHOWN = "SS_LIGHT_RATIONALE_SHOWN";
    private static final String SS_HARD_RATIONALE_SHOWN = "SS_HARD_RATIONALE_SHOWN";
    //endregion

    private final Activity activity;

    private Callback callback;
    private AlertDialog lightRationaleAlertDialog;
    private AlertDialog hardRationaleAlertDialog;

    private boolean requestInProgress;
    private boolean pausedForRequest;
    private boolean isPermissionRequested = false;

    public BaseRtPermissionsDelegate(Activity activity) {
        this.activity = activity;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    /**
     * Действия при старте активити
     */
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(SS_PERMISSION_DELEGATE_BUNDLE);
            if (bundle != null) {
                requestInProgress = savedInstanceState.getBoolean(SS_REQUEST_IN_PROGRESS);
            }
        }
    }
    /**
     * Действия при возобновлении активити
     */
    public void onResume() {
        if(!isPermissionRequested){
            return;
        }
        if (pausedForRequest) {
            pausedForRequest = false;
        } else {
            checkPermissions();
        }
    }
    /**
     * Действия при приостановке активити
     */
    public void onPause() {
        pausedForRequest = requestInProgress;
    }
    /**
     * Запрос разрешений
     */
    public void requestPermissions(){
        isPermissionRequested = true;
        if (pausedForRequest) {
            pausedForRequest = false;
        } else {
            checkPermissions();
        }
    }
    /**
     * сохраненеи состояния
     */
    public void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(SS_REQUEST_IN_PROGRESS, requestInProgress);
        bundle.putBoolean(SS_LIGHT_RATIONALE_SHOWN, lightRationaleAlertDialog != null);
        bundle.putBoolean(SS_HARD_RATIONALE_SHOWN, hardRationaleAlertDialog != null);
        outState.putBundle(SS_PERMISSION_DELEGATE_BUNDLE, bundle);
    }

    public void onDestroy() {
        dismissRationaleHardDialog();
        dismissRationaleLightDialog();
    }
    /**
     * Прповерка разрешений
     */
    private void checkPermissions() {
        logger.trace("checkPermissions");
        isPermissionRequested = false;
        if (requestInProgress) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = getPermissionNames();
            List<String> grantedPermissions = new ArrayList<>();
            List<String> notGrantedRationalePermissions = new ArrayList<>();
            List<String> notGrantedPermissions = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions.add(permission);
                } else if (activity.shouldShowRequestPermissionRationale(permission)) {
                    notGrantedRationalePermissions.add(permission);
                } else {
                    notGrantedPermissions.add(permission);
                }
            }
            if (!notGrantedPermissions.isEmpty()) {
                requestPermissions(getPermissionNames(), RC_PERMISSION);
            } else if (!notGrantedRationalePermissions.isEmpty()) {
                showRationaleLightDialog();
            } else {
                callback.onPermissionRequestFinished(true);
            }
        } else {
            callback.onPermissionRequestFinished(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions(@NonNull String[] permissions, int requestCode) {
        requestInProgress = true;
        activity.requestPermissions(permissions, requestCode);
    }
    /**
     * Обработка результата запроса разрешения
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((requestCode == RC_PERMISSION || requestCode == RC_PERMISSION_AFTER_RATIONALE)) {
            requestInProgress = false;
            List<String> grantedPermissions = new ArrayList<>();
            List<String> notGrantedHardPermissions = new ArrayList<>();
            List<String> notGrantedPermissions = new ArrayList<>();
            if (grantResults.length == getPermissionNames().length) {
                for (int i = 0; i < permissions.length; i++) {
                    int grantResult = grantResults[i];
                    /**
                     * Разрешение выдано
                     */
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        grantedPermissions.add(permissions[i]);
                        continue;
                    }
                    if (!activity.shouldShowRequestPermissionRationale(permissions[i]) && requestCode == RC_PERMISSION) {
                        /**
                         * В резрешении отказано
                         */
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
                            notGrantedHardPermissions.add(permissions[i]);
                            continue;
                        }
                    }
                    notGrantedPermissions.add(permissions[i]);
                }/**
                 * Нет запрашиваемых разрешений
                 */
                if (!notGrantedPermissions.isEmpty()) {
                    callback.onPermissionRequestFinished(false);
                } else if (!notGrantedHardPermissions.isEmpty()) {
                    showRationaleHardDialog();
                } else {
                    callback.onPermissionRequestFinished(true);
                }
                return;
            }
            callback.onPermissionRequestFinished(false);
        }
    }
    /**
     * Отображение диалогового окна
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void showRationaleLightDialog() {
        dismissRationaleHardDialog();
        if (lightRationaleAlertDialog != null) {
            return;
        }
        lightRationaleAlertDialog = new AlertDialog.Builder(activity)
                .setMessage(getRationaleLight())
                /**
                 * Кнопка продолжить
                 */
                .setPositiveButton(R.string.common_rt_permissions_rationale_light_continue,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(getPermissionNames(), RC_PERMISSION_AFTER_RATIONALE);
                            }
                        })
                /**
                 * Кнопка отменить
                 */
                .setNegativeButton(R.string.common_rt_permissions_rationale_light_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onPermissionRequestFinished(false);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dismissRationaleLightDialog();
                    }
                })
                .setCancelable(false)
                .show();
    }
    /**
     * закрытие диалога
     */
    private void dismissRationaleLightDialog() {
        if (lightRationaleAlertDialog != null) {
            lightRationaleAlertDialog.dismiss();
            lightRationaleAlertDialog = null;
        }
    }
    /**
     * отображение диалога
     */
    private void showRationaleHardDialog() {
        dismissRationaleLightDialog();
        if (hardRationaleAlertDialog != null) {
            return;
        }
        hardRationaleAlertDialog = new AlertDialog.Builder(activity)
                .setMessage(getRationaleHard())
                /**
                 * Кнопка настойки
                 */
                .setPositiveButton(R.string.common_rt_permissions_rationale_hard_continue,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                navigateToAppSettings(activity);
                            }
                        })
                /**
                 * Кнопка отмена
                 */
                .setNegativeButton(R.string.common_rt_permissions_rationale_hard_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callback.onPermissionRequestFinished(false);
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dismissRationaleHardDialog();
                    }
                })
                .setCancelable(false)
                .show();
    }
    /**
     * Хакрытие диалога
     */
    private void dismissRationaleHardDialog() {
        if (hardRationaleAlertDialog != null) {
            hardRationaleAlertDialog.dismiss();
            hardRationaleAlertDialog = null;
        }
    }
    /**
     * Переход к настрокам
     */
    private void navigateToAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    /**
     * Возвращает наименования разрешений, с которыми работает делегат.
     */
    protected abstract String[] getPermissionNames();

    /**
     * Возвращает разъяснение о необходимости разрешения, когда
     * пользователь уже отказал без флага "Больше не спрашивать"
     */
    @StringRes
    protected abstract int getRationaleLight();

    /**
     * Возвращает разъяснение о необходимости разрешения, когда
     * пользователь уже отказал с флагом "Больше не спрашивать"
     */
    @StringRes
    protected abstract int getRationaleHard();

    public interface Callback {
        /**
         * Вызывается по заверешнию проверки разрешений
         *
         * @param granted {@code true} если рпзрешения были предоставлены, {@code false} иначе
         */
        void onPermissionRequestFinished(boolean granted);
    }
}
