package ooo.poorld.mycard.entity;

/**
 * @program: springboottemplate
 * @author: Teenyda
 * @create: 2019-08-24 09:32
 * @description: 文件上传成功响应实体类
 **/
public class FileUploadResponse {

    private String fileName;
    private String fileDownloadUrl;
    private String fileType;
    private long size;

    public FileUploadResponse(String fileName, String fileDownloadUrl, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUrl = fileDownloadUrl;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
