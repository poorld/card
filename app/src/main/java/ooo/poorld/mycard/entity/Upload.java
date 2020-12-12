package ooo.poorld.mycard.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (Upload)实体类
 *
 * @author makejava
 * @since 2020-12-12 14:13:17
 */
public class Upload implements Serializable {
    private static final long serialVersionUID = -16719153159237629L;

    private Integer id;

    private String fileName;

    private String filePath;

    private String uploadDate;
    private long fileSize;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

}