package ru.digipeople.locotech.inspector.data

/**
 * @author Kashonkov Nikita
 */
data class RemarkModel(
        val title: String,
        val author: String,
        val date: String,
        val works: List<WorkModel>,
        val photoAmount: Int
)