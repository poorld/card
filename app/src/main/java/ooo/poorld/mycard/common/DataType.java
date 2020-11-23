package ooo.poorld.mycard.common;

/**
 * author: teenyda
 * date: 2020/11/21
 * description:
 */
public enum  DataType {
    TYPE_JPG("图片", "jpg"),
    TYPE_PNG("图片", "png"),
    TYPE_MP3("音乐", "mp3"),
    TYPE_MP4("视频", "mp4"),
    TYPE_TXT("文本", "txt"),
    TYPE_WORD("word文档", "word"),
    TYPE_PDF("pdf文档","pdf"),
    TYPE_EXCEL("excel文档", "excel")
    ;
    private String name;
    private String type;

    DataType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
