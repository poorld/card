package ooo.poorld.mycard.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author: teenyda
 * date: 2020/11/21
 * description:
 */
@Entity
public class CardImage {
    @Id(autoincrement = true)
    private Long imageID;

    private long cardID;

    private String fileName;

    private String filePath;

    private long fileSize;

    @Generated(hash = 1107621120)
    public CardImage(Long imageID, long cardID, String fileName, String filePath,
            long fileSize) {
        this.imageID = imageID;
        this.cardID = cardID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    @Generated(hash = 845088074)
    public CardImage() {
    }

    public Long getImageID() {
        return this.imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }

    public long getCardID() {
        return this.cardID;
    }

    public void setCardID(long cardID) {
        this.cardID = cardID;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
