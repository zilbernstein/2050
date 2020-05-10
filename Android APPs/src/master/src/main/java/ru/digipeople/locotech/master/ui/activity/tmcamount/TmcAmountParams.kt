package ru.digipeople.locotech.master.ui.activity.tmcamount

import android.os.Parcel
import android.os.Parcelable

/**
 * параметры ввода/изменения количества ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcAmountParams(val workId: String, val tmcId: String, val tmcTitle: String, val askedAmount: Double, val sectionLeft: Double, val stockLeft: Double, val uom: String) : Parcelable {
    /**
     * Конструктор
     */
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readDouble(),
            source.readString()
    )

    override fun describeContents() = 0
    /**
     * Упаковка данных
     */
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(workId)
        writeString(tmcId)
        writeString(tmcTitle)
        writeDouble(askedAmount)
        writeDouble(sectionLeft)
        writeDouble(stockLeft)
        writeString(uom)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TmcAmountParams> = object : Parcelable.Creator<TmcAmountParams> {
            /**
             * Извлечение данных
             */
            override fun createFromParcel(source: Parcel): TmcAmountParams = TmcAmountParams(source)
            override fun newArray(size: Int): Array<TmcAmountParams?> = arrayOfNulls(size)
        }
    }
}