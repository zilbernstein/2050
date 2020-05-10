package ru.digipeople.locotech.inspector.data

/**
 * @author Kashonkov Nikita
 */
class EquipmentDataRepository {
    val equipmentRemarkList: MutableList<EquipmentRemarkModel>

    init {
        val firstWork = WorkModel("", "Проверка проката по кругу катания с помощью шаблона", WorkStatus.DONE, false, 0, true, false, false)
        val secondWork = WorkModel("", "Проверка шаблоном (И 433.01) высоты гребня", WorkStatus.DONE, true,  0, true, true, true)
        val thirddWork = WorkModel("", "Проверка шаблоном (И 433.01) толщины гребня", WorkStatus.IN_WORK, true,  0, false, false, false)
        val firstRemarkWorkList = listOf(firstWork, secondWork, thirddWork)

        val fourthWork = WorkModel("", "Проверка проката по кругу катания с помощью шаблона", WorkStatus.IN_WORK, true, 0, false, false, false)
        val fifthhWork = WorkModel("", "Проверка шаблоном (И 433.01) высоты гребня", WorkStatus.NEW, false, 0, false, false, false)
        val secondRemarkWorkList = listOf(fourthWork, fifthhWork)

        val sixthWork = WorkModel("", "Проверка шаблоном (И 433.01) толщины гребня", WorkStatus.NEW, true, 0, false, false, false)
        val seventhWork = WorkModel("", "Проверка крутизны гребня шаблоном (УТ-1)", WorkStatus.DONE, false, 0, true, false, false)
        val thirdRemarkWorkList = listOf(sixthWork, seventhWork)

        equipmentRemarkList = mutableListOf()
        equipmentRemarkList.add(EquipmentRemarkModel("Колесная пара локомотива", firstRemarkWorkList))
        equipmentRemarkList.add(EquipmentRemarkModel("Колесная пара локомотива", secondRemarkWorkList))
        equipmentRemarkList.add(EquipmentRemarkModel("Колесная пара локомотива", thirdRemarkWorkList))
    }

    private object Holder {
        val INSTANCE = EquipmentDataRepository()
    }

    companion object {
        val INSTANCE: EquipmentDataRepository by lazy { Holder.INSTANCE }
    }
}
