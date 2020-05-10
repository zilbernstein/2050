package ru.digipeople.locotech.inspector.data

/**
 * @author Kashonkov Nikita
 */
class RepairPageRepository private constructor() {
    val repairPages: List<RepairPage>

    init {
        repairPages = listOf(
                RepairPage("ТУ-152", ""),
                RepairPage("Замеры", ""),
                RepairPage("Испытания", ""),
                RepairPage("Смена оборудования", ""),
                RepairPage("Замечания", ""),
                RepairPage("ТУ-162", "file:///android_asset/tu-162 -tp1.htm"),
                RepairPage("ТУ-31Л", "file:///android_asset/tu31l.htm"),
                RepairPage("Акты допуска", "file:///android_asset/act-puti1.htm"),
                RepairPage("Технические акты", "file:///android_asset/act-test-tp1.htm"))
    }


    private object Holder {
        val INSTANCE = RepairPageRepository()
    }

    companion object {
        val INSTANCE: RepairPageRepository by lazy { Holder.INSTANCE }
    }
}