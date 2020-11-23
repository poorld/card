package ooo.poorld.mycard.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.UUID;

/**
 * author: teenyda
 * date: 2020/9/8
 * description:
 */
public class ConstansUtil {

    /**
     * 拍照存储路径
     * /storage/emulated/0/Pictures/take_photo.jpg
     * @return
     */
    public static String takePictureFilePath() {
        return Environment.getExternalStorageDirectory() +
                File.separator +
                Constans.PHOTO_STORAGE_PATH +
                File.separator +
                Constans.TAKE_PICTURE_PHOTO_NAME;
    }

    /**
     * 相册获取的图片 存储路径
     * /storage/emulated/0/Pictures/photo_album.jpg
     * @return
     */
    public static String getPhotoAlbumPath() {
        return Environment.getExternalStorageDirectory() +
                File.separator +
                Constans.PHOTO_STORAGE_PATH +
                File.separator +
                Constans.PHOTO_ALBUM;
    }

    public static String getStoragePath() {
        return Environment.getExternalStorageDirectory() +
                File.separator +
                Constans.PHOTO_STORAGE_PATH +
                File.separator;
    }

    public static File getExternalFilesDir(Context context, String path) {
        return context.getExternalFilesDir(path);
    }

    public static String getExternalFilesPath(Context context, String path, String fileName) {
        return getExternalFilesDir(context, path + File.separator + fileName).getAbsolutePath();
    }
    public static File getDataDir(Context context, String path) {
        return context.getDir(path, Context.MODE_PRIVATE);
    }

    public static File getStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
           return Environment.getExternalStorageDirectory();
        }

        return null;
    }

    public static File getBaseDir() {
        return new File(getStorage(), Constans.BASE_PATH);
    }

    public static File getStorageDir(String dirName) {
        return new File(getBaseDir(), dirName);
    }

    public static long getUUIDNumber(){
        Integer orderId=UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId; //String.hashCode() 值会为空
        return orderId;
    }

    /**
     * M转Byte
     * 1024Byte(字节)=1KB
     * 1024KB=1MB
     * 1024MB=1GB
     * @param M
     * @return
     */
    public static long getByte(int M){
        return M * 1024 * 1024;
    }

    public static long getKilobyte(int M){
        return M * 1024;
    }


}
