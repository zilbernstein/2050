package ru.digipeople.locotech.inspector.data

/**
 * @author Kashonkov Nikita
 */
data class WorkModel(
        val sectionTitle: String,
        val title: String,
        val status: WorkStatus,
        val hasControlPoint: Boolean,
        val photoAmount: Int,
        val isMasterAccepted: Boolean,
        var isOtkAccepted: Boolean,
        val isRzdAccepted: Boolean
)