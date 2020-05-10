package ru.digipeople.locotech.master.ui.activity.writeofftmcamount

import android.os.Parcel
import android.os.Parcelable

/**
 * Параметры списания ТМЦ
 *
 * @author Kashonkov Nikita
 */
class WriteOffTmcAmountParams(val workId: String, val tmcId: String, val tmcTitle: String, val asked: Double, val normal: Double, val uom: String) : Parcelable {
    /**
     * Конструктор
     */
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
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
        writeDouble(asked)
        writeDouble(normal)
        writeString(uom)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WriteOffTmcAmountParams> = object : Parcelable.Creator<WriteOffTmcAmountParams> {
            /**
             * Извлечение данных
             */
            override fun createFromParcel(source: Parcel): WriteOffTmcAmountParams = WriteOffTmcAmountParams(source)
            override fun newArray(size: Int): Array<WriteOffTmcAmountParams?> = arrayOfNulls(size)
        }
    }
}