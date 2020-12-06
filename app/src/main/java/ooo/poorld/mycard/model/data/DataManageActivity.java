package ooo.poorld.mycard.model.data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ooo.poorld.mycard.App;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.adapter.DataAdapter;
import ooo.poorld.mycard.common.DataType;
import ooo.poorld.mycard.entity.DaoSession;
import ooo.poorld.mycard.entity.Data;
import ooo.poorld.mycard.entity.DataDao;
import ooo.poorld.mycard.entity.FileData;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.GlideEngine;
import ooo.poorld.mycard.utils.Tools;
import ooo.poorld.mycard.view.PopupGetPhoto;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class DataManageActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fab;

    private static final int REQUEST_CODE = 9527;

    private static final String dataPath = Constans.BASE_PATH + File.separator + Constans.DATA_PATH_DATA;;
    private String mPath;
    private String mDataType;
    private PopupGetPhoto mPopupGetPhoto;

    private DaoSession mDaoSession;
    private DataDao mDataDao;

   /* private static final String PATH_DATA_DOCUMENT = getPath(Constans.DATA_PATH_DATA_DOCUMENT);
    private static final String PATH_DATA_MUSIC = getPath(Constans.DATA_PATH_DATA_MUSIC);
    private static final String PATH_DATA_VIDEO = getPath(Constans.DATA_PATH_DATA_VIDEO);
    private static final String PATH_DATA_IMAGE = getPath(Constans.DATA_PATH_DATA_IMAGE);

    private static String getPath(String finallyPath) {
        return Constans.BASE_PATH + File.separator + dataPath + File.separator + finallyPath;
    }*/

    public static void startActivity(Context context, String path, String dataType) {
        Intent intent = new Intent(context, DataManageActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("dataType", dataType);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manage);

        mDataType = getIntent().getStringExtra("dataType");

        mPopupGetPhoto = new PopupGetPhoto(this);
        mPopupGetPhoto.setPhotoListener(new PopupGetPhoto.GetPhotoListener() {
            @Override
            public void fromDir() {
                selectFile();
                mPopupGetPhoto.dismiss();
            }

            @Override
            public void fromAlbum() {
                pickFile();
                mPopupGetPhoto.dismiss();
            }
        });

        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pickFile();
                // selectFile();
                // 如果是选择图片，则使用pictureSelector
                if (Constans.DATA_PATH_DATA_IMAGE.equals(mDataType)) {
                    // mPopupGetPhoto.show(fab);
                    pictureSelect();
                } else {
                    selectFile();
                }
            }
        });

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(DataManageActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                //没有权限则申请权限
                ActivityCompat.requestPermissions(DataManageActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else {
                //有权限直接执行,docode()不用做处理
                initFile();

            }
        }else {
            //小于6.0，不用申请权限，直接执行
            initFile();
        }

        mDaoSession = ((App) getApplication()).getDaoSession();
        mDataDao = mDaoSession.getDataDao();

    }


    // 打开系统的文件选择器
    public void pickFile() {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/docx");
        this.startActivityForResult(intent, REQUEST_CODE);*/
        // 打开系统图库选择图片
        Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, REQUEST_CODE);
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


    private void pictureSelect() {
        //相册
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(true)
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(3)
                .isCompress(true)
                .minimumCompressSize(500)
                .compressSavePath(ConstansUtil.getStoragePath())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // 结果回调
                        // 1.media.getPath(); 为原图path
                        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                        // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                        onPictureSelect(result);
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });
    }


    public void onPictureSelect(List<LocalMedia> result) {
        File outputDir = new File(mPath);
        Observable.fromArray(result)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<List<LocalMedia>, List<Data>>() {
                    @Override
                    public List<Data> apply(List<LocalMedia> localMedia) throws Exception {
                        List<Data> datas = new ArrayList<>();
                        for (LocalMedia media : localMedia) {
                            long id = ConstansUtil.getUUIDNumber();

                            String path = media.getPath();
                            File input = new File(path);

                            // // 获取后缀
                            // String suffix = getSuffix(input.getName());
                            // // 新文件名
                            // String newFileName = id + suffix;

                            File output = new File(outputDir, input.getName());

                            Data data = new Data();
                            data.setDataID(id);
                            data.setDataName(input.getName());
                            data.setFilePath(output.getPath());
                            data.setDataType(DataType.valueOf(mDataType));

                            datas.add(data);

                            Tools.copy(input, output);
                        }
                        return datas;
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<Data>>() {
                    @Override
                    public void accept(List<Data> datas) throws Exception {
                        saveToDB(datas);
                    }
                });
    }

    /**
     * 文件选完后
     * @param uris
     */
    public void onFileSelected(List<Uri> uris) {
        File outputDir = new File(mPath);

        Observable.fromArray(uris)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<List<Uri>, List<Data>>() {
                    @Override
                    public List<Data> apply(List<Uri> uris) throws Exception {
                        List<Data> datas = new ArrayList<>();
                        for (Uri uri : uris) {
                            long id = ConstansUtil.getUUIDNumber();

                            String path = uri.getPath();
                            File output = Tools.getFileFromUri(uri, DataManageActivity.this, mPath);
                            /*File input = new File(path);

                            // 获取后缀
                            String suffix = getSuffix(input.getName());
                            // 新文件名
                            String newFileName = id + suffix;

                            File output = new File(outputDir, newFileName);*/

                            Data data = new Data();
                            data.setDataID(id);
                            data.setDataName(output.getName());
                            data.setFilePath(output.getPath());
                             data.setDataType(DataType.valueOf(mDataType));

                            datas.add(data);

                            // Tools.copy(input, output);
                        }
                        return datas;
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<Data>>() {
                    @Override
                    public void accept(List<Data> datas) throws Exception {
                        saveToDB(datas);
                    }
                });

    }

    private void saveToDB(List<Data> datas) {
        mDataDao.insertInTx(datas);
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
/*    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {

            *//*Uri uri = data.getData(); // 获取用户选择文件的URI
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
            cursor.close();*//*

            String pathResult = null;  // 获取图片路径的方法调用
            try {
                Uri uri = data.getData();
                pathResult = getPath(uri);
                Log.e("TAG", "图片路径===" + pathResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }*/




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
