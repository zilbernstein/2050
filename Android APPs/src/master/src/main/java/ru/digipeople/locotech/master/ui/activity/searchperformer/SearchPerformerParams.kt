package ru.digipeople.locotech.master.ui.activity.searchperformer

import android.os.Parcel
import android.os.Parcelable

/**
 * Параметры выбора сотрудника / исполнителя
 *
 * @author Kashonkov Nikita
 */
class SearchPerformerParams constructor(val workId: String, val workTitle: String, val outfitPercent: Int, val workNormal: Long, val workRemain: Long, val outfitTitle: String?) : Parcelable {
    /**
     * Конструктор
     */
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readLong(),
            source.readLong(),
            source.readString()
    )

    override fun describeContents() = 0
    /**
     * Упаковка данных
     */
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(workId)
        writeString(workTitle)
        writeInt(outfitPercent)
        writeLong(workNormal)
        writeLong(workRemain)
        writeString(outfitTitle)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SearchPerformerParams> = object : Parcelable.Creator<SearchPerformerParams> {
            /**
             * Извлечение данных
             */
            override fun createFromParcel(source: Parcel): SearchPerformerParams = SearchPerformerParams(source)
            override fun newArray(size: Int): Array<SearchPerformerParams?> = arrayOfNulls(size)
        }
    }
}