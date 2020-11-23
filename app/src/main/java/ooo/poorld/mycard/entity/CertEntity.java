package ooo.poorld.mycard.entity;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class CertEntity implements Serializable {

    private String title;
    private String content;
    private List<File> mFiles;
    // private List<LocalMedia> mLocalMedia;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<File> getFiles() {
        return mFiles;
    }

    public void setFiles(List<File> files) {
        mFiles = files;
    }

    // public List<LocalMedia> getLocalMedia() {
    //     return mLocalMedia;
    // }

    // public void setLocalMedia(List<LocalMedia> localMedia) {
    //     mLocalMedia = localMedia;
    // }
}
