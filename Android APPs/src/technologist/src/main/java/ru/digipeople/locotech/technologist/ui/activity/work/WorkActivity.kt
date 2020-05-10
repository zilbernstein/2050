package ru.digipeople.locotech.technologist.ui.activity.work

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.technologist.AppComponent
import ru.digipeople.locotech.technologist.R
import ru.digipeople.locotech.technologist.model.Parameter
import ru.digipeople.locotech.technologist.ui.Navigator
import ru.digipeople.locotech.technologist.ui.activity.work.adapter.ParametersAdapter
import ru.digipeople.ui.UiComponent
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити работы
 */
class WorkActivity : AppActivity() {
    //region Di
    private val component by lazy {
        DaggerWorkComponent.builder()
                .activityModule(ActivityModule(this))
                .appComponent(AppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var loadingFragment: LoadingFragmentDelegate
    //endregion
    //region Views
    private lateinit var parametersRecyclerView: RecyclerView
    //endregion
    //region Other
    private val parametersAdapter = ParametersAdapter()
    private lateinit var viewModel: WorkViewModel
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.work_activity_title)
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(true)

        parametersRecyclerView = findViewById(R.id.activity_work_parameter_value_recycler)
        parametersRecyclerView.layoutManager = LinearLayoutManager(this)
        parametersRecyclerView.adapter = parametersAdapter

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(WorkViewModel::class.java)
        viewModel.apply {
            viewModel.workId = intent.extras!!.getString(EXTRA_WORK_ID)!!

            loadingLiveData.observe({ lifecycle }, ::setLoadingVisible)
            userErrorLiveData.observe({ lifecycle }, ::showUserError)
            parametersLiveData.observe({ lifecycle }, ::setParameters)
            commentLiveData.observe({ lifecycle }, ::setComment)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)

            start()
        }

        parametersAdapter.onCommentClickListener = viewModel::onCommentClicked
    }
    /**
     * Отображение сообщения об отсутствия данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        parametersRecyclerView.isVisible = !visible
    }

    override fun onStart() {
        super.onStart()
        viewModel.onScreenShown()
    }

    override fun onResume() {
        navigator.onResume(this)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        navigator.onPause()
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
        mainDrawerDelegate.onBackPressed()
        super.onBackPressed()
    }
    /**
     * Установка параметров
     */
    private fun setParameters(parameters: List<Parameter>) {
        parametersAdapter.items = parameters
        parametersAdapter.notifyDataSetChanged()
    }
    /**
     * Установка комментария
     */
    private fun setComment(comment: String) {
        parametersAdapter.comment = comment
    }
    /**
     * Управление видимсотью лоадера
     */
    private fun setLoadingVisible(visible: Boolean) = loadingFragment.setLoadingVisibility(visible)
    /**
     * Отображение ошибки
     */
    private fun showUserError(userError: UserError) {
        Snackbar.make(parametersRecyclerView, userError.message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_WORK_ID = "EXTRA_WORK_ID"

        fun getCallingIntent(context: Context, workId: String): Intent {
            /**
             * Интент
             */
            val intent = Intent(context, WorkActivity::class.java)
            intent.putExtra(EXTRA_WORK_ID, workId)
            return intent
        }
    }
}