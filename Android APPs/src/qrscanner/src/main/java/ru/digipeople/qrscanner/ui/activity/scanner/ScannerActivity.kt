package ru.digipeople.qrscanner.ui.activity.scanner

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.BarcodeDetector
import ru.digipeople.qrscanner.R
import ru.digipeople.qrscanner.tracker.BarcodeTracker
import ru.digipeople.qrscanner.tracker.BarcodeTrackerFactory
import ru.digipeople.qrscanner.ui.view.camera.CameraSourcePreview
import ru.digipeople.qrscanner.ui.view.camera.GraphicOverlay
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.BaseRtPermissionsDelegate
import ru.digipeople.ui.delegate.CameraRtPermissionDelegate
import java.io.IOException
import javax.inject.Inject

/**
 * Активити сканер
 *
 * @author Sostavkin Grisha
 */
class ScannerActivity : MvpActivity(), ScannerView {
    //region Di
    private lateinit var screenComponent: ScannerScreenComponent
    lateinit var component: ScannerComponent
    @Inject
    lateinit var cameraRtPermissionsDelegate: CameraRtPermissionDelegate
    //endregion
    //region Other
    private val RC_HANDLE_GMS = 9001
    private lateinit var presenter: ScannerPresenter
    private lateinit var overlay: GraphicOverlay
    private lateinit var preview: CameraSourcePreview
    private var cameraSource: CameraSource? = null
    private var googleErrorDialog: Dialog? = null
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        cameraRtPermissionsDelegate.setCallback(rtPermissionsDelegateCallback)
        cameraRtPermissionsDelegate.onCreate(savedInstanceState)
        /**
         * Инициализация перзентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, ScannerPresenter::class.java)
        presenter.initialize()

        overlay = findViewById(R.id.overlay)
        preview = findViewById(R.id.camera_preview)

    }
    /**
     * Действия при возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        cameraRtPermissionsDelegate.onResume()
        cameraRtPermissionsDelegate.requestPermissions()
        startCameraSource()
    }
    /**
     * Действия при приотановке активити
     */
    override fun onPause() {
        super.onPause()
        preview.stop()
    }
    /**
     * обработка нажатиия назад
     */
    override fun onBackPressed() {
        super.onBackPressed()
    }
    /**
     * действия при уничтожении активити
     */
    override fun onDestroy() {
        if (googleErrorDialog != null) {
            googleErrorDialog!!.dismiss()
        }
        if (cameraSource != null) {
            cameraSource!!.release()
        }
        cameraRtPermissionsDelegate.onDestroy()

        super.onDestroy()
    }
    /**
     * Запрос разрешений
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        cameraRtPermissionsDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        cameraRtPermissionsDelegate.onSaveInstanceState(outState)
    }
    /**
     * Созранение результата
     */
    override fun setScanningResult(id: String) {
        val intent = Intent()
        intent.putExtra(DATA, id)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private val rtPermissionsDelegateCallback = object : BaseRtPermissionsDelegate.Callback {
        override fun onPermissionRequestFinished(granted: Boolean) {
            if (granted) {
                createCameraSource()
            } else {
                onBackPressed()
            }
        }
    }

    private fun createCameraSource() {

        val barcodeDetector = BarcodeDetector.Builder(baseContext).build()
        val barcodeTrackerFactory = BarcodeTrackerFactory(overlay, barcodeListener)
        barcodeDetector.setProcessor(MultiProcessor.Builder(barcodeTrackerFactory).build())

        //проверям подгружен ли сканер QR кодов. Он позгружается при установке приложения
        if (!barcodeDetector.isOperational) {
            val lowstorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = baseContext!!.registerReceiver(null, lowstorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(baseContext, "Мало памяти", Toast.LENGTH_LONG).show()
            }

        }

        cameraSource = CameraSource.Builder(baseContext, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedFps(15.0f)
                .build();
    }

    private fun startCameraSource() {
        //Проверяет доступность сервисов google без них не будет работать сканер
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            googleErrorDialog = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS)
            googleErrorDialog!!.show()
        }

        if (cameraSource != null) {
            try {
                preview.start(cameraSource as CameraSource, overlay)
            } catch (e: IOException) {
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    private fun getScreenComponent(): ScannerScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return ru.digipeople.qrscanner.ScannerComponent.get().scannerScreenComponent()
        } else {
            return saved as ScannerScreenComponent
        }

    }
    /**
     * Слушатель трекера
     */
    private val barcodeListener = object : BarcodeTracker.BarcodeTrackerListener {
        override fun barcodeDetected(text: String) {
            presenter.barcodeDetected(text)
        }
    }

    companion object {
        //region Extra
        val DATA = "DATA"
        //endRegion

        fun getCallingIntentForResult(context: Context): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, ScannerActivity::class.java)
            return intent
        }
        /**
         * Получение данных из интента
         */
        fun getResultFromIntent(resultCode: Int, intent: Intent?): String {
            if (resultCode == Activity.RESULT_OK) {
                val id = intent?.getStringExtra(DATA)
                if (id != null) {
                    return id
                } else {
                    return ""
                }
            } else {
                return ""
            }
        }
    }
}