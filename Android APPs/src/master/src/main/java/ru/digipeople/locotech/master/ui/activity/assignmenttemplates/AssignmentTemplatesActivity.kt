package ru.digipeople.locotech.master.ui.activity.assignmenttemplates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_workers_presence.*
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.ActivityNavigator
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.adapter.AssignmentTemplatesAdapter
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.adapter.item.AssignmentTemplateItem
import ru.digipeople.locotech.master.ui.activity.assignmenttemplates.dialog.AssignmentParamsDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject
/**
 * Активити выбора шаблона исполнителей
 */
class AssignmentTemplatesActivity : AppCompatActivity() {
    //region DI
    private val component: AssignmentTemplatesComponent by lazy {
        AppComponent.get().assignmentTemplatesComponent(ActivityModule(this))
    }
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    @Inject
    lateinit var navigator: ActivityNavigator
    //endregion
    //region Other
    private val adapter = AssignmentTemplatesAdapter()
    private lateinit var viewModel: AssignmentTemplatesViewModel

    //endregion
    /**
     * Операции. выполняемые при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_templates)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(getString(R.string.assignment_templates_title))

        recycler_view.adapter = adapter
        /**
         * Инициализация модуля меню
         */
        mainDrawerDelegate.init(iconBack = true)
        /**
         * Инициализация viewModel
         */
        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(AssignmentTemplatesViewModel::class.java)
        viewModel.apply {
            loadingLiveData.observe({ lifecycle }, ::setProgressVisible)
            noDataLiveData.observe({ lifecycle }, ::setNoDataVisible)
            userErrorLiveData.observe({ lifecycle }, ::showError)
            assignmentTemplatesLiveData.observe({ lifecycle }, ::setData)
            assignmentWasSetLiveData.observe({ lifecycle }) {
                navigator.navigateBack(this@AssignmentTemplatesActivity)
            }
            start()
        }

        adapter.onItemClickListener = this::showAssignmentDialog
    }
    /**
     * вызов диалогового окна
     */
    private fun showAssignmentDialog(item: AssignmentTemplateItem) {
        AssignmentParamsDialog.show(this.supportFragmentManager) { date, nightShift ->
            viewModel.onSetTemplateClicked(item, date, nightShift)
        }
    }
    /**
     * управление видимостью лоадера
     */
    private fun setProgressVisible(visible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(visible)
    }
    /**
     * отображение ошибки
     */
    private fun showError(userError: UserError) {
        Snackbar.make(recycler_view, userError.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Управление видимостью сообщения об отсутствии данных
     */
    private fun setNoDataVisible(visible: Boolean) {
        no_data.visibility = if (visible) View.VISIBLE else View.GONE
    }
    /**
     * установка данных
     */
    private fun setData(data: List<AssignmentTemplateItem>) {
        adapter.items = data
    }

    companion object {
        /**
         * Интент для вызова активити
         */
        fun getCallingIntent(activity: Activity) = Intent(activity, AssignmentTemplatesActivity::class.java)
    }
}