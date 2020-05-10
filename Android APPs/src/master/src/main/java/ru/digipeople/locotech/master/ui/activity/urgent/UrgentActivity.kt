package ru.digipeople.locotech.master.ui.activity.urgent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.model.Work
import ru.digipeople.locotech.master.ui.activity.Navigator
import ru.digipeople.locotech.master.ui.activity.comment.CommentActivity
import ru.digipeople.locotech.master.ui.activity.performance.PerformanceActivity
import ru.digipeople.locotech.master.ui.activity.urgent.adapter.UrgentCardAdapter
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити срочно
 *
 * @author Kashonkov Nikita
 */
class UrgentActivity : MvpActivity(), UrgentView {
    //region DI
    private lateinit var screenComponent: UrgentScreenComponent
    private lateinit var component: UrgentComponent
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var adapter: UrgentCardAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //region other
    //region Other
    private lateinit var presenter: UrgentPresenter
    private lateinit var recyler: androidx.recyclerview.widget.RecyclerView
    //endRegion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.urgentComponent(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urgent)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.urgent_activity_title)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.urgentPresenter() }, UrgentPresenter::class.java)
        presenter.initialize()
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)

        recyler = findViewById(R.id.activity_urgent_recycler_view)
        recyler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        adapter.commentItemClickListener = presenter::showComment
        adapter.startButtonClickedListener = presenter::startWorkClicked
        adapter.stopButtonClickedListener = presenter::stopWorkClicked
        adapter.returnButtonClickedListener = presenter::returnWorkClicked
        adapter.acceptButtonClickedListener = presenter::acceptWorkClicked
        adapter.tmcClickListener = presenter::onTmcClicked
        adapter.measurementClickListener = presenter::onMeasurementClicked
        recyler.adapter = adapter
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
//        super.onBackPressed()
    }
    /**
     * Действия при старте активити
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Действия при возобновлении активити
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PerformanceActivity.EDIT_COMMENT_BEFORE_RETURN) {
            /**
             * Получение данных из интента
             */
            val isSuccesfull = CommentActivity.getResultFromIntent(resultCode, data)
            if (isSuccesfull) {
                presenter.onCommentAddedToReturnedWork()
            } else {
                presenter.onCommentAddedToReturnWorkMistake()
            }
        }
    }
    /**
     * Установка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Work>) {
        adapter.setData(list)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisible(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recyler, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): UrgentScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().urgentScreenComponent()
        } else {
            return saved as UrgentScreenComponent
        }

    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, UrgentActivity::class.java)
        }

    }
}
