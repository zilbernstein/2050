package ru.digipeople.ui.view

import android.inputmethodservice.Keyboard
import android.os.Handler
import androidx.appcompat.widget.SearchView

/**
 * Слушатель для отложенного ввода
 *
 * @author Kashonkov Nikita
 */
class DebounceQueryTextListener(private val debounce: Long,
                                private val textChangeListener: ((text: String) -> Unit)?,
                                private val querySubmitListener: ((text: String) -> Unit)? = null)
    : SearchView.OnQueryTextListener {
    /**
     * [Handler] для отложенного выполнения кода
     */
    private val handler: Handler = Handler()

    /**
     * Отложенная операция
     */
    private var delayedCallback: Runnable? = null

    override fun onQueryTextSubmit(query: String): Boolean {
        querySubmitListener?.invoke(query)
        return true
    }
    /**
     * Обработка измения строки запроса
     */
    override fun onQueryTextChange(newText: String): Boolean {
        if (delayedCallback != null) {
            handler.removeCallbacks(delayedCallback)
        }
        delayedCallback = Runnable {
            textChangeListener?.invoke(newText)
            delayedCallback = null
        }
        handler.postDelayed(delayedCallback, debounce)
        return true
    }
}