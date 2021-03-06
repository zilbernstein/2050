package ru.digipeople.locotech.inspector.ui.dialog

import android.os.Parcel
import android.os.Parcelable

class RepairTypeViewModel(
        val id: String,
        val name: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0
    /**
     * упаковка данных
     */
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
    }

    // Equals and hashcode are used for comparator fit
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RepairTypeViewModel

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }
    /**
     * хэшкод
     */
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RepairTypeViewModel> = object : Parcelable.Creator<RepairTypeViewModel> {
            /**
             * извлечение данных
             */
            override fun createFromParcel(source: Parcel): RepairTypeViewModel = RepairTypeViewModel(source)
            override fun newArray(size: Int): Array<RepairTypeViewModel?> = arrayOfNulls(size)
        }
    }
}