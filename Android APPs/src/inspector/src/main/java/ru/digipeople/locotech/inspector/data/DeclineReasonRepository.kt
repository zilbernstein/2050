package ru.digipeople.locotech.inspector.data

/**
 * @author Kashonkov Nikita
 */
class DeclineReasonRepository  private constructor() {
    val declineReasonList: List<String>

    init {
        declineReasonList = listOf("Работа(-ы) в замечании уже выполнена(-ы)", "Выполнение работ не требуется", "Некорректное замечание", "Другие причины")
    }

    private object Holder {
        val INSTANCE = DeclineReasonRepository()
    }

    companion object {
        val INSTANCE: DeclineReasonRepository by lazy { Holder.INSTANCE }
    }
}
