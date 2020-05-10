package ru.digipeople.message.ui.fragment.messagelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.message.R
import ru.digipeople.message.model.Message
import ru.digipeople.message.ui.activity.message.MessageActivity
import ru.digipeople.message.ui.activity.messagelist.MessageListActivity
import ru.digipeople.message.ui.fragment.messagelist.adapter.MessageAdapter
import ru.digipeople.message.ui.fragment.messagelist.model.Tab
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.ui.fragment.base.MvpFragment
import ru.digipeople.utils.model.UserError
import javax.inject.Inject

/**
 * Фрагмент списка сообщений
 */
class MessageListFragment : MvpFragment(), MessageListView {
    //region Di
    private lateinit var component: MessageListComponent
    @Inject
    lateinit var adapter: MessageAdapter
    @Inject
    lateinit var loadingDelegate: LoadingFragmentDelegate
    //endregion
    //region View
    private lateinit var tabs: RadioGroup
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    //endregion
    //region Other
    var onCreateMessageClickListener: (() -> Unit)? = null
    var onMessageClickListener: ((message: Message) -> Unit)? = null
    lateinit var presenter: MessageListPresenter
    //endregion
    /**
     * Действия при создани фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is MessageActivity) {
            component = (activity as MessageActivity).getComponent()
                    .messageListComponent()
        } else {
            component = (activity as MessageListActivity).getComponent()
                    .messageListComponent()
        }
        component.inject(this)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, MessageListPresenter::class.java)
        presenter.initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message_list, container, false)

        tabs = view.findViewById(R.id.tabs)
        /**
         * переключение вкладок
         */
        tabs.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.fragment_message_income -> presenter.onTabSelected(Tab.INCOME)
                R.id.fragment_message_outcome -> presenter.onTabSelected(Tab.OUTCOME)
            }
        }

        recyclerView = view.findViewById(R.id.fragment_message_list_recycler)

        fab = view.findViewById(R.id.fragment_message_list_add_button)
        fab.setOnClickListener { onCreateMessageClickListener?.invoke() }

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.onItemClickListener = presenter::onMessageClicked

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onScreenShown()
    }
    /**
     * отображени ошибки
     */
    override fun showError(userError: UserError) {
        Snackbar.make(recyclerView, userError.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * загрузка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Message>) {
        adapter.items = list
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisible(visible: Boolean) {
        loadingDelegate.setLoadingVisibility(visible)
    }
    /**
     * обработка нажатия на сообщение
     */
    override fun invokeMessageClickListener(message: Message) {
        onMessageClickListener?.invoke(message)
    }

    companion object {
        fun newInstance(): MessageListFragment {
            return MessageListFragment()
        }
    }
}