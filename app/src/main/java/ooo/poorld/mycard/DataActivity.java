package ooo.poorld.mycard;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greentoad.turtlebody.docpicker.DocPicker;
import com.greentoad.turtlebody.docpicker.core.DocPickerConfig;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ooo.poorld.mycard.adapter.DataAdapter;
import ooo.poorld.mycard.entity.FileData;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.GlideEngine;
import ooo.poorld.mycard.utils.Tools;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class DataActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fab;

    private static final int REQUEST_CODE = 9527;

    private static final String dataPath = Constans.BASE_PATH + File.separator + Constans.DATA_PATH_DATA;;
    private String mPath;

   /* private static final String PATH_DATA_DOCUMENT = getPath(Constans.DATA_PATH_DATA_DOCUMENT);
    private static final String PATH_DATA_MUSIC = getPath(Constans.DATA_PATH_DATA_MUSIC);
    private static final String PATH_DATA_VIDEO = getPath(Constans.DATA_PATH_DATA_VIDEO);
    private static final String PATH_DATA_IMAGE = getPath(Constans.DATA_PATH_DATA_IMAGE);

    private static String getPath(String finallyPath) {
        return Constans.BASE_PATH + File.separator + dataPath + File.separator + finallyPath;
    }*/

    public static void startActivity(Context context, String path) {
        Intent intent = new Intent(context, DataActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pickFile();
                selectFile();
            }
        });

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

    // 打开系统的文件选择器
    public void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/docx");
        this.startActivityForResult(intent, REQUEST_CODE);
    }

    public void selectFile() {
        if (TextUtils.isEmpty(mPath)) {
            return;
        }

        ArrayList<String> docs = new ArrayList<>();
        String parent = new File(mPath).getName();
        switch (parent) {
            case Constans.DATA_PATH_DATA_DOCUMENT:
                docs.add(DocPicker.DocTypes.PDF);
                docs.add(DocPicker.DocTypes.MS_POWERPOINT);
                docs.add(DocPicker.DocTypes.MS_EXCEL);
                docs.add(DocPicker.DocTypes.TEXT);
                break;
            case Constans.DATA_PATH_DATA_IMAGE:
                docs.add(DocPicker.DocTypes.IMAGE);
                break;
            case Constans.DATA_PATH_DATA_MUSIC:
                docs.add(DocPicker.DocTypes.AUDIO);
                break;
            case Constans.DATA_PATH_DATA_VIDEO:
                docs.add(DocPicker.DocTypes.VIDEO);
                break;
        }

        DocPickerConfig pickerConfig = new DocPickerConfig()
                .setAllowMultiSelection(true)
                .setShowConfirmationDialog(true)
                .setExtArgs(docs);

        DocPicker.with(this)
                .setConfig(pickerConfig)
                .onResult()
                .subscribe(new Observer<ArrayList<Uri>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ArrayList<Uri> uris) {
                        //uris: list of uri
                        onFileSelected(uris);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });

    }

    public void onFileSelected(List<Uri> uris) {
        File output = new File(mPath);

        Observable.fromArray(uris)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Uri>>() {
                    @Override
                    public void accept(List<Uri> uris) throws Exception {
                        for (Uri uri : uris) {
                            String path = uri.getPath();
                            File input = new File(path);
                            Tools.copy(input, output);
                        }
                    }
                });

    }

    private void initFile() {
        File directory_data = null;

        mPath = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(mPath)) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
                directory_data = Environment.getExternalStoragePublicDirectory(dataPath);

            } else {
                //没外部存储就使用内部存储
                ///data/data/app包名/files目录
                directory_data = new File(getFilesDir() + File.separator + dataPath);
            }
        } else {
            directory_data = new File(mPath);
        }


        // if(!directory_data.exists()){//判断文件目录是否存在
        //     directory_data.mkdirs();
        // }

        File[] files = directory_data.listFiles();
        List<FileData> arrayList = new ArrayList<>();
        for (File file: files) {
            boolean directory = file.isDirectory();

            FileData fileData = new FileData();
            String fileName = file.getName();

            fileData.setDirectory(directory);
            fileData.setFileName(fileName);
            fileData.setFileType(getSuffix(fileName));
            fileData.setFileSize(file.length());

            if (directory) {
                fileData.setFilePath(file.getAbsolutePath());
            } else {
                fileData.setFilePath(directory_data.getAbsolutePath() + File.separator + fileName);
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


    // 获取文件的真实路径
    /*@Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            // 用户未选择任何文件，直接返回
            return;
        }
        Uri uri = data.getData(); // 获取用户选择文件的URI
        // 通过ContentProvider查询文件路径
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // 未查询到，说明为普通文件，可直接通过URI获取文件路径
            String path = uri.getPath();
            return;
        }
        if (cursor.moveToFirst()) {
            // 多媒体文件，从数据库中获取文件的真实路径
            String path = cursor.getString(cursor.getColumnIndex("_data"));
        }
        cursor.close();
    }
*/
    /**
     * 打开文件夹
     */
    /*public void openFolder(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()

                +  File.separator + "myFolder" + File.separator);

        intent.setDataAndType(uri, "text/csv");

        startActivity(Intent.createChooser(intent, "Open folder"));

    }*/

}
