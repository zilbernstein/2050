package ru.digipeople.locotech.master.ui.activity.tmclist

import android.os.Parcel
import android.os.Parcelable

/**
 * Параметры списка ТМЦ
 *
 * @author Kashonkov Nikita
 */
class TmcListParameters constructor(val workId: String, val workTitle: String) : Parcelable {
    /**
     * Конструктор
     */
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0
    /**
     * Упаковка данных
     */
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(workId)
        writeString(workTitle)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TmcListParameters> = object : Parcelable.Creator<TmcListParameters> {
            /**
             * Извлечение данных
             */
            override fun createFromParcel(source: Parcel): TmcListParameters = TmcListParameters(source)
            override fun newArray(size: Int): Array<TmcListParameters?> = arrayOfNulls(size)
        }
    }
}