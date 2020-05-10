package ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.widget.ImageView
import android.widget.TextView
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.WriteOffTmc
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.adapter.AdapterData
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.adapter.TMCAdapter
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.adapter.TMCData
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.dialog.WriteOffTmcDialog
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити списание ТМЦ
 * @author Kashonkov Nikita
 */
class TMCBeforeAcceptActivity : MvpActivity(), TMCBeforeAcceptView {

    //region DI
    private lateinit var screenComponent: TmcBeforeAcceptScreenComponent
    private lateinit var component: TmcBeforeAcceptComponent
    @Inject
    lateinit var adapter: TMCAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var navigator: Navigator
    //endregion
    //region View
    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var backBtn: ImageView
    private lateinit var writeOff: TextView
    //end Region
    //region Other
    private var convertDisposables = Disposables.disposed()
    private lateinit var presenter: TMCBeforeAcceptPresenter
    var writeOffDialog: WriteOffTmcDialog? = null
    //end Region
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workIds = intent.getStringArrayListExtra(EXTRA_TMC_PARAMS)
        screenComponent = getScreenComponent()
        component = screenComponent.component()
                .activityModule(ActivityModule(this))
                .workIds(workIds)
                .build()
        component.inject(this)

        setContentView(R.layout.activity_tmc_before_accept)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, TMCBeforeAcceptPresenter::class.java)
        presenter.initialize()

        recycler = findViewById(R.id.recycler_view)
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.tmcClickListener = presenter::onItemClicked
        /**
         * Обработка нажатия на кнопку назад
         */
        backBtn = findViewById(R.id.back_button)
        backBtn.setOnClickListener { navigator.navigateBack() }

        writeOff = findViewById(R.id.write_off)
        writeOff.setOnClickListener {
            if (callingActivity != null) {
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                presenter.onCheckBtnClecked()
            }
        }
    }
    /**
     * Действи япри возвобновлении активити
     */
    override fun onResume() {
        super.onResume()
        presenter.onScreenShown()
        navigator.onResume(this)
    }
    /**
     * Действия при постановке активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recycler, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Установка данных
     */
    override fun setData(list: List<WriteOffTmc>) {
        convertDisposables.dispose()
        convertDisposables = Single.fromCallable {
            val data = AdapterData()

            list.forEach { writeOffTmc ->
                data.add(writeOffTmc.workName)
                writeOffTmc.tmcList.forEach { data.add(TMCData(it, writeOffTmc.workId)) }
            }

            data
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setLoadingVisibility(false)
                    adapter.setData(it)
                }, {
                    /**
                     * Отображегние ошибки
                     */
                    setLoadingVisibility(false)
                    showError(it.localizedMessage)
                })
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение диалога списания
     */
    override fun showWriteOffDialog(shouldShowOverrunWarning: Boolean) {
        if (writeOffDialog != null) {
            return
        }

        writeOffDialog = WriteOffTmcDialog(this, shouldShowOverrunWarning)
        writeOffDialog?.acceptListener = presenter::onAcceptCLicked
        writeOffDialog?.setOnDismissListener { writeOffDialog = null }
        writeOffDialog?.show()
    }

    /**
     * Получение экранного компонента
     */
    private fun getScreenComponent(): TmcBeforeAcceptScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().tmcBeforeAcceptScreenComponent()
        } else {
            return saved as TmcBeforeAcceptScreenComponent
        }
    }


    companion object {
        //region Extras
        private const val EXTRA_TMC_PARAMS = "EXTRA_WORK_IDS"

        //end Region
        fun getCallingIntent(context: Context, workIds: ArrayList<String>): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, TMCBeforeAcceptActivity::class.java)
            intent.putStringArrayListExtra(EXTRA_TMC_PARAMS, workIds)
            return intent
        }
        /**
         * Проверка получения даннных из интента
         */
        fun getResultFromIntent(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }
}