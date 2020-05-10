package ru.digipeople.locotech.master.ui.activity.measurement

import android.os.Parcel
import android.os.Parcelable
import ru.digipeople.locotech.core.data.model.MeasureValueType
import ru.digipeople.locotech.core.data.model.Stage
import java.util.*

/**
 * Параметры замеров (для передачи между экранами)
 *
 * @author Sostavkin Grisha
 */
class MeasureParams constructor(val workName: String,
                                val workId: String,
                                val workStatus: Int,
                                val measureId: String,
                                val characteristicId: String,
                                val characteristicName: String,
                                val measureNorm: String,
                                val measureDate: Date?,
                                val performerName: String,
                                val measureStage: Stage,
                                val measureValue: String,
                                val measureType: MeasureValueType,
                                val commentText: String,
                                val measureName: String,
                                val isHardwareMeasurement: Boolean) : Parcelable {
    /**
     * конструктор
     */
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong().let { if (it == -1L) null else Date(it) },
            source.readString(),
            Stage.values()[source.readInt()],
            source.readString(),
            MeasureValueType.values()[source.readInt()],
            source.readString(),
            source.readString(),
            source.readInt() == 1
    )

    override fun describeContents() = 0
    /**
     * упаковка данных
     */
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(workName)
        writeString(workId)
        writeInt(workStatus)
        writeString(measureId)
        writeString(characteristicId)
        writeString(characteristicName)
        writeString(measureNorm)
        writeLong(measureDate?.time ?: -1L)
        writeString(performerName)
        writeInt(measureStage.ordinal)
        writeString(measureValue)
        writeInt(measureType.ordinal)
        writeString(commentText)
        writeString(measureName)
        writeInt(if (isHardwareMeasurement) 1 else 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MeasureParams> = object : Parcelable.Creator<MeasureParams> {
            /**
             * извлечение данных
             */
            override fun createFromParcel(source: Parcel): MeasureParams = MeasureParams(source)
            override fun newArray(size: Int): Array<MeasureParams?> = arrayOfNulls(size)
        }
    }
}