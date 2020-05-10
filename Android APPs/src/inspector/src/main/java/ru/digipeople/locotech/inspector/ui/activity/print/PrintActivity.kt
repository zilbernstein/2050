package ru.digipeople.locotech.inspector.ui.activity.print

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.inspector.AppComponent
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.Navigator
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.DocumentModel
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.DocumentsAdapter
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.SignersCategoriesAdapter
import ru.digipeople.locotech.inspector.ui.activity.print.adapter.SignersCategoryModel
import ru.digipeople.locotech.inspector.ui.activity.print.decorator.DividerItemDecorator
import ru.digipeople.locotech.inspector.ui.activity.print.dialog.ClearAllSignersDialog
import ru.digipeople.locotech.inspector.ui.activity.print.dialog.EnterEmailDialog
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Активити печати
 *
 * @author Kashonkov Nikita
 */
class PrintActivity : MvpActivity(), PrintView {
    //region Di
    private lateinit var screenComponent: PrintScreenComponent
    private lateinit var component: PrintComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var adapter: DocumentsAdapter
    @Inject
    lateinit var signersAdapter: SignersCategoriesAdapter
    //endregion
    //region Views
    private lateinit var checkAll: TextView
    private lateinit var clearAll: TextView
    private lateinit var documentsRecyclerView: RecyclerView
    private lateinit var signersRecyclerView: RecyclerView
    private lateinit var printButton: Button
    private var isCheked: Boolean = false
    //endregion
    //region Other
    lateinit var presenter: PrintPresenter
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print)
        /**
         * инициализация тулбара
         */
        toolbarDelegate.init()
        /**
         * инициализация презентера и бокового меню
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, PrintPresenter::class.java)
        presenter.initialize()

        mainDrawerDelegate.init(true)

        checkAll = findViewById(R.id.check_all)
        checkAll.setOnClickListener {
            presenter.onCheckAllClicked(isCheked)
        }

        clearAll = findViewById(R.id.clear_all)
        clearAll.setOnClickListener { presenter.onClearAllSignersClicked() }

        printButton = findViewById(R.id.print_btn)
        printButton.setOnClickListener { presenter.onPrintBtnClicked() }

        documentsRecyclerView = findViewById(R.id.documentsRecyclerView)
        documentsRecyclerView.layoutManager = LinearLayoutManager(this)
        documentsRecyclerView.addItemDecoration(DividerItemDecorator(this))
        documentsRecyclerView.adapter = adapter
        adapter.onItemClickListener = presenter::onDocumentClicked

        signersRecyclerView = findViewById(R.id.signersRecyclerView)
        signersRecyclerView.layoutManager = LinearLayoutManager(this)
        signersRecyclerView.addItemDecoration(DividerItemDecorator(this))
        signersRecyclerView.adapter = signersAdapter
        signersAdapter.onRemoveSignerClickListener = presenter::onRemoveSigner
        signersAdapter.onRemoveAllSignersClickListener = presenter::onRemoveAllSignersFromCategory
        signersAdapter.onAddSignerClickListener = presenter::onAddSignerToCategory
    }

    override fun onBackPressed() {
        // nop
    }

    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
        presenter.onScreenShown()
    }

    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * установка заголовка
     */
    override fun setTitle(equipmentName: String) {
        toolbarDelegate.setTitle(getString(R.string.print_activity_title, equipmentName))
    }
    /**
     * загрузка документов
     */
    override fun setDocuments(documents: List<DocumentModel>) {
        adapter.items = documents
    }
    /**
     * загрузка категорий подписантов
     */
    override fun setSignersCategories(signersCategories: List<SignersCategoryModel>) {
        signersAdapter.items = signersCategories
    }
    /**
     * диалог удаления подписантов
     */
    override fun showDeleteSignersDialog() {
        ClearAllSignersDialog(this).run {
            agreeListener = presenter::onClearAllSignersApproveClicked
            show()
        }
    }
    /**
     * диалог ввода почты
     */
    override fun showEnterEmailDialog(email: String) {
        EnterEmailDialog(this, email).run {
            sendListener = presenter::onSendBtnClicked
            show()
        }
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
        signersAdapter.notifyDataSetChanged()
    }
    /**
     * Отображение ошибки
     */
    override fun showError(error: UserError) {
        Snackbar.make(documentsRecyclerView, error.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * отображение сообщения
     */
    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    /**
     * установка заголовка выбрать все/сбросить все
     */
    override fun setTitleCheckAll(showCheckAll: Boolean) {
        if (showCheckAll) {
            checkAll.setText(R.string.print_activity_check_all)
            isCheked = true
        } else {
            checkAll.setText(R.string.print_activity_reset_all)
            isCheked = false
        }
    }

    private fun getScreenComponent(): PrintScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().printScreenComponent()
        } else {
            return saved as PrintScreenComponent
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, PrintActivity::class.java)
        }
    }
}