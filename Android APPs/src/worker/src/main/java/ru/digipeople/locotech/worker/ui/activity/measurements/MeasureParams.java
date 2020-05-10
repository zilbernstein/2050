package ru.digipeople.locotech.worker.ui.activity.measurements;

import android.os.Parcel;
import android.os.Parcelable;

import ru.digipeople.locotech.core.data.model.MeasureValueType;
import ru.digipeople.locotech.core.data.model.Stage;

/**
 * Параметры для замеров
 */
public class MeasureParams implements Parcelable {
    private String measurementId;
    private String measurementName;
    private String characteristicId;
    private String characteristicName;
    private String measurementNorm;
    private String worker;
    private String measurementValue;
    private MeasureValueType valueType;
    private Boolean valueCompliance;
    private String measurementDate;
    private String workId;
    private Stage measurementStage;
    private String comment;
    private boolean hardwareMeasurement;

    public MeasureParams(String measurementId,
                         String measurementName,
                         String characteristicId,
                         String characteristicName,
                         String measurementNorm,
                         String measurementValue,
                         String worker,
                         MeasureValueType valueType,
                         Boolean valueCompliance,
                         String measurementDate,
                         String workId,
                         Stage measurementStage,
                         String comment,
                         boolean hardwareMeasurement) {
        this.measurementId = measurementId;
        this.measurementName = measurementName;
        this.characteristicId = characteristicId;
        this.characteristicName = characteristicName;
        this.measurementNorm = measurementNorm;
        this.worker = worker;
        this.measurementValue = measurementValue;
        this.valueType = valueType;
        this.valueCompliance = valueCompliance;
        this.measurementDate = measurementDate;
        this.workId = workId;
        this.measurementStage = measurementStage;
        this.comment = comment;
        this.hardwareMeasurement = hardwareMeasurement;
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
        dest.writeString(this.measurementId);
        dest.writeString(this.measurementName);
        dest.writeString(this.characteristicId);
        dest.writeString(this.characteristicName);
        dest.writeString(this.measurementNorm);
        dest.writeString(this.measurementValue);
        dest.writeString(this.worker);
        dest.writeInt(valueType.ordinal());
        dest.writeString(this.valueCompliance.toString());
        dest.writeString(this.measurementDate);
        dest.writeString(this.workId);
        dest.writeInt(measurementStage.ordinal());
        dest.writeString(this.comment);
        dest.writeInt(hardwareMeasurement ? 1 : 0); // запись булевого параметра как число
    }

    protected MeasureParams(Parcel in) {
        this.measurementId = in.readString();
        this.measurementName = in.readString();
        this.characteristicId = in.readString();
        this.characteristicName = in.readString();
        this.measurementNorm = in.readString();
        this.measurementValue = in.readString();
        this.worker = in.readString();
        this.valueType = MeasureValueType.values()[in.readInt()];
        this.valueCompliance = Boolean.valueOf(in.readString());
        this.measurementDate = in.readString();
        this.workId = in.readString();
        this.measurementStage = Stage.values()[in.readInt()];
        this.comment = in.readString();
        this.hardwareMeasurement = in.readInt() == 1; // передача булевого параметра через число
    }

    public static final Creator<MeasureParams> CREATOR = new Creator<MeasureParams>() {
        /**
         * Извлечение данных
         */
        @Override
        public MeasureParams createFromParcel(Parcel source) {
            return new MeasureParams(source);
        }

        @Override
        public MeasureParams[] newArray(int size) {
            return new MeasureParams[size];
        }
    };

    public String getMeasurementId() {
        return measurementId;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public String getCharacteristicId() {
        return characteristicId;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    public String getMeasurementNorm() {
        return measurementNorm;
    }

    public String getMeasurementValue() {
        return measurementValue;
    }

    public String getWorker() {
        return worker;
    }

    public MeasureValueType getValueType() {
        return valueType;
    }

    public Boolean getValueCompliance() {
        return valueCompliance;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public String getWorkId() {
        return workId;
    }

    public Stage getMeasurementStage() {
        return measurementStage;
    }

    public String getComment() {
        return comment;
    }

    public boolean isHardwareMeasurement() {
        return hardwareMeasurement;
    }
}
