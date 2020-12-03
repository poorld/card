package ooo.poorld.mycard.model.data;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.adapter.DataAdapter;
import ooo.poorld.mycard.entity.FileData;
import ooo.poorld.mycard.utils.Constans;

/**
 * author: teenyda
 * date: 2020/11/27
 * description:
 */
public class DataMain extends AppCompatActivity {
    private RecyclerView rv;

    private static final String dataPath = Constans.BASE_PATH + File.separator + Constans.DATA_PATH_DATA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        rv = findViewById(R.id.rv);


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(DataMain.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //没有权限则申请权限
                ActivityCompat.requestPermissions(DataMain.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
        // if(!directory_data.exists()){//判断文件目录是否存在
        //     directory_data.mkdirs();
        // }

        File[] files = directory_data.listFiles();
        List<FileData> arrayList = new ArrayList<>();
        for (File file: files) {
            boolean directory = file.isDirectory();

            FileData fileData = new FileData();
            if (directory) {
                fileData.setFilePath(file.getAbsolutePath());
                fileData.setDirectory(true);
            }

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
