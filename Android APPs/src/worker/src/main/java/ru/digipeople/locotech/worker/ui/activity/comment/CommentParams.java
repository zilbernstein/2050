package ru.digipeople.locotech.worker.ui.activity.comment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Параметры для структуры коментария
 *
 * @author Kashonkov Nikita
 */
public class CommentParams implements Parcelable {
    private String workId;
    private String text;

    public CommentParams(String workId, String text) {
        this.workId = workId;
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
        dest.writeString(this.workId);
        dest.writeString(this.text);
    }

    public CommentParams() {
    }
    /**
     * Получение ид работы
     */
    public String getWorkId() {
        return workId;
    }
    /**
     * Получение текста
     */
    public String getText() {
        return text;
    }

    protected CommentParams(Parcel in) {
        this.workId = in.readString();
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
