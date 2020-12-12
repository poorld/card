package ooo.poorld.mycard;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import androidx.multidex.MultiDex;
import ooo.poorld.mycard.entity.DaoMaster;
import ooo.poorld.mycard.entity.DaoSession;
import ooo.poorld.mycard.utils.Constans;

/**
 * author: teenyda
 * date: 2020/11/21
 * description:
 */
public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        // regular SQLite database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, Constans.DB_NAME);
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = new DaoMaster(db).newSession();

        // 初始化MultiDex
        MultiDex.install(this);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
