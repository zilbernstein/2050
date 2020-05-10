package ru.digipeople.locotech.master.ui.activity.workerspresence.adapter

import android.app.TimePickerDialog
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_workers_presence_brigade.view.*
import kotlinx.android.synthetic.main.item_workers_presence_worker.*
import ru.digipeople.locotech.master.R
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.BrigadePresenceItem
import ru.digipeople.locotech.master.ui.activity.workerspresence.adapter.item.WorkerPresenceItem
import ru.digipeople.ui.adapter.BaseRecyclerAdapter
import ru.digipeople.utils.input.Keyboard
import java.text.SimpleDateFormat
import java.util.*
/**
 * Адаптер явки сотрудников
 */
class WorkersPresenceAdapter : BaseRecyclerAdapter<RecyclerView.ViewHolder>() {
    private val defaultTimeZone = TimeZone.getTimeZone("UTC")
    private val dateFormatterHHMM = SimpleDateFormat("HH:mm", Locale.getDefault())
            .apply { timeZone = defaultTimeZone }
    private val dateFormatterH = SimpleDateFormat("H", Locale.getDefault())
            .apply { timeZone = defaultTimeZone }
    private val lengthFilter = InputFilter.LengthFilter(WORKER_TIME_FILTER_MAX_LENGTH)

    var items = AdapterData()
    var onEditBtnClickListener: ((WorkerPresenceItem) -> Unit)? = null
    var onSaveBtnClickListener: ((WorkerPresenceItem, Int) -> Unit)? = null

