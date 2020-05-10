package ru.digipeople.locotech.inspector.ui.view.signerview

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ru.digipeople.locotech.inspector.R
import ru.digipeople.locotech.inspector.model.Signer

/**
 * Класс подписант
 * @author Kashonkov Nikita
 */
class SignersView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    //region Views
    private val title: TextView
    private val addSigner: ImageView
    private val removeSigner: ImageView
    private val chipGroup: ChipGroup
    private val splashText: TextView
    //endregion
    //region Others
    var removeSignerClickListener: ((signer: Signer) -> Unit)? = null
    var removeAllSignersClickListener: (() -> Unit)? = null
    var addSignerClickListener: (() -> Unit)? = null
    //end region
    /**
     * инициализация
     */
    init {
        inflate(context, R.layout.signers_view, this)

        title = findViewById(R.id.title)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SignersView)
        title.text = typedArray.getString(R.styleable.SignersView_title)!!
        typedArray.recycle()

        addSigner = findViewById(R.id.add_signer)
        addSigner.setOnClickListener { addSignerClickListener?.invoke() }
        /**
         * контейнер для подписантов
         */
        chipGroup = findViewById(R.id.chip_group)

        removeSigner = findViewById(R.id.remove_signer)
        removeSigner.setOnClickListener {
            removeAllSignersClickListener?.invoke()
            chipGroup.removeAllViews()
            updateEmptyDataVisibility()
        }

        splashText = findViewById(R.id.hint)
        splashText.setOnClickListener { addSignerClickListener?.invoke() }

        updateEmptyDataVisibility()
    }
    /**
     * установить подписантов в чипгруп
     */
    fun setSigners(signers: Set<Signer>) {
        chipGroup.removeAllViews()
        signers.forEach { signer ->
            val chip = Chip(chipGroup.context)
            chip.text = signer.name
            chip.isClickable = true
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener {
                removeSignerClickListener?.invoke(signer)
                chipGroup.removeView(chip)
                updateEmptyDataVisibility()
            }
            chipGroup.addView(chip)
        }
        updateEmptyDataVisibility()
    }

    private fun updateEmptyDataVisibility() {
        if (chipGroup.childCount == 0) {
            removeSigner.isEnabled = false
            chipGroup.visibility = View.GONE
            splashText.visibility = View.VISIBLE
        } else {
            removeSigner.isEnabled = true
            chipGroup.visibility = View.VISIBLE
            splashText.visibility = View.GONE
        }
    }
}