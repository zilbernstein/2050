package ru.digipeople.locotech.inspector.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.dialog.adapter.RepairTypesAdapter
/**
 * диалог выбора типа ремонта
 */
class RepairTypeFilterDialog : DialogFragment() {
    private val adapter = RepairTypesAdapter()
    private val args: Arguments by lazy {
        arguments!!.getParcelable(ARGS) as Arguments
    }

    private var repairTypeFilterDialogListener: RepairTypeFilterDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        repairTypeFilterDialogListener = activity as RepairTypeFilterDialogListener?
    }
    /**
     * действия при создании окна
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_repair_type_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener {
            repairTypeFilterDialogListener?.onRepairTypesSelected(emptyList())
            dismiss()
        }

        view.findViewById<Button>(R.id.accept_btn).setOnClickListener {
            repairTypeFilterDialogListener?.onRepairTypesSelected(adapter.selectedItems.toList())
            dismiss()
        }

        view.findViewById<RecyclerView>(R.id.recycler_view).adapter = adapter.apply {
            selectedItems.addAll(args.selected)
            items = args.repairTypes
        }
    }

    companion object {
        private val TAG = "REPAIR_TYPES_FILTER_DIALOG"
        private const val ARGS = "ARGS"
        /**
         * отрисовка
         */
        fun show(repairTypes: Set<RepairTypeViewModel>, selected: List<RepairTypeViewModel>, activity: AppCompatActivity) {
            val fm = activity.supportFragmentManager
            (fm.findFragmentByTag(TAG) as RepairTypeFilterDialog?)?.dismiss()
            val newDialog = RepairTypeFilterDialog().apply {
                arguments = Bundle().also {
                    it.putParcelable(ARGS, Arguments(repairTypes.toList(), selected))
                }
            }

            newDialog.show(fm, TAG)
        }
    }
}
