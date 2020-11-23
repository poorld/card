package ooo.poorld.mycard;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ooo.poorld.mycard.adapter.DataAdapter;
import ooo.poorld.mycard.entity.FileData;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.GlideEngine;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class DataActivity extends AppCompatActivity {

    private RecyclerView rv;

    private static final String dataPath = "MyCard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        rv = findViewById(R.id.rv);


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(DataActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //没有权限则申请权限
                ActivityCompat.requestPermissions(DataActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else {
                //有权限直接执行,docode()不用做处理
                initFile();

            }
        }else {
            //小于6.0，不用申请权限，直接执行
            initFile();
        }


    }

    private void initFile() {
        File directory_data = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
            directory_data = Environment.getExternalStoragePublicDirectory(dataPath);

        }else{
            //没外部存储就使用内部存储
            ///data/data/app包名/files目录
            directory_data= new File(getFilesDir() + File.separator + dataPath);
        }
        if(!directory_data.exists()){//判断文件目录是否存在
            directory_data.mkdirs();
        }

        File[] files = directory_data.listFiles();
        List<FileData> arrayList = new ArrayList<>();
        for (File file: files) {
            file.isDirectory();
            FileData fileData = new FileData();
            String fileName = file.getName();

            fileData.setFileName(fileName);
            fileData.setDirectory(file.isDirectory());
            fileData.setFileType(getSuffix(fileName));
            fileData.setFileSize(file.length());
            fileData.setFilePath(directory_data.getPath() + File.separator + fileName);

            arrayList.add(fileData);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new DataAdapter(this, arrayList));
    }

    private String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
