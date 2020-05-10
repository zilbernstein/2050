package ru.digipeople.locotech.master.ui.activity.divide

import android.os.Parcel
import android.os.Parcelable

/**
 * Параметры разделения работы
 *
 * @author Kashonkov Nikita
 */
class DivideParams constructor(val workId: String, val workTitle: String, val outfitPercent: Int, val workNormal: Long, val outfitTitle: String?) : Parcelable {
    /**
     * конструктор
     */
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readLong(),
            source.readString()
    )

    override fun describeContents() = 0
    /**
     * упаковка данных
     */
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(workId)
        writeString(workTitle)
        writeInt(outfitPercent)
        writeLong(workNormal)
        writeString(outfitTitle)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DivideParams> = object : Parcelable.Creator<DivideParams> {
            /**
             * извлечение данных
             */
            override fun createFromParcel(source: Parcel): DivideParams = DivideParams(source)
            override fun newArray(size: Int): Array<DivideParams?> = arrayOfNulls(size)
        }
    }
}