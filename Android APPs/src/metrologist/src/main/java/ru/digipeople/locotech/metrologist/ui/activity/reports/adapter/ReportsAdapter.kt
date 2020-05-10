package ru.digipeople.locotech.metrologist.ui.activity.reports.adapter

import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import ru.digipeople.locotech.metrologist.R
import ru.digipeople.locotech.metrologist.data.model.Report
import ru.digipeople.ui.adapter.BaseItemsRecyclerAdapter
/**
 * Адаптер отчетов
 */
class ReportsAdapter : BaseItemsRecyclerAdapter<Report, ReportsAdapter.ViewHolder>() {

    val webViewHeaders = mutableMapOf<String, String>()
    /**
     * Создание холдера
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item_report, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bind(items[position])
    }
    /**
     * Холдер отчета
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val webView = view as WebView
        /**
         * Инициализация
         */
        init {
            val webSettings = webView.settings
            webSettings.builtInZoomControls = true
            webSettings.displayZoomControls = false
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
        }
        /**
         * Привязка данных к шаблону
         */
        fun bind(report: Report) {
            webView.loadUrl(report.url, webViewHeaders)
        }
    }
}