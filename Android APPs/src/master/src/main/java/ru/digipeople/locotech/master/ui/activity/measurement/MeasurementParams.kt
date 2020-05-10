package ru.digipeople.locotech.master.ui.activity.measurement

import android.os.Parcel
import android.os.Parcelable
/**
 * Параметры замеров
 *
 * @author Sostavkin Grisha
 */
class MeasurementParams(
        val workId: String,
        val workName: String,
        val workStatus: Int
) : Parcelable {
    /**
     * конструктор
     */
    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString(), parcel.readInt())
    /**
     * упаковка параметров
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(workId)
        parcel.writeString(workName)
        parcel.writeInt(workStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MeasurementParams> {
        /**
         * извлечение данных
         */
        override fun createFromParcel(parcel: Parcel): MeasurementParams {
            return MeasurementParams(parcel)
        }

        override fun newArray(size: Int): Array<MeasurementParams?> {
            return arrayOfNulls(size)
        }
    }
}