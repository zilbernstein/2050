package ru.digipeople.locotech.master.ui.activity.tmcamount

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
 * Активити ввода/изменения количества ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcAmountActivity : MvpActivity(), TmcAmountView {
    //region DI
    private lateinit var screenComponent: TmcAmountScreenComponent
    private lateinit var component: TmcAmountComponent
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
    lateinit var sectionAmount: TextView
    lateinit var stockAmount: TextView
    lateinit var uom: TextView
    lateinit var mainLayout: ConstraintLayout
    //end Region
    //region Other
    lateinit var presenter: TmcAmountPresenter
    lateinit var params: TmcAmountParams
    //end Region
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        params = intent.getParcelableExtra(EXTRA_TMC_PARAMS)
        screenComponent = getScreenComponent()
        component = screenComponent.component().activityModule(ActivityModule(this)).params(params).build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmc_amount)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, TmcAmountPresenter::class.java)
        presenter.initialize()

        title = findViewById(R.id.title)
        title.text = params.tmcTitle

        editAmount = findViewById(R.id.edit_amount)

        clearBtn = findViewById(R.id.clear_btn)
        checkBtn = findViewById(R.id.check_button)
        backBtn = findViewById(R.id.back_button)

        sectionAmount = findViewById(R.id.section_value)
        uom = findViewById(R.id.uom_value)
        stockAmount = findViewById(R.id.stock_value)

        mainLayout = findViewById(R.id.main_layout)

        clearBtn.setOnClickListener { editAmount.setText("") }
        /**
         * Обработка нажатия кнопки очистка
         */
        checkBtn.setOnClickListener { presenter.onCheckBtnClicked(editAmount.text.toString()) }
        /**
         * Обработка нажатия кнопки назад
         */
        backBtn.setOnClickListener { navigator.navigateBack() }

        editAmount.setText("${params.askedAmount}")
        editAmount.setSelection(editAmount.text.length)
        sectionAmount.text = "${params.sectionLeft}"
        stockAmount.text = "${params.stockLeft}"
        uom.text = params.uom
    }
    /**
     * Действия при возобновлениии активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Действия при постановке активити на паузу
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
//        super.onBackPressed()
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
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }

    private fun getScreenComponent(): TmcAmountScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().tmcAmountScreenComponent()
        } else {
            return saved as TmcAmountScreenComponent
        }
    }

    companion object {
        //region Extras
        private const val EXTRA_TMC_PARAMS = "EXTRA_TMC_PARAMS"
        //end Region
        /**
         * Интент
         */
        fun getCallingIntent(context: Context, params: TmcAmountParams): Intent {
            val intent = Intent(context, TmcAmountActivity::class.java)
            intent.putExtra(EXTRA_TMC_PARAMS, params)
            return intent
        }
    }
}