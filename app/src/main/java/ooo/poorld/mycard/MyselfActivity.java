package ooo.poorld.mycard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ExcludeFileFilter;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Response;
import ooo.poorld.mycard.utils.BackupTask;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.ZipUtils;
import ooo.poorld.mycard.utils.okhttp.CallBackUtil;
import ooo.poorld.mycard.utils.okhttp.OkhttpUtil;
import ooo.poorld.mycard.view.MyProgressBar;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class MyselfActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv;
    private FloatingActionButton fab;
    private TextView upload;
    private TextView download;

    private MyProgressBar myProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        upload = findViewById(R.id.upload);
        download = findViewById(R.id.download);
        upload.setOnClickListener(this);
        download.setOnClickListener(this);

        myProgressBar = new MyProgressBar(MyselfActivity.this);
        myProgressBar.initDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload:
                zip();
                break;
            case R.id.download:
                unzip();
                break;
        }
    }

    private void unzip() {
        File zipOutDir = ConstansUtil.getStorageDir(Constans.DATA_PATH_BACKUP);
        File storage = ConstansUtil.getStorage();

        File zipOutFile = new File(zipOutDir, "back.zip");
        if (!zipOutFile.exists()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            ZipUtils.unzip(zipOutFile.getPath(), storage.getPath(), null);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private void zip() {
        File zipOutDir = ConstansUtil.getStorageDir(Constans.DATA_PATH_BACKUP);
        File dataDir = ConstansUtil.getBaseDir();

        File zipOutFile = new File(zipOutDir, "back.zip");
        if (zipOutFile.exists()) {
            zipOutFile.delete();
        }

        // 备份数据库
        BackupTask task = new BackupTask(this);
        task.doInBackground(BackupTask.COMMAND_BACKUP);

        // 压缩文件
        String zipPath = ZipUtils.zip(dataDir.getPath(), zipOutFile.getPath(), true, null);

        String url = Constans.uploadUrl;
        String fileKey = "file";
        String fileType = OkhttpUtil.FILE_TYPE_FILE;

        // 上传文件
        OkhttpUtil.okHttpUploadFile(url, zipOutFile, fileKey, fileType, new CallBackUtil() {
            @Override
            public Object onParseResponse(Call call, Response response) {
                return null;
            }

            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(Object response) {

            }

            @Override
            public void onProgress(float progress, long total) {
                super.onProgress(progress, total);
                myProgressBar.setProgress((int) progress);
            }
        });
    }
}
