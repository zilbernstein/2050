package ru.digipeople.locotech.master.ui.activity.partlyaccept

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.tmcbeforeaccept.TMCBeforeAcceptActivity
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.DateUtils
import javax.inject.Inject

/**
 * Активити частичной приемки
 *
 * @author Kashonkov Nikita
 */
class PartlyAcceptActivity : MvpActivity(), PartlyAcceptView {
    //region Const
    private val FIRST_FAST_PERCENT = 10
    private val SECOND_FAST_PERCENT = 25
    private val THIRD_FAST_PERCENT = 50
    private val FOURTH_FAST_PERCENT = 75
    //end Region
    //region DI
    private lateinit var screenComponent: PartlyAcceptScreenComponent
    private lateinit var component: PartlyAcceptComponent
    @Inject
    lateinit var loadingDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    //end Region
    //region View
    private lateinit var parentLayout: ConstraintLayout
    private lateinit var workTitle: TextView
    private lateinit var outfitTitle: TextView
    private lateinit var outfitTime: TextView
    private lateinit var outfitPercent: TextView
    private lateinit var workNormal: TextView
    private lateinit var editPercent: EditText
    private lateinit var percent10: TextView
    private lateinit var percent25: TextView
    private lateinit var percent50: TextView
    private lateinit var percent75: TextView
    private lateinit var newOutfitTime: TextView
    private lateinit var newOutfitPercent: TextView
    private lateinit var newOutfitInfoGroup: Group
    //end Region
    //region Other
    private lateinit var presenter: PartlyAcceptPresenter
    private var isAcceptEnable = false
    //end Region

    /**
     * действия при старте активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.componentBuilder()
                .activityModule(ActivityModule(this))
                .params(intent.getParcelableExtra(EXTRA_PARAMS))
                .build()
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partly_accepted)
        /**
         * инициализация тулбара и установка заголовка
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.partly_accept_title)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, PartlyAcceptPresenter::class.java)
        presenter.initialize()
        /**
         * инициализация бокового меню
         */
        mainDrawerDelegate.init(true)

        parentLayout = findViewById(R.id.info_layout)
        workTitle = findViewById(R.id.work_title)
        outfitTitle = findViewById(R.id.outfit_title)
        outfitTime = findViewById(R.id.outfit_time)
        outfitPercent = findViewById(R.id.outfit_percent)
        workNormal = findViewById(R.id.work_normal)
        /**
         * обработка нажатия на 10%
         */
        percent10 = findViewById(R.id.percent_10)
        percent10.setOnClickListener {
            editPercent.setText(FIRST_FAST_PERCENT.toString())
            presenter.onPercentChanged(FIRST_FAST_PERCENT.toString())
        }
        /**
         * обработка нажатия на 25%
         */
        percent25 = findViewById(R.id.percent_25)
        percent25.setOnClickListener {
            editPercent.setText(SECOND_FAST_PERCENT.toString())
            presenter.onPercentChanged(SECOND_FAST_PERCENT.toString())
        }
        /**
         * обработка нажатия на 50%
         */
        percent50 = findViewById(R.id.percent_50)
        percent50.setOnClickListener {
            editPercent.setText(THIRD_FAST_PERCENT.toString())
            presenter.onPercentChanged(THIRD_FAST_PERCENT.toString())
        }
        /**
         * обработка нажатия на 75%
         */
        percent75 = findViewById(R.id.percent_75)
        percent75.setOnClickListener {
            editPercent.setText(FOURTH_FAST_PERCENT.toString())
            presenter.onPercentChanged(FOURTH_FAST_PERCENT.toString())
        }

        newOutfitTime = findViewById(R.id.new_outfit_time)
        newOutfitPercent = findViewById(R.id.new_outfit_percent)

        editPercent = findViewById(R.id.edit_percent)
        editPercent.addTextChangedListener(percentTextWatcher)

