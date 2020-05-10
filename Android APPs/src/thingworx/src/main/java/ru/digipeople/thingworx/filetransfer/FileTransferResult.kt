package ru.digipeople.thingworx.filetransfer

/**
 * Результат передачи файлов
 *
 * @author Kashonkov Nikita
 */
enum class FileTransferResult(val description: String) {
    /**
     *  Успешный результат
     */
    SUCCESS("VALIDATED"),
    /**
     * Ошибка
     */
    ERROR("ERROR");

    companion object {
        fun descriptionOf(description: String): FileTransferResult {
            val result = FileTransferResult.values().firstOrNull { it.description.equals(description, true) }
            if (result == null) return ERROR
            return result
        }
    }
}