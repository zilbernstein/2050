package ru.digipeople.telephonebook.ui.activity.telephonedirectory.adapter

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.volcaniccoder.volxfastscroll.IVolxAdapter
import ru.digipeople.telephonebook.R
import ru.digipeople.telephonebook.model.Contact
import javax.inject.Inject

/**
 * Адаптер для телефонного справочника
 *
 * @author Sostavkin Grisha
 */
class TelephoneDirectoryAdapter @Inject constructor(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<TelephoneDirectoryAdapter.TelephoneDirectoryViewHolder>(), IVolxAdapter<Contact> {

    lateinit var dataSet: List<Contact>

    var itemClickListener: ((contact: Contact) -> Unit)? = null
    /**
     * Холдер телефонной записи
     */
    class TelephoneDirectoryViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val contactName: TextView
        val contactProfession: TextView
        val contactTel: TextView
        val constraint: ConstraintLayout
        /**
         * инициализация полей
         */
        init {
            contactName = view.findViewById(R.id.name)
            contactProfession = view.findViewById(R.id.profession)
            contactTel = view.findViewById(R.id.telephone_number)
            constraint = view.findViewById(R.id.item_directory_layout)
        }
    }
    /**
     * создание холдреа
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TelephoneDirectoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_telephone_directory, parent, false)
        return TelephoneDirectoryViewHolder(view)
    }
    /**
     * установка данных
     */
    fun setData(list: List<Contact>) {
        val diffUtilCallback = DiffUtilCallback(dataSet, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.dataSet = list
        diffResult.dispatchUpdatesTo(this)
    }
    /**
     * определение количесвта элементов
     */
    override fun getItemCount(): Int {
        return dataSet.size
    }
    /**
     * привязка данных к шаблону
     */
    override fun onBindViewHolder(p0: TelephoneDirectoryViewHolder, p1: Int) {

        val contact = dataSet.get(p1)
        val sb = StringBuilder()

        for (name in contact.name) {
            sb.append(name)
        }

        p0.contactName.text = dataSet.get(p1).name
        p0.contactTel.text = dataSet.get(p1).phoneNumber
        p0.contactProfession.text = dataSet.get(p1).job

        p0.constraint.setOnClickListener {
            itemClickListener?.invoke(contact)
        }
    }

    override fun getList(): List<Contact> {
        return dataSet
    }
}