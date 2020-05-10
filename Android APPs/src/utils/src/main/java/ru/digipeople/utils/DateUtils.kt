package ru.digipeople.utils

/**
 * Класс для работы с датой
 *
 * @author Kashonkov Nikita
 */
class DateUtils {
    companion object {
        /**
         * Возвращает строку формата HH:mm:ss
         */
        fun convertToString(time: Long): String {
            var hours = time / 1000 / 60 / 60
            var minutes = time / 1000 / 60 - hours * 60
            var seconds = time / 1000 - hours * 60 * 60 - minutes * 60

            val stringBuilder = StringBuilder()
            /**
             * Установка отрицательного значения
             */
            if (hours < 0 || minutes < 0 || seconds < 0) {
                stringBuilder.append('-')
            }

            hours = Math.abs(hours)
            minutes = Math.abs(minutes)
            seconds = Math.abs(seconds)
            /**
             * добавление 0 еслди значение меньше 10
             */
            if (hours < 10) {
                stringBuilder.append("0$hours")
            } else {
                stringBuilder.append(hours)
            }
            /**
             * добавление разделителя :
             */
            stringBuilder.append(":")

            if (minutes < 10) {
                stringBuilder.append("0$minutes")
            } else {
                stringBuilder.append(minutes)
            }
            /**
             * добавление разделителя :
             */
            stringBuilder.append(":")

            if (seconds < 10) {
                stringBuilder.append("0$seconds")
            } else {
                stringBuilder.append(seconds)
            }
            return stringBuilder.toString()
        }
    }
}