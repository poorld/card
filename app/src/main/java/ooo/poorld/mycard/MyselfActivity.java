package ooo.poorld.mycard;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.lingala.zip4j.exception.ZipException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Response;
import ooo.poorld.mycard.entity.FileUploadResponse;
import ooo.poorld.mycard.entity.Upload;
import ooo.poorld.mycard.utils.BackupTask;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.FileUtils;
import ooo.poorld.mycard.utils.Tools;
import ooo.poorld.mycard.utils.ZipUtils;
import ooo.poorld.mycard.utils.okhttp.CallBackUtil;
import ooo.poorld.mycard.utils.okhttp.OkhttpUtil;
import ooo.poorld.mycard.view.MyProgressBar;
import ooo.poorld.mycard.view.PopupBackup;

import static ooo.poorld.mycard.utils.Constans.TAG;

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
    private BackupTask mTask;
    private boolean inited = false;

    private PopupBackup mPopupBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        upload = findViewById(R.id.upload);
        download = findViewById(R.id.download);
        upload.setOnClickListener(this);
        download.setOnClickListener(this);

        myProgressBar = new MyProgressBar(MyselfActivity.this);

        // 备份数据库
        mTask = new BackupTask(this);

        mPopupBackup = new PopupBackup(this);
        mPopupBackup.setItemClickListener(new PopupBackup.ItemClickListener() {
            @Override
            public void onItemClick(Upload upload) {
                downloadBackup(upload);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload:
                backup();
                break;
            case R.id.download:
                recovery();
                break;
        }
    }

    /**
     * 恢复备份
     */
    private void recovery() {


        String url = Constans.backup_list;
        OkhttpUtil.okHttpGet(url, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                JsonObject jo = new JsonParser().parse(response).getAsJsonObject();
                JsonElement data = jo.get("data");
                Type type = new TypeToken<List<Upload>>(){}.getType();

                List<Upload> uploads = new Gson().fromJson(data, type);
                mPopupBackup.addUploadDatas(uploads);
                mPopupBackup.show(upload);

            }
        });
    }

    /**
     * 备份
     */
    private void backup() {
        File zipOutDir = ConstansUtil.getStorageDir(Constans.DATA_PATH_BACKUP);
        File dataDir = ConstansUtil.getBaseDir();

        File zipOutFile = new File(zipOutDir, "back.zip");
        if (zipOutFile.exists()) {
            zipOutFile.delete();
        }


        mTask.doInBackground(BackupTask.COMMAND_BACKUP);

        // 压缩文件
        String zipPath = ZipUtils.zip(dataDir.getPath(), zipOutFile.getPath(), true, null);

        String url = Constans.uploadUrl;
        String fileKey = "file";
        String fileType = OkhttpUtil.FILE_TYPE_FILE;

        // 上传文件
        OkhttpUtil.okHttpUploadFile(url, zipOutFile, fileKey, fileType, new CallBackUtil.CallBackString() {

            @Override
            public void onFailure(Call call, Exception e) {
                myProgressBar.colseDialog();
                Toast.makeText(MyselfActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                JsonObject jo = new JsonParser().parse(response).getAsJsonObject();
                JsonElement data = jo.get("data");
                FileUploadResponse resp = new Gson().fromJson(data, FileUploadResponse.class);
                Upload upload = new Upload();
                upload.setId((int) ConstansUtil.getUUIDNumber());
                upload.setFileName(resp.getFileName());
                upload.setFilePath(resp.getFileDownloadUrl());
                upload.setFileSize(zipOutFile.length());
                saveUpload(upload);
            }

            @Override
            public void onProgress(float progress, long total) {
                super.onProgress(progress, total);
                if (!inited) {
                    inited = true;
                    int t = (int) (total / 1024 / 1024 * 1000);
                    Log.d(TAG, "Total: " + t);
                    myProgressBar.setMax(100);
                    myProgressBar.setTitle("正在备份...");
                }
                Log.d(TAG, "onProgress: " + progress * 100);
                myProgressBar.setProgress((int) (progress * 100));
            }
        });
    }

    private void saveUpload(Upload upload) {
        String url = Constans.upload_save;
        OkhttpUtil.okHttpPostJson(url, new Gson().toJson(upload), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                myProgressBar.colseDialog();
            }

            @Override
            public void onResponse(String response) {
                myProgressBar.colseDialog();
                inited = false;
                Toast.makeText(MyselfActivity.this, "备份成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadBackup(Upload upload) {
        File baseDir = ConstansUtil.getBaseDir();
        FileUtils.deleteDirectory(baseDir.getPath());
        File storage = ConstansUtil.getStorage();
        File zipOutDir = ConstansUtil.getStorageDir(Constans.DATA_PATH_BACKUP);
        File zipOutFile = new File(zipOutDir, "back.zip");
        if (zipOutFile.exists()) {
            zipOutFile.delete();
        }

        /*JsonObject jo = new JsonParser().parse(response).getAsJsonObject();
        JsonElement data = jo.get("data");
        Upload upload = new Gson().fromJson(data, Upload.class);*/

        String downloadUrl = Constans.downloadUrl + upload.getFileName();

        OkhttpUtil.okHttpDownloadFile(downloadUrl, new CallBackUtil.CallBackFile(zipOutFile.getParent(), zipOutFile.getName()) {
            @Override
            public void onFailure(Call call, Exception e) {
                Toast.makeText(MyselfActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(File response) {
                if (!zipOutFile.exists()) {
                    Toast.makeText(MyselfActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    ZipUtils.unzip(zipOutFile.getPath(), storage.getPath(), null);
                    mTask.doInBackground(BackupTask.COMMAND_RESTORE);
                    myProgressBar.colseDialog();
                    inited = false;
                    Toast.makeText(MyselfActivity.this, "恢复成功", Toast.LENGTH_SHORT).show();
                } catch (ZipException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(float progress, long total) {
                super.onProgress(progress, total);
                if (!inited) {
                    inited = true;
                    int t = (int) (total / 1024 / 1024 * 1000);
                    Log.d(TAG, "Total: " + t);
                    myProgressBar.setMax(100);
                    myProgressBar.setTitle("正在恢复备份...");
                }
                Log.d(TAG, "onProgress: " + progress);
                myProgressBar.setProgress((int)(progress));
            }
        });
    }
}
