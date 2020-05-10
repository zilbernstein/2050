package ru.digipeople.locotech.worker.ui.activity.measurements

import android.os.Parcel
import android.os.Parcelable
import ru.digipeople.locotech.worker.model.WorkStatus
/**
 * Параметры для замеров
 */
class MeasurementsParams(
        val workId: String,
        val workName: String,
        val workStatus: WorkStatus
) : Parcelable {
    /**
     * Конструктор
     */
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            WorkStatus.valueOf(parcel.readInt())!!) {
    }
    /**
     * Упаковка данных
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(workId)
        parcel.writeString(workName)
        parcel.writeInt(workStatus.code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MeasurementsParams> {
        /**
         * Извлечение данных
         */
        override fun createFromParcel(parcel: Parcel): MeasurementsParams {
            return MeasurementsParams(parcel)
        }

        override fun newArray(size: Int): Array<MeasurementsParams?> {
            return arrayOfNulls(size)
        }
    }

}