package ru.digipeople.locotech.technologist.ui.activity.remarks.dialog.approve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.technologist.R

/**
 * Диалог подтверждения
 */
class ApproveDialog : DialogFragment() {

    //region Args
    private val reject by lazy { arguments!!.getBoolean(ARG_REJECT) }
    private val works by lazy { arguments!!.getStringArrayList(ARG_WORKS) }
    //endregion
    //region Views
    private lateinit var approveRecycler: RecyclerView
    private lateinit var title: TextView
    private lateinit var acceptBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var dialogView: View
    //endregion
    //region Other
    private val adapter = ApproveAdapter()
    var onCancelBtnClickListener: (() -> Unit)? = null
    var onOkBtnClickListener: (() -> Unit)? = null
    //endregion
    /**
     * Действия при создании диалога
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_approve, container)

        title = view.findViewById(R.id.fragment_approve_title)
        acceptBtn = view.findViewById(R.id.fragment_approve_approved_button)
        cancelBtn = view.findViewById(R.id.fragment_approve_rejected_button)
        dialogView = view.findViewById(R.id.constraint_layout)
        approveRecycler = view.findViewById(R.id.fragment_approve_approved_recycler)
        /**
         * Заголовок
         */
        val titleRes = if (reject) {
            R.string.approve_fragment_reject_title
        } else {
            R.string.approve_fragment_accept_title
        }
        title.text = getString(titleRes)

        approveRecycler.adapter = adapter
        adapter.items = works

        /**
         * Обрабокта нажатия принять
         */
        acceptBtn.setOnClickListener {
            dismiss()
            onOkBtnClickListener?.invoke()
        }
        /**
         * Обрабокта нажатия отменить
         */
        cancelBtn.setOnClickListener {
            dismiss()
            onCancelBtnClickListener?.invoke()
        }

        return view
    }

    companion object {
        const val TAG = "APPROVE_DIALOG_TAG"
        private const val ARG_REJECT = "ARG_REJECT"
        private const val ARG_WORKS = "ARG_WORKS"
        /**
         * Создание окна
         */
        fun newInstance(works: List<String>, reject: Boolean): ApproveDialog {
            return ApproveDialog().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_REJECT, reject)
                    putStringArrayList(ARG_WORKS, ArrayList(works))
                }
            }
        }
    }
}