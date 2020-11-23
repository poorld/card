package ooo.poorld.mycard;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import ooo.poorld.mycard.model.card.CardActivity;
import ooo.poorld.mycard.model.cert.CertificateActivity;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.Permissions;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
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
                        startActivity(new Intent(MainActivity.this, DataActivity.class));
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
        // File certFile = ConstansUtil.getExternalFilesDir(this, Constans.DATA_PATH_CERT);
        // File cardFile = ConstansUtil.getExternalFilesDir(this, Constans.DATA_PATH_CARD);
        // File dataFile = ConstansUtil.getExternalFilesDir(this, Constans.DATA_PATH_DATA);
        // File backupFile = ConstansUtil.getExternalFilesDir(this, Constans.DATA_PATH_BACKUP);
        //
        // List<File> files = new ArrayList<>();
        // files.add(cardFile);
        // files.add(cardFile);
        // files.add(dataFile);
        // files.add(backupFile);

        // File dataDir = ConstansUtil.getExternalFilesDir(this, Constans.DATA_PATH_CERT);
        // dataDir.mkdirs();
        // ConstansUtil.getDataDir(this,"/files/" + Constans.DATA_PATH_CARD);
        // ConstansUtil.getDataDir(this,"/files/" + Constans.DATA_PATH_DATA);
        // ConstansUtil.getDataDir(this,"/files/" + Constans.DATA_PATH_BACKUP);

        File root = ConstansUtil.getBaseDir();
        if (root.exists()) {
            root.mkdir();
        }

        List<File> files = new ArrayList<>();
        files.add(new File(root, Constans.DATA_PATH_CERT));
        files.add(new File(root, Constans.DATA_PATH_CARD));
        files.add(new File(root, Constans.DATA_PATH_DATA));
        files.add(new File(root, Constans.DATA_PATH_BACKUP));

        for (File file : files) {
            if (!file.exists()) {
                file.mkdir();
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
