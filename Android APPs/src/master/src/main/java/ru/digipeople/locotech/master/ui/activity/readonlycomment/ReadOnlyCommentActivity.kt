package ru.digipeople.locotech.master.ui.activity.readonlycomment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import android.widget.TextView
import ru.digipeople.locotech.master.R

/**
 * Комментарий только для чтения
 *
 * @author Sostavkin Grisha
 */
class ReadOnlyCommentActivity: Activity() {

    private lateinit var textView: TextView
    private lateinit var backBtn: AppCompatImageView
    private var text: String = ""
    /**
     * действия при создании активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Извлечение данных из интента
         */
        text = intent.getStringExtra(EXTRA_TEXT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_only_comment)

        textView = findViewById(R.id.activity_comment_edit_text)
        backBtn = findViewById(R.id.activity_comment_back_button)

        textView.text = text
        /**
         * обработка нажатия на кнопку назад
         */
        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {

        private const val EXTRA_TEXT = "EXTRA_TEXT"
        /**
         * интент
         */
        fun getCallingIntent(context: Context, text: String): Intent {
            val intent = Intent(context, ReadOnlyCommentActivity::class.java)
            intent.putExtra(EXTRA_TEXT,text)
            return intent
        }
    }
}