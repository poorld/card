package ooo.poorld.mycard;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ooo.poorld.mycard.model.card.CardActivity;
import ooo.poorld.mycard.model.cert.CertificateActivity;
import ooo.poorld.mycard.model.data.DataMain;
import ooo.poorld.mycard.model.data.DataManageActivity;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.Permissions;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.rl_cert)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, CertificateActivity.class));
                    }
                });
        findViewById(R.id.rl_card)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, CardActivity.class));
                    }
                });
        findViewById(R.id.rl_data)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, DataMain.class));
                    }
                });
        findViewById(R.id.rl_myself)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get the note DAO
                        // DaoSession daoSession = ((App) getApplication()).getDaoSession();
                        // NoteDao noteDao = daoSession.getNoteDao();
                        // String tablename = noteDao.getTablename();
                        // Note note = new Note();
                        // note.setText("你好");
                        // note.setDate(new Date());
                        // noteDao.insert(note);
                        // Log.d("DaoExample", "Inserted new note, ID: " + note.getId());
                        startActivity(new Intent(MainActivity.this, MyselfActivity.class));
                    }
                });


        Permissions.verifyStoragePermissions(this);

    }

    private void createDir() {

        // /storage/emulated/0/mycard
        File root = ConstansUtil.getBaseDir();
        if (!root.exists()) {
            root.mkdir();
        }

        // /storage/emulated/0/mycard/data
        File data = new File(root, Constans.DATA_PATH_DATA);
        String dataPath = data.getPath();

        List<File> files = new ArrayList<>(Arrays.asList(
                // /storage/emulated/0/mycard/cert 证件
                new File(root, Constans.DATA_PATH_CERT),
                // /storage/emulated/0/mycard/card 卡片
                new File(root, Constans.DATA_PATH_CARD),
                // /storage/emulated/0/mycard/data 各模块
                // new File(root, Constans.DATA_PATH_DATA),
                // /storage/emulated/0/mycard/back 备份
                new File(root, Constans.DATA_PATH_BACKUP),
                // /storage/emulated/0/mycard/db 数据库备份
                new File(root, Constans.DATA_PATH_DB),
                // /storage/emulated/0/mycard/data/我的文档
                new File(dataPath, Constans.DATA_PATH_DATA_DOCUMENT),
                // /storage/emulated/0/mycard/data/我的图片
                new File(dataPath, Constans.DATA_PATH_DATA_IMAGE),
                // /storage/emulated/0/mycard/data/我的音乐
                new File(dataPath, Constans.DATA_PATH_DATA_MUSIC),
                // /storage/emulated/0/mycard/data/我的视频
                new File(dataPath, Constans.DATA_PATH_DATA_VIDEO))
        );

        files.add(data);

        for (File file : files) {
            if (!file.exists()) {
                file.mkdirs();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){//
            case Permissions.REQUEST_EXTERNAL_STORAGE://如果申请权限回调的参数
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"申请成功",Toast.LENGTH_SHORT).show();
                    createDir();
                } else {
                    Toast.makeText(this,"拒绝权限",Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }
}
