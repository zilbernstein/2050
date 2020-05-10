package ru.digipeople.locotech.master.ui.activity.comment

import android.os.Parcel
import android.os.Parcelable

/**
 * Параметры комментариев
 * @author Kashonkov Nikita
 */
class CommentParams constructor(val id: String, val title: String, val text: String?) : Parcelable {
    /**
     * конструктор
     */
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0
    /**
     * упаковка данных
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
             * извлечение данных
             */
            override fun createFromParcel(source: Parcel): CommentParams = CommentParams(source)
            override fun newArray(size: Int): Array<CommentParams?> = arrayOfNulls(size)
        }
    }
}