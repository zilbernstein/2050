package ru.digipeople.locotech.core.ui.activity.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.core.CoreAppComponent
import ru.digipeople.locotech.core.R
import ru.digipeople.locotech.core.ui.activity.base.AppActivity
import ru.digipeople.locotech.core.ui.activity.settings.adapter.UrlsAdapter
import ru.digipeople.locotech.core.ui.activity.settings.model.ThingWorxEndpoint
import ru.digipeople.ui.activity.base.ActivityModule
import ru.digipeople.ui.activity.base.ToolbarDelegate
import javax.inject.Inject

/**
 * Экран настроек приложения.
 *
 * @author Aleksandr Brazhkin
 */
class SettingsActivity : AppActivity() {

    //region Di
    private val component by lazy {
        DaggerSettingsComponent.builder()
                .settingsModule(SettingsModule())
                .activityModule(ActivityModule(this))
                .coreAppComponent(CoreAppComponent.get())
                .build()
    }
    @Inject
    lateinit var adapter: UrlsAdapter
    @Inject
    lateinit var toolbarDelegate: ToolbarDelegate
    //endregion
    //region View
    lateinit var recyclerView: RecyclerView
    lateinit var url: EditText
    //endregion
    //region Other
    private lateinit var viewModel: SettingsViewModel
    //endregion
    /**
     * Действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = CoreAppComponent.get().screenOrientation()
        super.onCreate(savedInstanceState)
        component.inject(this)
        setContentView(R.layout.activity_settings)
        /**
         * Инициализация тулбара
         */
        toolbarDelegate.init()
        toolbarDelegate.setTitle(R.string.settings_title)
        toolbarDelegate.setNavigationIcon(R.drawable.ic_toolbar_back)
        toolbarDelegate.setNavigationOnClickListener { onBackPressed() }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.onItemClickListener = {
            url.setText(it.url)
        }

        url = findViewById(R.id.url)

        viewModel = ViewModelProviders.of(this, component.viewModelFactory()).get(SettingsViewModel::class.java)
        viewModel.also {
            it.urlLiveData.observe({ lifecycle }, ::setUrl)
            it.urlsLiveData.observe({ lifecycle }, ::setUrls)
            it.start()
        }
    }
    /**
     * Создание меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return true
    }
    /**
     * Обработка пункта меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.apply -> {
                viewModel.onApplyBtnClicked(url.text.toString())
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    /**
     * Установка URL
     */
    private fun setUrl(url: String) {
        this.url.setText(url)
    }

    private fun setUrls(items: List<ThingWorxEndpoint>) {
        adapter.items = items
    }

    companion object {
        fun getCallingIntent(context: Context) = Intent(context, SettingsActivity::class.java)
    }
}