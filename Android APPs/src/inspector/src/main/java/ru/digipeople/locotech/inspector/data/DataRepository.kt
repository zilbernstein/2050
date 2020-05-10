package ru.digipeople.locotech.inspector.data

/**
 * @author Kashonkov Nikita
 */
class DataRepository private constructor() {
    val cyclicRemarkList: MutableList<CyclicRemark>
    val otkRemarkList: MutableList<RemarkModel>
    val rzdRemarkList: MutableList<RemarkModel>

    init {
        val firstWork = WorkModel("Участок 01 - ЛО - 234567890", "Снятие оновных шплинтов", WorkStatus.MASTER_ACCEPTED, false,  0, true, false, false)
        val secondWork = WorkModel("Участок 01 - ЛО - 234567890", "Демонтаж роликов", WorkStatus.MASTER_ACCEPTED, true,  0, true, true, false)
        val firstRemarkWorkList = listOf(firstWork, secondWork)

        val thirdWork = WorkModel("Участок 02 - ЛО - 0987654321", "Снятие кожуха", WorkStatus.IN_WORK, true,  0, false, false, false)
        val fourthWork = WorkModel("Участок 02 - ЛО - 0987654321", "Демонтаж крышки оновного отсека", WorkStatus.NEW, false, 0, false, false, false)
        val secondRemarkWorkList = listOf(thirdWork, fourthWork)

        val fifthWork = WorkModel("Участок 02 - ЛО - 546328971", "Снятие оновных шплинтов", WorkStatus.IN_TASK, true,  0, false, false, false)
        val sixthWork = WorkModel("Участок 02 - ЛО - 546328971", "Демонтаж роликов", WorkStatus.DONE, false, 0, false, false, false)
        val thirdRemarkWorkList = listOf(fifthWork, sixthWork)

        cyclicRemarkList = mutableListOf()
        cyclicRemarkList.add(CyclicRemark("Группа работ по замене колесной пары", firstRemarkWorkList))
        cyclicRemarkList.add(CyclicRemark("Группа работ по демонтажу/установке двигателя", secondRemarkWorkList))
        cyclicRemarkList.add(CyclicRemark("Группа работ по замене колесной пары", thirdRemarkWorkList))

        val firstOtkWork = WorkModel("Участок 01 - ЛО - 234567890", "Разгерметизация", WorkStatus.MASTER_ACCEPTED, false,  0, true, true, true)
        val secondOtkWork = WorkModel("Участок 01 - ЛО - 234567890", "Обезжиривание и зачистка", WorkStatus.DONE, false,  0, false, false, false)
        val thirdOtkWork = WorkModel("Участок 01 - ЛО - 234567890", "Наложение швов", WorkStatus.IN_WORK, false, 0, false, false, false)
        val firstRemarkOtkWorkList = listOf(firstOtkWork, secondOtkWork, thirdOtkWork)

        val fourthOtkWork = WorkModel("Участок 01 - ЛО - 234567890", "Установка колпачков", WorkStatus.NOT_APPROVED, false,  0, false, false, false)
        val secondRemarkOtkWorkList = listOf(fourthOtkWork)

        otkRemarkList = mutableListOf()
        otkRemarkList.add(RemarkModel("Трещина на корпусе", "Слуцкий А.В.", "23-10-2018 15:20", firstRemarkOtkWorkList, 0))
        otkRemarkList.add(RemarkModel("Отсутствие защитных колпачков", "Зеленский П.К.", "29-12-2018 00:15", secondRemarkOtkWorkList, 0))
        otkRemarkList.add(RemarkModel("Некачественная сборка узла", "Попков Н.Н.", "1-02-2018 20:30", listOf(), 0))

        val firstRzdWork = WorkModel("Участок 01 - ЛО - 234567890", "Снятие крепежа", WorkStatus.MASTER_ACCEPTED, false, 0, true, false, false)
        val secondRzdWork = WorkModel("Участок 01 - ЛО - 234567890", "Установка панели управления", WorkStatus.DONE, false,  0, false, false, false)
        val thirdRzdWork = WorkModel("Участок 01 - ЛО - 234567890", "Установка крепежа", WorkStatus.IN_WORK, false, 0, false, false, false)
        val firstRemarkRzdWorkList = listOf(firstRzdWork, secondRzdWork, thirdRzdWork)

        val fourthRzdWork = WorkModel("Участок 01 - ЛО - 234567890", "Долив масла", WorkStatus.IN_WORK, false,  0, false, false, false)
        val secondRemarkRzdWorkList = listOf(fourthRzdWork)

        rzdRemarkList = mutableListOf()
        rzdRemarkList.add(RemarkModel("Некорректная установка панели упарвления", "Слуцкий А.В.", "23-10-2018 15:20", firstRemarkRzdWorkList, 0))
        rzdRemarkList.add(RemarkModel("Отсутствие масла в коллекторе", "Зеленский П.К.", "29-12-2018 00:15", secondRemarkRzdWorkList, 0))


    }

    private object Holder {
        val INSTANCE = DataRepository()
    }

    companion object {
        val INSTANCE: DataRepository by lazy { Holder.INSTANCE }
    }
}