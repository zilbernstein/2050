package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_remark_rzd.*
import ru.digipeople.locotech.core.ui.helper.LoadingFragmentDelegate
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.inspection.InspectionActivity
import ru.digipeople.locotech.inspector.ui.activity.inspection.InspectionFilterState
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.RemarkRzdAdapter
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.adapter.WorkData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.rzdremarks.decorator.DividerItemDecorator
import ru.digipeople.ui.fragment.base.MvpFragment
import javax.inject.Inject

/**
 * Фрагмент замечаний РЖД
 *
 * @author Kashonkov Nikita
 */
class RzdRemarksFragment : MvpFragment(), RzdRemarksView {
    //region Di
    private lateinit var component: RzdRemarksComponent
    @Inject
    lateinit var adapter: RemarkRzdAdapter
    //endRegion
    //region View
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var dataFilter: TextView
    lateinit var createRemarkButton: Button
    //endRegion
    //region Other
    val loadingFragmentDelegate = LoadingFragmentDelegate(this)
    lateinit var presenter: RzdRemarksPresenter
    private var currentFilter = InspectionFilterState.FILTER_ALL
    //endRegion
    /**
     * Действия при создании фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = (requireActivity() as InspectionActivity).component().remarkRzdComponent()
        component.inject(this)
        /**
         * инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, RzdRemarksPresenter::class.java)
        presenter.initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_remark_rzd, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecorator(requireContext()))
        recyclerView.adapter = adapter
        adapter.acceptAllClickListener = presenter::onAcceptAllClicked
        adapter.checkOtkClickListener = presenter::onCheckOtkWorkClicked
        adapter.checkRzdClickListener = presenter::onCheckRzdWorkClicked
        adapter.commentWorkClickListener = presenter::onWorkCommentCLicked
        adapter.commentRemarkClickListener = presenter::onRemarkCommentCLicked
        adapter.deleteRemarkClickListener = presenter::onRemarkDeleteClicked
        adapter.photoClickListener = presenter::onWorkPhotoClicked
        adapter.photoRemarkClickListener = presenter::onRemarkPhotoClciked

        createRemarkButton = view.findViewById(R.id.new_remark)
        createRemarkButton.setOnClickListener { presenter.onCreateRemarkClicked() }
        dataFilter = view.findViewById(R.id.filter)
        /**
         * Установка фильтра Все/активные
         */
        var span = SpannableString(getString(R.string.inspection_activity_filter))
        span.setSpan(ForegroundColorSpan(ContextCompat.getColor(this.context!!, R.color.appBlack)),
                0, 6,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        dataFilter.text = span
        dataFilter.setOnClickListener {
            onFilterClicked()
            presenter.onFilterChange(currentFilter)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onScreenShown()
    }

    override fun onPause() {
        loadingFragmentDelegate.setLoadingVisibility(false)
        super.onPause()
    }
    /**
     * Установка данных
     */
    override fun setData(adapterData: AdapterData?) {
        adapterData?.let { adapter.setData(it) }
    }
    /**
     * обновление замечания
     */
    override fun updateRemark(position: Int) {
        adapter.remarkItemChanged(position)
    }
    /**
     * обновление работы
     */
    override fun updateWork(workData: WorkData, position: Int) {
        adapter.workItemChanged(workData, position)
    }
    /**
     * установка числа замечаний
     */
    override fun setRemarkCount(count: Int) {
        (requireActivity() as InspectionActivity).setRzdRemarkCount(count)
    }
    /**
     * управленние видимостью кнопки
     */
    override fun setCreateButtonVisibility(isVisible: Boolean) {
        if (isVisible) {
            createRemarkButton.visibility = View.VISIBLE
        } else {
            createRemarkButton.visibility = View.GONE
        }
    }
    /**
     * управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * переключение фильтра
     */
    private fun onFilterClicked() {
        when (currentFilter) {
            /**
             * все
             */
            InspectionFilterState.FILTER_ALL -> {
                currentFilter = InspectionFilterState.FILTER_ACTIVE
                var span = SpannableString(getString(R.string.inspection_activity_filter))
                span.setSpan(ForegroundColorSpan(ContextCompat.getColor(this.context!!, R.color.appBlack)),
                        4, 14,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                filter.text = span
            }
            /**
             * активные
             */
            InspectionFilterState.FILTER_ACTIVE -> {
                currentFilter = InspectionFilterState.FILTER_ALL
                var span = SpannableString(getString(R.string.inspection_activity_filter))
                span.setSpan(ForegroundColorSpan(ContextCompat.getColor(this.context!!, R.color.appBlack)),
                        0, 6,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                dataFilter.text = span
            }
        }
    }

    companion object {

        fun newInstance(): RzdRemarksFragment {
            return RzdRemarksFragment()
        }
    }
}