package ru.digipeople.locotech.inspector.ui.dialog

import android.os.Parcel
import android.os.Parcelable
/**
 * аргументы диалога выбора типа ремонта
 */
class Arguments(val repairTypes: List<RepairTypeViewModel>, val selected: List<RepairTypeViewModel>) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(RepairTypeViewModel.CREATOR),
            source.createTypedArrayList(RepairTypeViewModel.CREATOR)
    )

    override fun describeContents() = 0
    /**
     * упаковка данных
     */
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(repairTypes)
        writeTypedList(selected)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Arguments> = object : Parcelable.Creator<Arguments> {
            /**
             * извлечение данных
             */
            override fun createFromParcel(source: Parcel): Arguments = Arguments(source)
            override fun newArray(size: Int): Array<Arguments?> = arrayOfNulls(size)
        }
    }
}