package ru.digipeople.ui.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.digipeople.ui.R

/**
 * Класс для работы с диалогом загрузки
 *
 * @author Kashonkov Nikita
 */
class LoadingDialog : DialogFragment() {
    /**
     * Действия при создании диалога
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }
    /**
     * Действия при отрисовке диалога
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_loading, container)
        return view
    }


}