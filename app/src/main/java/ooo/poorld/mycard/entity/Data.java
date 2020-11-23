package ooo.poorld.mycard.entity;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import ooo.poorld.mycard.common.DataType;
import ooo.poorld.mycard.common.DateTypeConverter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: teenyda
 * date: 2020/11/21
 * description: 证件
 */
@Entity
public class Data {
    @Id(autoincrement = true)
    private Long dataID;
    private String dataName;
    @Convert(converter = DateTypeConverter.class, columnType = String.class)
    private DataType dataType;
    //备注
    private String note;
    private String filePath;
    @Generated(hash = 1933845477)
    public Data(Long dataID, String dataName, DataType dataType, String note,
            String filePath) {
        this.dataID = dataID;
        this.dataName = dataName;
        this.dataType = dataType;
        this.note = note;
        this.filePath = filePath;
    }
    @Generated(hash = 2135787902)
    public Data() {
    }
    public Long getDataID() {
        return this.dataID;
    }
    public void setDataID(Long dataID) {
        this.dataID = dataID;
    }
    public String getDataName() {
        return this.dataName;
    }
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    public DataType getDataType() {
        return this.dataType;
    }
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
}