        newOutfitInfoGroup = findViewById(R.id.new_outfit_info)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * действия при восстановлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * действия при паузе активити
     */
    override fun onPause() {
        super.onPause()
        navigator.onPause()
    }
    /**
     * создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_with_check, menu)
        return true
    }
    /**
     * добавление пунктов меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.check -> {
                if (isAcceptEnable) {
                    presenter.onAcceptBtnClicked()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
     * отображение данных по частичной приемке работы
     */
    override fun showModel(work: Work?) {
        if (work == null) return

        workTitle.text = work.title
        outfitTitle.text = getString(R.string.divide_outfit, work.outfitNumber)

        var span = SpannableString(getString(R.string.divide_outfit_time, DateUtils.convertToString(work.workPartPercent * work.normalTime / 100)))
        span.setSpan(StyleSpan(Typeface.BOLD),
                0, 6,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        outfitTime.text = span

        outfitPercent.text = getString(R.string.divide_outfit_percent, work.workPartPercent)
        var spanPercent = SpannableString(getString(R.string.divide_outfit_percent, work.workPartPercent))
        spanPercent.setSpan(StyleSpan(Typeface.BOLD),
                0, 24,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        outfitPercent.text = spanPercent

        var spanNormal = SpannableString(getString(R.string.divide_outfit_normal, DateUtils.convertToString(work.workPartPercent * work.normalTime / 100)))
        spanNormal.setSpan(StyleSpan(Typeface.BOLD),
                0, 6,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        workNormal.text = spanNormal
        /**
         * доступность кнопок в зависимости от процента частичной приемки
         */
        when (work.workPartPercent) {
            in 0..FIRST_FAST_PERCENT -> {
                percent10.isEnabled = false
                percent25.isEnabled = false
                percent50.isEnabled = false
                percent75.isEnabled = false
            }
            in FIRST_FAST_PERCENT..SECOND_FAST_PERCENT -> {
                percent25.isEnabled = false
                percent50.isEnabled = false
                percent75.isEnabled = false
            }
            in SECOND_FAST_PERCENT..THIRD_FAST_PERCENT -> {
                percent50.isEnabled = false
                percent75.isEnabled = false
            }
            in THIRD_FAST_PERCENT..FOURTH_FAST_PERCENT -> {
                percent75.isEnabled = false
            }
        }
    }
    /**
     * управление доступностью кнопки
     */
    override fun setAcceptButtonEnability(isEnable: Boolean) {
        isAcceptEnable = isEnable
    }
    /**
     * установка процента
     */
    override fun setPercent(percent: String) {
        editPercent.setText(percent)
        editPercent.setSelection(editPercent.text.length)
    }
    /**
     * установка нового процента
     */
    override fun setNewOutfitPercent(percent: Int) {
        newOutfitPercent.text = getString(R.string.partly_accept_new_outfit_percent, percent)
    }
    /**
     * установка нового времени
     */
    override fun setNewOutfitTime(time: Long) {
        newOutfitTime.text = getString(R.string.partly_accept_new_outfit_time, DateUtils.convertToString(time))
    }
    /**
     * управление видимостью информации
     */
    override fun setNewOutfitInfoVisibility(isVisible: Boolean) {
        if (isVisible) {
            newOutfitInfoGroup.visibility = View.VISIBLE
        } else {
            newOutfitInfoGroup.visibility = View.GONE
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == WRITE_OFF_TMC) {
            /**
             * получение данных из интента
             */
            val isSuccesfull = TMCBeforeAcceptActivity.getResultFromIntent(resultCode, data)
            if (isSuccesfull) {
                presenter.onTmcWroteOff()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getScreenComponent(): PartlyAcceptScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().partlyAcceptScreenComponent()
        } else {
            return saved as PartlyAcceptScreenComponent
        }
    }
    /**
     * наблюдатель за изменением процентов
     */
    private val percentTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                presenter.onPercentChanged(s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    companion object {
        //region Extras
        private const val EXTRA_PARAMS = "EXTRA_PARAMS"
        //end Region
        //region Code
        val WRITE_OFF_TMC: Int = 100
        //end Region
        /**
         * интент
         */
        fun getCallingIntent(context: Context, params: PartlyAcceptParams): Intent {
            val intent = Intent(context, PartlyAcceptActivity::class.java)
            intent.putExtra(EXTRA_PARAMS, params)
            return intent
        }
    }
}