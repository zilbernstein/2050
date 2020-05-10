package ru.digipeople.locotech.inspector.ui.activity.inspection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.CyclicWorksFragment
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.otkremarks.OtkRemarksFragment
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.RzdRemarksFragment
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити инспекционного контроля
 *
 * @author Kashonkov Nikita
 */
class InspectionActivity : MvpActivity(), InspectionView {
    //region DI
    private lateinit var screenComponent: InspectionScreenComponent
    private lateinit var component: InspectionComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //end Region
    //region View
    private lateinit var filterGroup: RadioGroup
    private var containerId = 0
    private lateinit var cyclicFilter: RadioButton
    private lateinit var remarkOtkFilter: RadioButton
    private lateinit var remarkRzdFilter: RadioButton
    private lateinit var container: FrameLayout
    //end Region
    //region Other.
    private lateinit var presenter: InspectionPresenter
    private lateinit var cyclicWorkFragment: CyclicWorksFragment
    private lateinit var remarkRzdFragment: RzdRemarksFragment
    private lateinit var remarkOtkFragment: OtkRemarksFragment
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspection)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, InspectionPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        mainDrawerDelegate.init(false)

        containerId = R.id.fragment_container
        container = findViewById(containerId)

        cyclicWorkFragment = CyclicWorksFragment.newInstance()
        remarkRzdFragment = RzdRemarksFragment.newInstance()
        remarkOtkFragment = OtkRemarksFragment.newInstance()

        val addFragmentTransaction = supportFragmentManager.beginTransaction()
        addFragmentTransaction.add(containerId, cyclicWorkFragment)
        addFragmentTransaction.add(containerId, remarkOtkFragment)
        addFragmentTransaction.add(containerId, remarkRzdFragment)
        addFragmentTransaction.commit()

        filterGroup = findViewById(R.id.filter_group)
        filterGroup.setOnCheckedChangeListener(groupCheckedListener)
        /**
         * Фильтры
         */
        cyclicFilter = findViewById(R.id.cyclic_work)
        setCyclicCount(0)
        remarkOtkFilter = findViewById(R.id.remark_otk)
        setOtkRemarkCount(0)
        remarkRzdFilter = findViewById(R.id.remark_rzd)
        setRzdRemarkCount(0)
        /**
         * Получение данных из интента
         */
        val startTypeCode = intent.getIntExtra(EXTRA_START_TYPE, StartTab.CYCLIC_WORK.code)
        val startType = StartTab.valueOf(startTypeCode)
        /**
         * Разделение по типам
         */
        when (startType) {
            StartTab.CYCLIC_WORK -> {
                cyclicFilter.isChecked = true
            }
            StartTab.REMARK_OTK -> {
                remarkOtkFilter.isChecked = true
            }
            StartTab.REMARK_RZD -> {
                remarkRzdFilter.isChecked = true
            }
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.onScreenShown()
        navigator.onResume(this)
    }

    override fun onPause() {
        navigator.onPause()
        loadingFragmentDelegate.setLoadingVisibility(false)
        super.onPause()
    }
    /**
     * Переход назад
     */
    override fun onBackPressed() {
//        super.onBackPressed()
    }
    /**
     * Создание нового меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notify_rzd_menu, menu)
        return true
    }
    /**
     * Обработка действия пунктов
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notify -> {
                presenter.onRzdBtnClicked()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    /**
     * Установка заголовка
     */
    override fun setTitle(equipmentTitle: String?) {
        toolbarDelegate.setTitle(getString(R.string.inspection_activity_title, equipmentTitle))
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установка видимости лоадера
     */
    override fun setProgressVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(container, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Отображение сообщения
     */
    override fun showSuccessfullyNotifiedMessage() {
        Snackbar.make(container, R.string.inspection_rzd_successfully_notified, Snackbar.LENGTH_LONG).show()
    }

    fun component(): InspectionComponent {
        return component
    }
    /**
     * Установка счетчика цикловых работ
     */
    fun setCyclicCount(count: Int) {
        cyclicFilter.text = getString(R.string.inspection_activity_cyclic_work, count)
    }
    /**
     * Установка счетчика замечаний ОТК
     */
    fun setOtkRemarkCount(count: Int) {
        remarkOtkFilter.text = getString(R.string.inspection_activity_remark_otk, count)
    }
    /**
     * Установка замечаний РЖД
     */
    fun setRzdRemarkCount(count: Int) {
        remarkRzdFilter.text = getString(R.string.inspection_activity_remark_rzd, count)
    }
    /**
     * Пказать цикловые работы
     */
    private fun showCyclicWork() {
        val showTransaction = supportFragmentManager.beginTransaction()
        showTransaction.hide(remarkOtkFragment)
        showTransaction.hide(remarkRzdFragment)
        showTransaction.show(cyclicWorkFragment)
        showTransaction.commit()
    }
    /**
     * Показать замечания ОТК
     */
    private fun showOtkRemark() {
        val showTransaction = supportFragmentManager.beginTransaction()
        showTransaction.hide(cyclicWorkFragment)
        showTransaction.hide(remarkRzdFragment)
        showTransaction.show(remarkOtkFragment)
        showTransaction.commit()
    }
    /**
     * Показать замечания РЖД
     */
    private fun showRzdRemark() {
        val showTransaction = supportFragmentManager.beginTransaction()
        showTransaction.hide(cyclicWorkFragment)
        showTransaction.hide(remarkOtkFragment)
        showTransaction.show(remarkRzdFragment)
        showTransaction.commit()
    }

    private fun getScreenComponent(): InspectionScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().inspectionScreenComponent()
        } else {
            return saved as InspectionScreenComponent
        }
    }

    private val groupCheckedListener = object : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            when (checkedId) {
                R.id.cyclic_work -> showCyclicWork()
                R.id.remark_otk -> showOtkRemark()
                R.id.remark_rzd -> showRzdRemark()
            }
        }
    }

    companion object {
        //region Extras
        private const val EXTRA_START_TYPE = "EXTRA_START_TYPE"

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, InspectionActivity::class.java)
        }

        fun getCallingIntentForRemarkRzd(context: Context): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, InspectionActivity::class.java)
            intent.putExtra(EXTRA_START_TYPE, StartTab.REMARK_RZD.code)
            return intent
        }

        fun getCallingIntentForRemarkOtk(context: Context): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, InspectionActivity::class.java)
            intent.putExtra(EXTRA_START_TYPE, StartTab.REMARK_OTK.code)
            return intent
        }
    }
}