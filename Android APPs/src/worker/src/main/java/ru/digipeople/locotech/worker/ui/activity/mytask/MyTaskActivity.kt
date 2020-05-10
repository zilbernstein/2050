package ru.digipeople.locotech.worker.ui.activity.mytask

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.MvpActivity
import ru.digipeople.ui.activity.base.ToolbarDelegate
import ru.digipeople.ui.delegate.LoadingFragmentDelegate
import ru.digipeople.locotech.worker.AppComponent
import ru.digipeople.locotech.worker.R
import ru.digipeople.locotech.worker.model.Equipment
import ru.digipeople.locotech.worker.ui.activity.Navigator
import ru.digipeople.locotech.worker.ui.activity.mytask.adapter.AdapterData
import ru.digipeople.locotech.worker.ui.activity.mytask.adapter.TaskSectionAdapter
import ru.digipeople.locotech.worker.ui.activity.mytask.adapter.TitleItem
import ru.digipeople.ui.activity.base.MainDrawerDelegate
import javax.inject.Inject

/**
 * Активити работы
 *
 * @author Kashonkov Nikita
 */
class MyTaskActivity : MvpActivity(), MyTaskView {
    //region DI
    private lateinit var screenComponent: MyTaskScreenComponent
    lateinit var component: MyTaskComponent
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var mainDrawerDelegate: MainDrawerDelegate
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    @Inject
    lateinit var adapter: TaskSectionAdapter
    @Inject
    lateinit var loadingFragmentDelegate: LoadingFragmentDelegate
    //endRegion
    //regionView
    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    private var dataTransformDisposable = Disposables.disposed()
    //endRegion
    //region Other
    private lateinit var presenter: MyTaskPresenter
    //endRegion
    /**
     * Действия при созданиии активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        screenComponent = getScreenComponent()
        component = screenComponent.component(ActivityModule(this))
        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_task)
        /**
         * Инициализация делегата
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.my_task_activity_title)
        /**
         * Инициализация бокового меню
         */
        mainDrawerDelegate.init(false)
        /**
         * Инициализация презентера
         */
        presenter = getMvpDelegate().getPresenter(component::presenter, MyTaskPresenter::class.java)
        presenter.initialize()

        recycler = findViewById(R.id.recycler_view)
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.taskClickListener = presenter::onItemClicked
    }
    /**
     * Действия при старте
     */
    override fun onStart() {
        super.onStart()
        presenter.onScreenShown()
    }
    /**
     * Действия при возобновлении активити
     */
    override fun onResume() {
        super.onResume()
        navigator.onResume(this)
    }
    /**
     * Действия при приостановкке активити
     */
    override fun onPause() {
        navigator.onPause()
        super.onPause()
    }
    /**
     * Обработка нажатия назад
     */
    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return screenComponent
    }
    /**
     * Установка данных в адаптер
     */
    override fun setDataToAdapter(list: List<Equipment>) {
        dataTransformDisposable = Single.fromCallable {
            val equipmentAdapterData = AdapterData()
            equipmentAdapterData.add(TitleItem(getString(R.string.my_task_activity_equipment)))

            val sectionAdapterData = AdapterData()
            sectionAdapterData.add(TitleItem(getString(R.string.my_task_activity_my_task)))
            /**
             * свой адаптер дял каждого типа
             */
            list.forEach { equipment ->
                equipmentAdapterData.add(equipment)
                equipmentAdapterData.add(View(this))

                equipment.sectionList.forEach { section ->
                    sectionAdapterData.add(section)

                    section.workList.forEach { work ->
                        sectionAdapterData.add(work)
                    }

                    sectionAdapterData.add(View(this))
                }
            }

            equipmentAdapterData.addAll(sectionAdapterData)

            equipmentAdapterData

        }
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe { setLoadingVisibility(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setLoadingVisibility(false)
                    adapter.setData(it)
                }, {
                    /**
                     * Обработка ошибки
                     */
                    setLoadingVisibility(false)
                    if (it.message != null) {
                        showError(it.message!!)
                    }
                })
    }
    /**
     * Управлдение видимостью лоадера
     */
    override fun setLoadingVisibility(isVisible: Boolean) {
        loadingFragmentDelegate.setLoadingVisibility(isVisible)
    }

    /**
     * Отображение ошибки
     */
    override fun showError(message: String) {
        Snackbar.make(recycler, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getScreenComponent(): MyTaskScreenComponent {
        val saved = lastCustomNonConfigurationInstance
        if (saved == null) {
            return AppComponent.get().myTaskScreenComponent()
        } else {
            return saved as MyTaskScreenComponent
        }
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, MyTaskActivity::class.java)
        }
    }
}