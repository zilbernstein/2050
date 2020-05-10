package ru.digipeople.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import ru.digipeople.ui.R

/**
 * Базовый класс для SortView
 *
 * @author Kashonkov Nikita
 */
abstract class BaseSortView : FrameLayout {
    //region View
    private var sortTitle: String
    var textView: TextView
    var startDrawable: Drawable? = null
    var view: View
    //endRegion
    //region Other
    var isChosen = false
    //endRegion
    /**
     * Конструктор
     */
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SortView)
        sortTitle = typedArray.getString(R.styleable.SortView_sortTitle)!!
        val startDrawableRes = typedArray.getResourceId(R.styleable.SortView_drawableStart, 0)
        if (startDrawableRes != 0)
            startDrawable = context.getDrawable(startDrawableRes)
        typedArray.recycle()

        val inflatedView = inflateView(LayoutInflater.from(context))
        view = inflatedView.findViewById(R.id.sort_underline) as View
        textView = inflatedView.findViewById(R.id.sort_title) as TextView
        textView.text = sortTitle
    }

    constructor(context: Context) : this(context, null)

    /**
     * Метод для инфлейтпа лэйаута наследников
     */
    abstract fun inflateView(inflater: LayoutInflater): View

    fun setSortedCount(count: Int) {
        var text = textView.text
        if (text.contains(Regex("\\(\\d*\\)"))) {
            text = text.replace(Regex("\\(\\d*\\)"), "($count)")
        } else {
            text = "$text ($count)"
        }
        textView.text = text

    }
    /**
     * Установка выделения выбранного элемента
     */
    fun setSelect(isSelected: Boolean) {
        isChosen = isSelected
        if (isSelected) {
            view.background = ContextCompat.getDrawable(context, R.drawable.red_line)
        } else {
            view.background = ContextCompat.getDrawable(context, R.drawable.gray_line)
        }
    }
    /**
     * Установка текста
     */
    fun setText(resId: Int) {
        textView.setText(resId)
    }

}