package ru.digipeople.photo.ui.activity.photogallery.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import ru.digipeople.photo.R
import ru.digipeople.utils.android.AndroidUtils

/**
 * Прегресс диалог для фотографий
 *
 * @author Kashonkov Nikita
 */
class ProgressDialog() : androidx.fragment.app.DialogFragment() {
    //region View
    private var progressBar: ProgressBar? = null
    private var progressText: TextView? = null
    //end Region
    //region Other
    private var maxProgress: Int = -1
    private var currentProgress: Int = -1
    //end Region

    /**
     * действия при создании окна
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_progress, container)

        progressBar = view.findViewById(R.id.progressBar)
        progressText = view.findViewById(R.id.progress)

        if (maxProgress != -1 && currentProgress != -1) {
//            val progress = (currentProgress.toFloat() / maxProgress.toFloat() * 100).toInt()
//            progressBar!!.progress = progress
            progressText!!.text = requireContext().getString(R.string.progress_dialog_progress, currentProgress, maxProgress)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = AndroidUtils.dpToPx((WIDTH + 2 * PADDING_HORIZONTAL).toFloat(), requireContext()).toInt()
        val height = AndroidUtils.dpToPx((HEIGHT + 2 * PADDING_VERTICAL).toFloat(), requireContext()).toInt()
        dialog!!.window!!.setLayout(width, height)
    }
    /**
     * установка прогресса
     */
    fun setProgress(currentProgress: Int, maxProgress: Int) {
        this.maxProgress = maxProgress
        this.currentProgress = currentProgress
//        if (progressBar != null) {
//            val progress = (currentProgress.toFloat() / maxProgress.toFloat() * 100).toInt()
//            progressBar!!.progress = progress
//        }
        if (progressText != null) {
            progressText!!.text = requireContext().getString(R.string.progress_dialog_progress, currentProgress, maxProgress)
        }
    }

    companion object {
        private const val PADDING_HORIZONTAL = 32
        private const val PADDING_VERTICAL = 16
        private const val WIDTH = 256
        private const val HEIGHT = 128
    }
}