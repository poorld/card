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
    /**
     * 1我的文档
     * 2我的图片
     * 3我的音乐
     * 4我的视频
     */
    private Integer type;
    // @Convert(converter = DateTypeConverter.class, columnType = Integer.class)
    private Integer dataType;
    //备注
    private String note;
    private String filePath;
    @Generated(hash = 1538806525)
    public Data(Long dataID, String dataName, Integer type, Integer dataType,
            String note, String filePath) {
        this.dataID = dataID;
        this.dataName = dataName;
        this.type = type;
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
    public Integer getType() {
        return this.type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getDataType() {
        return this.dataType;
    }
    public void setDataType(Integer dataType) {
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
