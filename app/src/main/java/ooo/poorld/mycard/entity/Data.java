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
    @Convert(converter = DateTypeConverter.class, columnType = Integer.class)
    private Integer dataType;
    //备注
    private String note;
    private String filePath;

    
}
