package ooo.poorld.mycard.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;

/**
 * author: teenyda
 * date: 2020/11/21
 * description: 证件
 */
@Entity
public class Certificate {
    @Id(autoincrement = true)
    private Long certificateID;
    private String certificateName;
    private String certificateNumber;
    //持卡人
    private String certificateHolder;
    //备注
    private String note;
    //有效期
    private Date certificateDate;

    @ToMany(referencedJoinProperty = "cardID")
    private List<CardImage> cardImages;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1076210078)
    private transient CertificateDao myDao;

    @Generated(hash = 1569513019)
    public Certificate(Long certificateID, String certificateName,
            String certificateNumber, String certificateHolder, String note,
            Date certificateDate) {
        this.certificateID = certificateID;
        this.certificateName = certificateName;
        this.certificateNumber = certificateNumber;
        this.certificateHolder = certificateHolder;
        this.note = note;
        this.certificateDate = certificateDate;
    }
    @Generated(hash = 427239225)
    public Certificate() {
    }
    public Long getCertificateID() {
        return this.certificateID;
    }
    public void setCertificateID(Long certificateID) {
        this.certificateID = certificateID;
    }
    public String getCertificateName() {
        return this.certificateName;
    }
    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }
    public String getCertificateNumber() {
        return this.certificateNumber;
    }
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
    public String getCertificateHolder() {
        return this.certificateHolder;
    }
    public void setCertificateHolder(String certificateHolder) {
        this.certificateHolder = certificateHolder;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public Date getCertificateDate() {
        return this.certificateDate;
    }
    public void setCertificateDate(Date certificateDate) {
        this.certificateDate = certificateDate;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 544385973)
    public List<CardImage> getCardImages() {
        if (cardImages == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CardImageDao targetDao = daoSession.getCardImageDao();
            List<CardImage> cardImagesNew = targetDao
                    ._queryCertificate_CardImages(certificateID);
            synchronized (this) {
                if (cardImages == null) {
                    cardImages = cardImagesNew;
                }
            }
        }
        return cardImages;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 210905773)
    public synchronized void resetCardImages() {
        cardImages = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 410358673)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCertificateDao() : null;
    }
    
}