    // Editable item for managing state of item which is edited
    private var editableItem: WorkerPresenceItem? = null
    /**
     * Создание холдера в зависимости от типа элемента
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            /**
             * Бригада
             */
            VIEW_TYPE_BRIGADE_PRESENCE -> BrigadePresenceViewHolder(
                    layoutInflater.inflate(R.layout.item_workers_presence_brigade, parent, false)
            )/**
             * Исполнитель
             */
            VIEW_TYPE_WORKER_PRESENCE -> WorkerPresenceViewHolder(
                    layoutInflater.inflate(R.layout.item_workers_presence_worker, parent, false)
            )
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }
    /**
     * Получение типа элемента
     */
    override fun getItemViewType(position: Int): Int {
        return when {
            items.isBrigadePresence(position) -> VIEW_TYPE_BRIGADE_PRESENCE
            items.isWorkerPresence(position) -> VIEW_TYPE_WORKER_PRESENCE
            else -> throw IllegalArgumentException("Unsupported view type at position $position")
        }
    }
    /**
     * определение холдера
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            items.isBrigadePresence(position) -> (holder as BrigadePresenceViewHolder)
                    .bind(items.getBrigadePresence(position))
            items.isWorkerPresence(position) -> {
                val item = items.getWorkerPresence(position)
                val isEditable = item.id == editableItem?.id ?: false
                (holder as WorkerPresenceViewHolder)
                        .also { it.applyEditableState(isEditable) }
                        .bind(if (isEditable) editableItem!! else item)
            }
        }
    }
    /**
     * Подсчет сила элементов
     */
    override fun getItemCount(): Int = items.size

    fun setEditableItem(item: WorkerPresenceItem?) {
        if (editableItem == item) return
        editableItem = item
        notifyDataSetChanged()
    }

    /**
     * Shows time picker dialog
     */
    private fun showTimePickerDialog(initDate: Date, onTimePicked: (Date) -> Unit) {
        val calendar = Calendar.getInstance(Locale.getDefault())
                .apply {
                    timeZone = defaultTimeZone
                    time = initDate
                }
        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            onTimePicked.invoke(calendar.time)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                .show()
    }
    /**
     * очестка фокуса
     */
    private fun clearFocus(view: View) {
        Keyboard.hide(view)
        view.clearFocus()
    }

    //region ViewHolders
    /**
     * Холдер бригады
     */
    inner class BrigadePresenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(item: BrigadePresenceItem) {
            itemView.brigade_name.text = item.name
        }
    }

    /**
     * Холдер исполнителя
     */
    inner class WorkerPresenceViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        //Flag is used due to performing of unnecessary mutations while listening to view's changes
        private var isEditable = false
        /**
         * Привязка данных к шаблону
         */
        init {
            /**
             * обработка нажатия на начало смены
             */
            worker_time_begin.setOnClickListener {
                val item = items.getWorkerPresence(adapterPosition)
                if (isEditable)
                    clearFocus(itemView)
                item.timeBegin?.let { timeBegin ->
                    showTimePickerDialog(timeBegin) {
                        worker_time_begin.text = dateFormatterHHMM.format(it)
                        editableItem = editableItem?.copy(timeBegin = it)
                    }
                }
            }
            /**
             * Обработка нажатия на конец смены
             */
            worker_time_finish.setOnClickListener {
                val item = items.getWorkerPresence(adapterPosition)
                if (isEditable)
                    clearFocus(itemView)
                item.timeFinish?.let { timeFinish ->
                    showTimePickerDialog(timeFinish) {
                        worker_time_finish.text = dateFormatterHHMM.format(it)
                        editableItem = editableItem?.copy(timeFinish = it)
                    }
                }
            }
            /**
             * Обработка изменнеия часов работы
             */
            worker_time_work_base.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable) {
                    if (isEditable && editable.isNotEmpty())
                        editableItem = editableItem?.copy(workTime = dateFormatterH.parse(editable.toString()))
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            /**
             * Обработка изменения ночных часов
             */
            worker_time_night.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable) {
                    if (isEditable && editable.isNotEmpty())
                        editableItem = editableItem?.copy(timeNight = dateFormatterH.parse(editable.toString()))
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            /**
             * Обработка изменения флага явка
             */
            worker_presence_flag.setOnCheckedChangeListener { _, isChecked ->
                if (isEditable)
                    editableItem = editableItem?.copy(presence = isChecked)
            }
            /**
             * Обработка изменения флага ночная смена
             */
            worker_night_shift_flag.setOnCheckedChangeListener { _, isChecked ->
                if (isEditable)
                    editableItem = editableItem?.copy(night = isChecked)
            }
            worker_btn_edit.setOnClickListener {
                val item = items.getWorkerPresence(adapterPosition)
                onEditBtnClickListener?.invoke(item)
            }
            worker_btn_save.setOnClickListener { onSaveBtnClickListener?.invoke(editableItem!!, adapterPosition) }
        }
        /**
         * Привязка данных к шаблону
         */
        internal fun bind(item: WorkerPresenceItem) {
            worker_name.text = item.name
            worker_number.text = item.number
            worker_presence_flag.isChecked = item.presence
            worker_night_shift_flag.isChecked = item.night
            worker_time_in.text = item.timeIn?.let { dateFormatterHHMM.format(it) }
                    ?: NO_DATA_PLACEHOLDER
            worker_time_out.text = item.timeOut?.let { dateFormatterHHMM.format(it) }
                    ?: NO_DATA_PLACEHOLDER
            worker_time_begin.text = item.timeBegin?.let { dateFormatterHHMM.format(it) }
                    ?: NO_DATA_PLACEHOLDER
            worker_time_finish.text = item.timeFinish?.let { dateFormatterHHMM.format(it) }
                    ?: NO_DATA_PLACEHOLDER
            worker_time_work_base.setText(item.workTime?.let { dateFormatterH.format(it) }
                    ?: NO_DATA_PLACEHOLDER)
            worker_time_work_base.filters = arrayOf(lengthFilter)
            worker_time_night.setText(item.timeNight?.let { dateFormatterH.format(it) }
                    ?: NO_DATA_PLACEHOLDER)
            worker_time_night.filters = arrayOf(lengthFilter)
        }
        /**
         * Установка редактируемых полей
         */
        internal fun applyEditableState(isEditable: Boolean) {
            this.isEditable = isEditable
            worker_btn_save.visibility = if (isEditable) View.VISIBLE else View.GONE
            worker_btn_edit.visibility = if (isEditable) View.GONE else View.VISIBLE
            worker_time_begin.isEnabled = isEditable
            worker_time_finish.isEnabled = isEditable
            worker_time_work_base.isEnabled = isEditable
            worker_time_work_base.isFocusableInTouchMode = isEditable
            worker_time_night.isEnabled = isEditable
            worker_time_night.isFocusableInTouchMode = isEditable
            worker_night_shift_flag.isClickable = isEditable
            worker_presence_flag.isClickable = isEditable
            // Setting up overlay visibility
            if (editableItem == null) semitransparent_overlay.visibility = View.GONE
            else semitransparent_overlay.visibility = if (isEditable) View.GONE else View.VISIBLE
        }
    }
    //endregion

    companion object {
        private const val WORKER_TIME_FILTER_MAX_LENGTH = 2
        private const val NO_DATA_PLACEHOLDER = "-"

        const val VIEW_TYPE_BRIGADE_PRESENCE = 0
        const val VIEW_TYPE_WORKER_PRESENCE = 1
    }
}