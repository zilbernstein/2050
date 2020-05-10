package ru.digipeople.locotech.master.ui.activity.writeofftmcamount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити списания ТМЦ
 * @author Kashonkov Nikita
 */
class WriteOffTmcAmountActivity: MvpActivity(), WriteOffTTmcAmountView {
    //region DI
    private lateinit var screenComponent: WriteOffTmcAmountScreenComponent
    private lateinit var component: WriteOffTmcAmountComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //region View
    lateinit var title: TextView
    lateinit var editAmount: EditText
    lateinit var clearBtn: ImageView
    lateinit var checkBtn: ImageView
    lateinit var backBtn: ImageView
    lateinit var normalAmount: TextView
    lateinit var mainLayout: ConstraintLayout
    lateinit var uom: TextView
    //end Region
    //region Other
    lateinit var presenter: WriteOffTmcAmountPresenter
    lateinit var params: WriteOffTmcAmountParams
    //end Region
    /**
     * Действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        params = intent.getParcelableExtra(EXTRA_TMC_PARAMS)
        screenComponent = getScreenComponent()
        component = screenComponent.component().activityModule(ActivityModule(this)).params(params).build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmc_write_off_amount)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, WriteOffTmcAmountPresenter::class.java)
        presenter.initialize()

        title = findViewById(R.id.title)
        title.text = params.tmcTitle

        editAmount = findViewById(R.id.edit_amount)

        clearBtn = findViewById(R.id.clear_btn)
        checkBtn = findViewById(R.id.check_button)
        backBtn = findViewById(R.id.back_button)

        normalAmount = findViewById(R.id.normal_value)

        mainLayout = findViewById(R.id.main_layout)

        uom = findViewById(R.id.uom_value)
        /**
         * Обработка нажатий на кнопки
         * Очистить
         */
        clearBtn.setOnClickListener { editAmount.setText("") }
        /**
         * Принять
         */
        checkBtn.setOnClickListener { presenter.onCheckBtnClicked(editAmount.text.toString()) }
        /**
         * Назад
         */
        backBtn.setOnClickListener { navigator.navigateBack() }

        editAmount.setText("${params.asked}")
        editAmount.setSelection(editAmount.text.length)
        normalAmount.text = "${params.normal}"
        uom.text = "${params.uom}"
    }
    /**
     * Действия при восстановлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Действия при приостановке активити
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
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управлени видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }

    private fun getScreenComponent(): WriteOffTmcAmountScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().writeOffTmcAmountScreenComponent()
        } else {
            return saved as WriteOffTmcAmountScreenComponent
        }

    }

    companion object {
        //region Extras
        private const val EXTRA_TMC_PARAMS = "EXTRA_TMC_PARAMS"
        //end Region
        fun getCallingIntent(context: Context, params: WriteOffTmcAmountParams): Intent {
            val intent = Intent(context, WriteOffTmcAmountActivity::class.java)
            intent.putExtra(EXTRA_TMC_PARAMS, params)
            return intent
        }
    }
}