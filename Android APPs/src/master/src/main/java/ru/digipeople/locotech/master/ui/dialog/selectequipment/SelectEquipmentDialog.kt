package ru.digipeople.locotech.master.ui.dialog.selectequipment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.data.model.ShortEquipment
import ru.digipeople.locotech.master.AppComponent
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.dialog.selectequipment.adapter.EquipmentsAdapter
import ru.digipeople.ui.UiComponent
import ru.digipeople.utils.android.AndroidUtils
import ru.digipeople.utils.model.UserError

/**
 * Диалог выбора оборудования из бокового меню.
 */
class SelectEquipmentDialog : DialogFragment() {

    //region Di
    private val component by lazy {
        DaggerSelectEquipmentComponent.builder()
                .appComponent(AppComponent.get())
                .coreAppComponent(CoreAppComponent.get())
                .uiComponent(UiComponent.get())
                .build()
    }
    //endregion
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressLayout: View
    //region Other
    private val equipmentsAdapter = EquipmentsAdapter()
    private val viewModel by lazy {
        ViewModelProviders.of(this, component.viewModelFactory()).get(SelectEquipmentViewModel::class.java)
    }
    var onDismissListener: ((DialogInterface) -> Unit)? = null
    //endregion
    /**
     * Действия при создании окна
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_select_equipment, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.gray_divider)!!)
        recyclerView.addItemDecoration(dividerItemDecoration)

        recyclerView.adapter = equipmentsAdapter
        equipmentsAdapter.onItemClickListener = viewModel::onEquipmentClicked

        progressLayout = view.findViewById(R.id.progressLayout)

        viewModel.apply {
            val lifecycleOwner = { viewLifecycleOwner.lifecycle }
            equipmentsLiveData.observe(lifecycleOwner, ::setEquipments)
            loadingLiveData.observe(lifecycleOwner, ::setLoading)
            userErrorLiveData.observe(lifecycleOwner, ::showError)
            selectedEquipmentIdLiveData.observe(lifecycleOwner, ::setSelectedEquipmentId)
            dismissEventLiveData.observe(lifecycleOwner, { dismiss() })
            start()
        }

        return view
    }
    /**
     * Действия при возобновлении отображения окна
     */
    override fun onResume() {
        super.onResume()
        val width = AndroidUtils.dpToPx(600f, requireContext()).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    /**
     * Обработка отмены
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke(dialog)
    }
    /**
     * Выбор оборудования
     */
    private fun setEquipments(equipments: List<ShortEquipment>) {
        equipmentsAdapter.items = equipments
    }
    /**
     * Управление видимостью загрузки
     */
    private fun setLoading(loading: Boolean) {
        recyclerView.isInvisible = loading
        progressLayout.isInvisible = !loading
        isCancelable = !loading
    }
    /**
     * Отображение ошиибки
     */
    private fun showError(userError: UserError) {
        Snackbar.make(recyclerView, userError.message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Установка ид выбранного оборудования
     */
    private fun setSelectedEquipmentId(equipmentId: String) {
        equipmentsAdapter.selectedEquipmentId = equipmentId
    }

    companion object {
        fun newInstance() = SelectEquipmentDialog()
    }
}