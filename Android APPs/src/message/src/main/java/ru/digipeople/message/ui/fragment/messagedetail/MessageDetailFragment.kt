package ru.digipeople.message.ui.fragment.messagedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.digipeople.message.R
import ru.digipeople.message.model.Message
import ru.digipeople.message.model.MessageStatus
import ru.digipeople.message.ui.activity.MessageActivityNavigator
import ru.digipeople.message.ui.activity.message.MessageActivity
import ru.digipeople.message.ui.activity.messagedetail.MessageDetailActivity
import ru.digipeople.ui.fragment.base.MvpFragment
import javax.inject.Inject

/**
 * Фрагмента деталки сообщения
 */
class MessageDetailFragment : MvpFragment(), MessageDetailView {
    //region Di
    private lateinit var component: MessageDetailComponent
    @Inject
    lateinit var navigator: MessageActivityNavigator
    //endregion
    //region Views
    lateinit var addressee: TextView
    lateinit var text: TextView
    lateinit var resendBtn: TextView
    lateinit var whom: TextView
    //endregion
    //region Other
    lateinit var presenter: MessageDetailPresenter
    //endregion
    /**
     * действия при создании фрамента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = if (activity is MessageActivity) {
            (activity as MessageActivity).getComponent()
                    .messageDetailComponent()
        } else {
            (activity as MessageDetailActivity).getComponent()
                    .messageDetailComponent()
        }
        component.inject(this)
        /**
         * инициализация презентера
         */
        presenter = mvpDelegate.getPresenter({ component.presenter() }, MessageDetailPresenter::class.java)
        presenter.initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message_info, container, false)

        addressee = view.findViewById(R.id.fragment_message_info_recipient)
        text = view.findViewById(R.id.fragment_message_info_text)
        resendBtn = view.findViewById(R.id.fragment_message_info_send)

        whom = view.findViewById(R.id.fragment_message_info_whom)

        resendBtn.setOnClickListener { presenter.onResendBtnClicked() }
        return view
    }
    /**
     * переход к пересылке сообщения
     */
    override fun navigateToResendMessage() {
        navigator.navigateToResendMessageActivity(requireActivity())
    }
    /**
     * показать сообщение
     */
    override fun showMessage(message: Message?) {
        /**
         * пустое
         */
        if (message == null) {
            whom.text = ""
            addressee.text = ""
            text.text = ""
            resendBtn.visibility = View.GONE
            return
        }
        /**
         * входящее
         */
        if (!message.isIncoming) {
            whom.text = getString(R.string.message_activity_whom)
            addressee.text = message.recipients.joinToString(separator = ", ") { it.name }
        } else {
            whom.text = getString(R.string.message_activity_from_whom)
            addressee.text = message.sender.name
        }
        text.text = message.text

        if (message.messageStatus == MessageStatus.UNKNOWN) {
            resendBtn.visibility = View.VISIBLE
        } else {
            resendBtn.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): MessageDetailFragment {
            return MessageDetailFragment()
        }
    }
}