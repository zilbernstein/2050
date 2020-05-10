package ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.fragment_cyclic_work.*
import ru.digipeople.locotech.core.ui.helper.LoadingFragmentDelegate
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.ui.activity.inspection.InspectionActivity
import ru.digipeople.locotech.inspector.ui.activity.inspection.InspectionFilterState
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.AdapterData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.CyclicWorkAdapter
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.adapter.WorkData
import ru.digipeople.locotech.inspector.ui.activity.inspection.fragment.cyclicworks.decorator.DividerItemDecorator
import ru.digipeople.ui.fragment.base.MvpFragment
import javax.inject.Inject

/**
 * Фрагмент цикловых работ
 *
 * @author Kashonkov Nikita
 */
class CyclicWorksFragment : MvpFragment(), CyclicWorksView {
    //region Di
    private lateinit var component: CyclicWorksComponent
    @Inject
    lateinit var adapter: CyclicWorkAdapter
    //endRegion
    //region View
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
    private lateinit var dataFilter: TextView
    //endRegion
    //region Other
    val loadingFragmentDelegate = LoadingFragmentDelegate(this)
    lateinit var presenter: CyclicWorksPresenter
    var dataTransformDisposable = Disposables.disposed()
    private var currentFilter = InspectionFilterState.FILTER_ALL
    //endRegion
    /**
     * Действия при старте фрагмента
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = (requireActivity() as InspectionActivity).component().cyclicWorkComponent()
        component.inject(this)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter({ component.presenter() }, CyclicWorksPresenter::class.java)
        presenter.initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cyclic_work, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecorator(requireContext()))

        recyclerView.adapter = adapter
        adapter.controlPointClickListener = presenter::onControlPointClicked
        adapter.acceptAllClickListener = presenter::onAcceptAllClicked
        adapter.checkOtkClickListener = presenter::onCheckOtkWorkClicked
        adapter.checkRzdClickListener = presenter::onCheckRzdWorkClicked
        adapter.commentClickListener = presenter::onWorkCommentClicked
        adapter.photoClickListener = presenter::onPhotoClicked
        adapter.measurementClickListener = presenter::onMeasurementClicked
        /**
         * Формирование фильтра Все/активные
         */
        dataFilter = view.findViewById(R.id.filter)
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
    override fun setData(data: AdapterData?) {
        data?.let {
            adapter.setData(it)
        }
    }
    /**
     * Обновление замечания
     */
    override fun updateRemark(position: Int) {
        adapter.remarkItemChanged(position)
    }
    /**
     * Обновление работы
     */
    override fun updateWork(workData: WorkData, position: Int) {
        adapter.workItemChanged(workData, position)
    }
    /**
     * Установка чисоа замечаний
     */
    override fun setRemarkCount(count: Int) {
        (requireActivity() as InspectionActivity).setCyclicCount(count)
    }
    /**
     * Управление видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }
    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }
    /**
     * Обработка нажатия фильтра
     */
    private fun onFilterClicked() {
        when (currentFilter) {
            /**
             * Все
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
             * Активные
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

        fun newInstance(): CyclicWorksFragment {
            return CyclicWorksFragment()
        }
    }
}