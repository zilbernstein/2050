package ru.digipeople.locotech.inspector.ui.activity.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposables
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.CheckListAdapter
import ru.digipeople.locotech.inspector.ui.activity.checklist.adapter.OperationData
import ru.digipeople.locotech.inspector.ui.activity.checklist.decorator.DividerItemDecorator
import ru.digipeople.locotech.inspector.ui.activity.checklist.dialog.ApproveDialog
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import javax.inject.Inject

/**
 * Активити чек листа
 *
 * @author Kashonkov Nikita
 */
class CheckListActivity : MvpActivity(), CheckListView {
    //region DI
    private lateinit var screenComponent: CheckListScreenComponent
    private lateinit var component: CheckListComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var adapter: CheckListAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //end region
    //region View
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    lateinit var filter: TextView
    private lateinit var approveBtn: Button
    //end region
    //region Other
    lateinit var presenter: CheckListPresenter
    private var dataTransformDisposable = Disposables.disposed()
    private var dialog: ApproveDialog? = null
    private var currentFilter = CheckListFilterState.FILTER_ALL
    //end region
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, CheckListPresenter::class.java)
        presenter.initialize()
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        mainDrawerDelegate.init(false)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecorator(this))
        recyclerView.adapter = adapter
        /**
         * фильтр
         */
        filter = findViewById(R.id.filter)
        var span = SpannableString(getString(R.string.check_list_activity_filter))
        span.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.appBlack)),
                0, 6,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        filter.text = span
        filter.setOnClickListener { onFilterClicked() }

        adapter.checkOtkClickListener = presenter::onCheckOtkWorkClicked
        adapter.checkRzdClickListener = presenter::onCheckRzdWorkClicked
        adapter.commentClickListener = presenter::onCsoCommentClicked
        adapter.photoClickListener = presenter::onPhotoWorkClicked

        approveBtn = findViewById(R.id.approve_btn)
        approveBtn.setOnClickListener { presenter.onApproveButtonClicked() }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
        presenter.onScreenShown()
    }

    override fun onPause() {
        navigator.onPause()
        loadingFragmentDelegate.setLoadingVisibility(false)
        super.onPause()
    }
    /**
     * установка заголовка
     */
    override fun setTitle(equipmentName: String?) {
        toolbarDelegate.setTitle(getString(R.string.check_list_activity_title, equipmentName ?: ""))
    }
    /**
     * установка данных
     */
    override fun setData(adapterData: AdapterData?) {
        adapterData?.let {
            adapter.setData(adapterData)
        }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * обновить работу
     */
    override fun updateWork(operationData: OperationData, position: Int) {
        adapter.workItemChanged(operationData, position)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * диалог отмены согласования
     */
    override fun dismissApproveDialog() {
        dialog?.dismiss()
    }
    /**
     * диалог подтверждения согласования
     */
    override fun showApproveDialog() {
        if (dialog != null) {
            return
        }

        dialog = ApproveDialog(this)
        dialog?.agreeListener = presenter::onApproveDialogButtonClicked
        dialog?.setOnDismissListener { dialog = null }
        dialog?.show()
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }

    override fun setFinishBtnVisibility(isVisibile: Boolean) {
        if (isVisibile) {
            approveBtn.visibility = View.VISIBLE
        } else {
            approveBtn.visibility = View.GONE
        }
    }
    /**
     * обработка нажатия на фильтра
     */
    private fun onFilterClicked() {
        when (currentFilter) {
            /**
             * все
             */
            CheckListFilterState.FILTER_ALL -> {
                currentFilter = CheckListFilterState.FILTER_ACTIVE
                val span = SpannableString(getString(R.string.check_list_activity_filter))
                span.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.appBlack)),
                        4, 14,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                filter.text = span
            }
            /**
             * активные
             */
            CheckListFilterState.FILTER_ACTIVE -> {
                currentFilter = CheckListFilterState.FILTER_ALL
                val span = SpannableString(getString(R.string.check_list_activity_filter))
                span.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.appBlack)),
                        0, 6,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                filter.text = span
            }
        }
        presenter.onFilterClicked(currentFilter)
    }

    private fun getScreenComponent(): CheckListScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().checkListScreenComponent()
        } else {
            return saved as CheckListScreenComponent
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            /**
             * интент
             */
            return Intent(context, CheckListActivity::class.java)
        }

    }
}