package ru.digipeople.locotech.technologist.ui.activity.comment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Параметры для структуры коментария
 *
 * @author Sostavkin Grisha
 */
public class CommentParams implements Parcelable {
    /**
     * ID
     */
    private String id;
    /**
     * Текст комментария
     */
    private String text;
    /**
     * Конструктор
     */
    public CommentParams(String id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * Упаковка данных
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.text);
    }

    public CommentParams() {
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    protected CommentParams(Parcel in) {
        this.id = in.readString();
        this.text = in.readString();
    }

    public static final Creator<CommentParams> CREATOR = new Creator<CommentParams>() {
        /**
         * Извлечение данных
         */
        @Override
        public CommentParams createFromParcel(Parcel source) {
            return new CommentParams(source);
        }

        @Override
        public CommentParams[] newArray(int size) {
            return new CommentParams[size];
        }
    };
}
