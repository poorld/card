package ooo.poorld.mycard.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.TimeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * author: https://blog.csdn.net/qq_43184922/article/details/105884824
 * date: 2020/12/11
 * description: https://blog.csdn.net/qq_43184922/article/details/105884824
 * 备份：将3个数据库文件复制至外部存储
 * 还原：从外部存储复制覆盖掉原数据库文件
 */
public class BackupTask {
    public static final String COMMAND_BACKUP = "backupDatabase";
    private static final String COMMAND_RESTORE = "restoreDatabase";
    private final static String EXTERNAL_STORAGE_FOLDER = "Never Forget";
    private final static String EXTERNAL_STORAGE_BACKUP_DIR = "Backup";
    public String backup_version;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    public BackupTask(Context context) {
        this.mContext = context;
    }

    private static File getDBBackupDir() {//    /sdcard/Never Forget/
        File baseDir = ConstansUtil.getBaseDir();
        File dir = new File(baseDir, Constans.DATA_PATH_DB);
        return dir;
    }

    public String doInBackground(String command) {
        File dbFile = mContext.getDatabasePath("event_database");// 默认路径是 /data/data/(包名)/databases/*
        File dbFile_shm = mContext.getDatabasePath("event_database-shm");// 默认路径是 /data/data/(包名)/databases/*
        File dbFile_wal = mContext.getDatabasePath("event_database-wal");// 默认路径是 /data/data/(包名)/databases/*
        File exportDir = getDBBackupDir();
        if (!exportDir.exists()){
            exportDir.mkdirs();
        }
        File backup = new File(exportDir, dbFile.getName());//备份文件与原数据库文件名一致
        File backup_shm = new File(exportDir, dbFile_shm.getName());//备份文件与原数据库文件名一致
        File backup_wal = new File(exportDir, dbFile_wal.getName());//备份文件与原数据库文件名一致
        if (command.equals(COMMAND_BACKUP)) {
            try {
                backup.createNewFile();
                backup_shm.createNewFile();
                backup_wal.createNewFile();
                fileCopy(dbFile, backup);//数据库文件拷贝至备份文件
                fileCopy(dbFile_shm, backup_shm);//数据库文件拷贝至备份文件
                fileCopy(dbFile_wal, backup_wal);//数据库文件拷贝至备份文件

                backup_version = Tools.getTimeString("yyyy.MM.dd_HH:mm:ss");
                //backup.setLastModified(MyTimeUtils.getTimeLong());
                Log.d("myLog", "backup ok! 备份文件名："+backup.getName()+"\t"+backup_version);
                return backup_version;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("myLog", "backup fail! 备份文件名："+backup.getName());
                return null;
            }
        } else if (command.equals(COMMAND_RESTORE)) {
            try {
                fileCopy(backup, dbFile);//备份文件拷贝至数据库文件
                fileCopy(backup_shm, dbFile_shm);//备份文件拷贝至数据库文件
                fileCopy(backup_wal, dbFile_wal);//备份文件拷贝至数据库文件
                backup_version = Tools.getTimeString(backup.lastModified(),"yyyy.MM.dd_HH:mm:ss");
                Log.d("myLog", "restore success! 数据库文件名："+dbFile.getName()+"\t"+backup_version);
                return backup_version;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("myLog", "restore fail! 数据库文件名："+dbFile.getName());
                return null;
            }
        } else {
            return null;
        }
    }

    private void fileCopy(File dbFile, File backup) throws IOException {
        FileChannel inChannel = new FileInputStream(dbFile).getChannel();
        FileChannel outChannel = new FileOutputStream(backup).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
}
