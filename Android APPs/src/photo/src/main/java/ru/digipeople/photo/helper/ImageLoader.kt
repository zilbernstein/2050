package ru.digipeople.photo.helper

import android.graphics.drawable.Drawable
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaderFactory
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ru.digipeople.locotech.core.BuildConfig
import ru.digipeople.logger.LoggerFactory
import ru.digipeople.utils.HttpUtils


/**
 * Загрузчик изображений
 *
 * @author Kashonkov Nikita
 */
class ImageLoader(private val imageView: ImageView,
                  @DrawableRes private val placeholder: Int,
                  private val imageUrl: String) {
    /**
     * Подключение логгирования
     */
    private val logger = LoggerFactory.getLogger(ImageLoader::class)

    private val requestOptions
        get() = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.DATA)
                .placeholder(placeholder)
                .override(imageView.measuredWidth, imageView.measuredHeight)
    /**
     * Загрузка
     */
    fun load() {
        onImageUriChanged()
    }
    /**
     * Действия при изхменении Uri
     */
    private fun onImageUriChanged() {
        if (imageView.measuredWidth == 0 || imageView.measuredHeight == 0) {
            imageView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        } else {
            onImageViewSizeDetermined()
        }
    }
    /**
     * При изменении размера
     */
    private fun onImageViewSizeDetermined() {
        logger.trace("onImageViewSizeDetermined $imageUrl")

        if (imageUrl.contains(CONTENT_FLAG)) {
            Glide.with(imageView)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .listener(listener)
                    .into(imageView)
        } else {
            val glideUrl = GlideUrl(imageUrl,
                    LazyHeaders.Builder()
                            .addHeader("Authorization", BasicAuthorization(BuildConfig.USERNAME, BuildConfig.PASSWORD))
                            .build())

            Glide.with(imageView)
                    .load(glideUrl)
                    .apply(requestOptions)
                    .listener(listener)
                    .into(imageView)
        }
    }

    private val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (imageView.measuredWidth > 0 || imageView.measuredHeight > 0) {
                onImageViewSizeDetermined()
                imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    }

    private val listener = object : RequestListener<Drawable> {
        /**
         * Загрузка не удалась
         */
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            logger.error("onLoadFailed", e)
            return false
        }
        /**
         * ресукрсы готовы
         */
        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            logger.trace("onResourceReady $imageUrl")
            return false
        }

    }

    class BasicAuthorization constructor(val username: String, val password: String) : LazyHeaderFactory {
        override fun buildHeader(): String? {
            return HttpUtils.buildBasicAuthHeader(username, password)
        }
    }

    companion object {
        private const val CONTENT_FLAG = "content://"
    }
}