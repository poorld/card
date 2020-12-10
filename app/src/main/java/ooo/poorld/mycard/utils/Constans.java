package ooo.poorld.mycard.utils;

import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class Constans {

    public static final String TAG = "TEENYDA";

    //设置默认超时时间
    public static final int DEFAULT_TIME=10;


//    public final static  String BaseUrl = "http://120.78.186.81/api/";
//    public final static  String BaseUrl = "http://192.168.43.93:9000/api/app/";
    public final static  String BaseUrl = "http://192.168.1.66:9000/api/app/";

    public final static  String retrofit = "values/5";
    public final static  String retrofitList = "values";

    public final static String book = "book/book";

    /**
     * 拍照 相片名称
     */
    public static final String TAKE_PICTURE_PHOTO_NAME = "take_photo.jpg";

    /**
     * 相片存储路径:/storage/emulated/0/Pictures
     */
    public static final String PHOTO_STORAGE_PATH = "Pictures";

    /**
     * 从相册获取 相片名称photo album
     */
    public static final String PHOTO_ALBUM = "photo_album.jpg";

    /**
     * /storage/emulated/0/mycard/
     */
    public static final String BASE_PATH = "mycard";

    /**
     * 证件资源路径/data/data/appname/files/
     */
    public static final String PATH_FILES = "files";

    /**
     * 证件资源路径/storage/emulated/0/mycard/cert/
     */
    public static final String DATA_PATH_CERT = "cert";


    /**
     * 卡片资源路径/data/data/appname/file/card/
     */
    public static final String DATA_PATH_CARD = "card";

    /**
     * 资料资源路径/data/data/appname/file/data/
     */
    public static final String DATA_PATH_DATA = "data";
    public static final String DATA_PATH_DATA_DOCUMENT = "我的文档";
    public static final String DATA_PATH_DATA_IMAGE = "我的图片";
    public static final String DATA_PATH_DATA_MUSIC = "我的音乐";
    public static final String DATA_PATH_DATA_VIDEO = "我的视频";

    /**
     * 备份资源路径/data/data/appname/file/backup/
     */
    public static final String DATA_PATH_BACKUP = "backup";


    /**
     * 压缩密码
     */
    public static final String COMP_PASS = "9527";

    /**
     * 压缩方式
     */
    public static final CompressionMethod COMP_DEFLATE = CompressionMethod.DEFLATE;
    /**
     * 压缩级别
     */
    public static final CompressionLevel DEFLATE_LEVEL_NORMAL = CompressionLevel.NORMAL;
    /**
     * 加密方式 aes
     */
    public static final EncryptionMethod ENC_METHOD_STANDARD = EncryptionMethod.AES;


}
