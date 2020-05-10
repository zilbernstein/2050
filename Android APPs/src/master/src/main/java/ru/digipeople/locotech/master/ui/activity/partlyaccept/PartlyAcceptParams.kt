package ru.digipeople.locotech.master.ui.activity.partlyaccept

import android.os.Parcel
import android.os.Parcelable

/**
 * параметры частичной приемки
 *
 * @author Kashonkov Nikita
 */
class PartlyAcceptParams constructor(val workId: String, val workTitle: String, val outfitPercent: Int, val workNormal: Long, val outfitTitle: String?) : Parcelable {
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
        val CREATOR: Parcelable.Creator<PartlyAcceptParams> = object : Parcelable.Creator<PartlyAcceptParams> {
            /**
             * извлечение данных
             */
            override fun createFromParcel(source: Parcel): PartlyAcceptParams = PartlyAcceptParams(source)
            override fun newArray(size: Int): Array<PartlyAcceptParams?> = arrayOfNulls(size)
        }
    }
}