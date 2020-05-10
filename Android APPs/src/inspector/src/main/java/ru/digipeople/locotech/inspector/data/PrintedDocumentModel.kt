package ru.digipeople.locotech.inspector.data

import ru.digipeople.locotech.inspector.model.Signer

/**
 * @author Kashonkov Nikita
 */
data class PrintedDocumentModel(
        val title: String,
        val acceptSigners: MutableSet<Signer>,
        val checkSigners: MutableSet<Signer>,
        val controlSigners: MutableSet<Signer>,
        val approveSigners: MutableSet<Signer>
)