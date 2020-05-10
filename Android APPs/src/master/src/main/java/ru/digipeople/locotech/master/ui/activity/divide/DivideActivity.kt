package ru.digipeople.locotech.master.ui.activity.divide

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.divide.dialog.TimePickerDialog
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.DateUtils
import javax.inject.Inject

/**
 * Активити разделения работы
 *
 * @author Kashonkov Nikita
 */
class DivideActivity : MvpActivity(), DivideView {
    //region Const
    private val FIRST_FAST_PERCENT = 10
    private val SECOND_FAST_PERCENT = 25
    private val THIRD_FAST_PERCENT = 50
    private val FOURTH_FAST_PERCENT = 75
    //end Region
    //region DI
    private lateinit var screenComponent: DivideScreenComponent
    private lateinit var component: DivideComponent
    @Inject
    lateinit var loadingDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var navigator: Navigator
    //end Region
    //region View
    private lateinit var parentLayout: ConstraintLayout
    private lateinit var workTitle: TextView
    private lateinit var outfitTitle: TextView
    private lateinit var outfitTime: TextView
    private lateinit var outfitPercent: TextView
    private lateinit var workNormal: TextView
    private lateinit var editTime: TextView
    private lateinit var editPercent: EditText
    private lateinit var percent10: TextView
    private lateinit var percent25: TextView
    private lateinit var percent50: TextView
    private lateinit var percent75: TextView
    //end Region
    //region Other
    private lateinit var presenter: DividePresenter
    private var isDivideEnable = false
    private var shouldInformPresenterAboutPercent = true
    private var timePickerDialog: TimePickerDialog? = null
    //end Region
    /**
     * операции при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .params(intent.getParcelableExtra(EXTRA_PARAMS))
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_divide)
        /**
         * инициализация тулбара и установка заголовка
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.divide_title)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, DividePresenter::class.java)
        presenter.initialize()

        mainDrawerDelegate.init(true)

        parentLayout = findViewById(R.id.info_layout)
        workTitle = findViewById(R.id.work_title)
        outfitTitle = findViewById(R.id.outfit_title)
        outfitTime = findViewById(R.id.outfit_time)
        outfitPercent = findViewById(R.id.outfit_percent)
        workNormal = findViewById(R.id.work_normal)
        /**
         * установка процентного разделения работы
         * кнопка 10%
         */
        percent10 = findViewById(R.id.percent_10)
        percent10.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(2)))
        percent10.setOnClickListener {
            editPercent.setText(FIRST_FAST_PERCENT.toString())
            presenter.onPercentChanged(FIRST_FAST_PERCENT.toString())
        }
        /**
         * кнопка 25%
         */
        percent25 = findViewById(R.id.percent_25)
        percent25.setOnClickListener {
            editPercent.setText(SECOND_FAST_PERCENT.toString())
            presenter.onPercentChanged(SECOND_FAST_PERCENT.toString())
        }
        /**
         * кнопка 50%
         */
        percent50 = findViewById(R.id.percent_50)
        percent50.setOnClickListener {
            editPercent.setText(THIRD_FAST_PERCENT.toString())
            presenter.onPercentChanged(THIRD_FAST_PERCENT.toString())
        }
        /**
         * кнопка 75%
         */
        percent75 = findViewById(R.id.percent_75)
        percent75.setOnClickListener {
            editPercent.setText(FOURTH_FAST_PERCENT.toString())
            presenter.onPercentChanged(FOURTH_FAST_PERCENT.toString())
        }
        /**
         * редактирование времени
         */
        editTime = findViewById(R.id.edit_time)
        editTime.setOnClickListener { presenter.onEditTimeCLicked() }
        /**
         * рнедактирование в процентах
         */
        editPercent = findViewById(R.id.edit_percent)
        editPercent.addTextChangedListener(percentTextWatcher)
    }
    /**
     * операции при пробуждении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * операции при переходе активити на паузу
     */
    override fun onPause() {
        super.onPause()
        navigator.onPause()
        if (timePickerDialog != null) {
            timePickerDialog!!.dismiss()
            timePickerDialog = null
        }
    }
    /**
     * создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_with_check, menu)
        return true
    }
    /**
     * установка пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.check -> {
                if (isDivideEnable) {
                    presenter.onDivideBtnClicked()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * отображение информации по разделению работы
     */
    override fun showModel(work: Work?) {
        if (work == null) return

        workTitle.text = work.title
        outfitTitle.text = getString(R.string.divide_outfit, work.outfitNumber)
        /**
         * время выделяется жирным
         */
        var span = SpannableString(getString(R.string.divide_outfit_time, DateUtils.convertToString(work.workPartPercent * work.normalTime / 100)))
        span.setSpan(StyleSpan(Typeface.BOLD),
                0, 6,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        outfitTime.text = span
        /**
         * проценты выделяются жирным
         */
        outfitPercent.text = getString(R.string.divide_outfit_percent, work.workPartPercent)
        var spanPercent = SpannableString(getString(R.string.divide_outfit_percent, work.workPartPercent))
        spanPercent.setSpan(StyleSpan(Typeface.BOLD),
                0, 24,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        outfitPercent.text = spanPercent
        /**
         * проценты нормы выделяются жирным
         */
        var spanNormal = SpannableString(getString(R.string.divide_outfit_normal, DateUtils.convertToString(work.workPartPercent * work.normalTime / 100)))
        spanNormal.setSpan(StyleSpan(Typeface.BOLD),
                0, 6,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        workNormal.text = spanNormal
        /**
         * управление видимоостью процентов при разделении работы
         */
        when (work.workPartPercent) {
            /**
             * видны все проценты
             */
            in 0..FIRST_FAST_PERCENT -> {
                percent10.isEnabled = false
                percent25.isEnabled = false
                percent50.isEnabled = false
                percent75.isEnabled = false
            }
            /**
             * видны 25%, 50%, 75%
             */
            in FIRST_FAST_PERCENT..SECOND_FAST_PERCENT -> {
                percent25.isEnabled = false
                percent50.isEnabled = false
                percent75.isEnabled = false
            }
            /**
             * видны 50% и 75%
             */
            in SECOND_FAST_PERCENT..THIRD_FAST_PERCENT -> {
                percent50.isEnabled = false
                percent75.isEnabled = false
            }
            /**
             * видно только 75%
             */
            in THIRD_FAST_PERCENT..FOURTH_FAST_PERCENT -> {
                percent75.isEnabled = false
            }
        }
    }
    /**
     * установка времени
     */
    override fun setTime(time: Long) {
        editTime.text = DateUtils.convertToString(time)
    }
    /**
     * установка процентов
     */
    override fun setPercent(percent: String) {
        editPercent.setText(percent)
        editPercent.setSelection(editPercent.text.length)
    }
    /**
     * установка процентов после изменения времени
     */
    override fun setPercentAfterTimeChanged(percent: String) {
        shouldInformPresenterAboutPercent = false
        editPercent.setText(percent)
        editPercent.setSelection(editPercent.text.length)
    }
    /**
     * очистка времени
     */
    override fun hideDividedTime() {
        editTime.setText("")
    }
    /**
     * очистка процентов
     */
    override fun hidePercent() {
        editPercent.setText("")
    }

    /**
     * управление доступностью кнопки разделить работу
     */
    override fun setDivideButtonEnabled(isEnable: Boolean) {
        isDivideEnable = isEnable
    }
    /**
     * показать диалог выбора времени
     */
    override fun showTimePickerDialog(initTIme: Long, maxTime: Long) {
        if (timePickerDialog != null) return

        timePickerDialog = TimePickerDialog.newInstance(initTIme, maxTime)
        timePickerDialog!!.dismissListener = { timePickerDialog = null }
        timePickerDialog!!.setTimeListener = presenter::onSetTimeCLicked
        timePickerDialog!!.show(fragmentManager, "")
    }

    private fun getScreenComponent(): DivideScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().divideScreenComponent()
        } else {
            return saved as DivideScreenComponent
        }
    }
    /**
     * наблюдатель за процентами
     */
    private val percentTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (shouldInformPresenterAboutPercent) {
                if (s != null)
                    presenter.onPercentChanged(s.toString())
            }

            shouldInformPresenterAboutPercent = true
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    companion object {
        //region Extra
        private val EXTRA_PARAMS = "EXTRA_PARAMS"

        //end Region
        fun newInstance(context: Context, params: DivideParams): Intent {
            /**
             * интент стандартный
             */
            val intent = Intent(context, DivideActivity::class.java)
            intent.putExtra(EXTRA_PARAMS, params)
            return intent
        }

    }
}