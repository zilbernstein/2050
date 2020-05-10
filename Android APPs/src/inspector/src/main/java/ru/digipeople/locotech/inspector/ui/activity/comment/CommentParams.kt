package ru.digipeople.locotech.inspector.ui.activity.comment

import android.os.Parcel
import android.os.Parcelable

/**
 * Класс параметры, передаваемы при переходе к комментариям
 *
 * @author Kashonkov Nikita
 */
class CommentParams constructor(val id: String, val title: String, val text: String?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0
    /**
     * Упаковка параметров
     */
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeString(text)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CommentParams> = object : Parcelable.Creator<CommentParams> {
            /**
             * Извлечение данных
             */
            override fun createFromParcel(source: Parcel): CommentParams = CommentParams(source)
            override fun newArray(size: Int): Array<CommentParams?> = arrayOfNulls(size)
        }
    }
}