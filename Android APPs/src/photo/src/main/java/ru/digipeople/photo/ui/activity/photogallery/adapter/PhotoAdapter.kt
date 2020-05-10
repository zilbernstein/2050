package ru.digipeople.photo.ui.activity.photogallery.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import ru.digipeople.photo.R
import ru.digipeople.photo.helper.ImageLoader
import ru.digipeople.photo.model.Photo
import javax.inject.Inject

/**
 * Фото адаптер
 *
 * @author Kashonkov Nikita
 */
class PhotoAdapter @Inject constructor(val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    // region Other
    var onDeleteClickListener: ((photo: Photo) -> Unit)? = null
    var onPhotoClickListener: ((photo: Photo) -> Unit)? = null
    var dataList: List<Photo> = emptyList()
    var deleteButtonVisibility = View.VISIBLE
    var selectedPosition = -1
    //endRegion
/*
     * Холдер фотографии
     */
    inner class PhotoViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val photoImage: ImageView
        val busket: ImageView

        init {
            photoImage = view.findViewById(R.id.item_photo_image)
            busket = view.findViewById(R.id.item_photo_basket)
            /*
            * обработка нажатия на элемент
            */
            busket.setOnClickListener {
                if (adapterPosition == selectedPosition) {
                    changeSelectedPosition(dataList.lastIndex)
                }
                onDeleteClickListener?.invoke(dataList.get(adapterPosition))
            }
        }
            /*
             * Привязка данных к шаблону
             */
        fun bind() {
            val photo = dataList.get(adapterPosition)
                /*
                 * Загрузка фото
                 */
            ImageLoader(photoImage, R.drawable.ic_icon_photo_splash, photo.url).load()
            setSelection()
            photoImage.setOnClickListener {
                changeSelectedPosition(adapterPosition)
                onPhotoClickListener?.invoke(photo)
            }
            busket.visibility = deleteButtonVisibility
        }
        /*
                     * установить выделение
                     */
        fun setSelection() {
            if (adapterPosition == selectedPosition) {
                photoImage.background = ContextCompat.getDrawable(context, R.drawable.red_frame)
            } else {
                photoImage.background = ContextCompat.getDrawable(context, R.color.appTransparent)
            }
        }
    }

    /*
     * Привязка данных к шаблону
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_photo, p0, false)
        return PhotoViewHolder(view)
    }
        /*
         * определение количества элементов
         */
    override fun getItemCount(): Int {
        return dataList.size
    }
        /*
         * Установка данных
         */
    fun setData(list: List<Photo>) {
        val diffUtilCallback = DiffUtilCallback(dataList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        this.dataList = list

        if (selectedPosition != -1) {
            changeSelectedPosition(dataList.lastIndex)
        } else {
            selectedPosition = dataList.lastIndex
        }

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(p0: PhotoViewHolder, p1: Int) {
        p0.bind()
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            holder.bind()
        } else if (payloads.first() == SELECTION_CHANGE_PAYLOAD) {
            holder.setSelection()
        }
    }
    /*
             * Изменение выбранной фотографии
             */
    private fun changeSelectedPosition(newPosition: Int) {
        val oldPosition = selectedPosition
        selectedPosition = newPosition
        notifyItemChanged(oldPosition, SELECTION_CHANGE_PAYLOAD)
        notifyItemChanged(selectedPosition, SELECTION_CHANGE_PAYLOAD)
    }

    companion object {
        private val SELECTION_CHANGE_PAYLOAD = Object()

    }
}
