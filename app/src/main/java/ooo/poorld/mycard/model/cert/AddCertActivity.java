package ooo.poorld.mycard.model.cert;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.App;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.common.HeaderViewAdapter;
import ooo.poorld.mycard.entity.CardImage;
import ooo.poorld.mycard.entity.CardImageDao;
import ooo.poorld.mycard.entity.Certificate;
import ooo.poorld.mycard.entity.CertificateDao;
import ooo.poorld.mycard.entity.DaoSession;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.FileUtils;
import ooo.poorld.mycard.utils.GlideEngine;
import ooo.poorld.mycard.utils.Tools;


/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class AddCertActivity extends AppCompatActivity {

    // private ImageView iv_add_img;
    private LinearLayout ll_image_container;
    private TextView btn_add;
    private TextView et_date;
    private EditText et_content;

    private EditText et_name;
    private EditText et_cardholder;
    private EditText et_number;
    private RecyclerView cert_rv;

    // private List<LocalMedia> mLocalMedia;
    private List<File> mFiles;

    // 是否已是添加
    private boolean certIsInserted;

    private Long certID;
    private DaoSession mDaoSession;
    private CardImageDao mCardImageDao;
    private CertificateDao mCertificateDao;
    private HeaderViewAdapter mCertImageAdapter;

    public static void startActivity(Context context, Long certId) {
        Intent intent = new Intent(context, AddCertActivity.class);
        if (certId != null) {
            intent.putExtra("certId", certId);
        }
        ((Activity)context).startActivityForResult(intent, 1111);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cert_add);

        init();

        getIntentData();
    }

    private void getIntentData() {
        long id = getIntent().getLongExtra("certId", -1);
        if (id == -1) {
            return;
        }
        certID = id;
        certIsInserted = true;
        btn_add.setText("编辑");
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        CertificateDao certificateDao = daoSession.getCertificateDao();
        Certificate cert = certificateDao.queryBuilder()
                .where(CertificateDao.Properties.CertificateID.eq(certID))
                .unique();

        et_name.setText(TextUtils.isEmpty(cert.getCertificateName()) ? "" : cert.getCertificateName());
        et_cardholder.setText(TextUtils.isEmpty(cert.getCertificateHolder()) ? "" : cert.getCertificateHolder());
        et_number.setText(TextUtils.isEmpty(cert.getCertificateNumber()) ? "" : cert.getCertificateNumber());
        et_date.setText(Tools.data2String(cert.getCertificateDate()));
        et_content.setText(TextUtils.isEmpty(cert.getNote()) ? "" : cert.getNote());

        mCertImageAdapter.addDatas(cert.getCardImages());
    }

    private void init() {
        mDaoSession = ((App) getApplication()).getDaoSession();
        mCardImageDao = mDaoSession.getCardImageDao();
        mCertificateDao = mDaoSession.getCertificateDao();

        /**
         * 初始化certID
         */
        certID = ConstansUtil.getUUIDNumber();

        // iv_add_img = findViewById(R.id.iv_add_img);
        btn_add = findViewById(R.id.btn_add);
        et_date = findViewById(R.id.et_date);
        et_content = findViewById(R.id.et_content);

        et_name = findViewById(R.id.et_name);
        et_cardholder = findViewById(R.id.et_cardholder);
        et_number = findViewById(R.id.et_number);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddCertActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(AddCertActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        /*iv_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int childCount = ll_image_container.getChildCount();
                if (childCount >= 3){
                    Toast.makeText(AddCertActivity.this, "最多3张", Toast.LENGTH_SHORT).show();
                    return;
                }
                addPhoto();
            }
        });*/

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*CertEntity entity = new CertEntity();
                // entity.setTitle(et_title.getText().toString());
                entity.setTitle(et_content.getText().toString());
                entity.setFiles(mFiles);*/


                // entity.setLocalMedia(mLocalMedia);

                updateCert();

            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tools.showDatePickDialog(AddCertActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //选择日期过后执行的事件
                        et_date.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, Tools.curentDate());
            }
        });

        mCertImageAdapter = new HeaderViewAdapter(this);
        cert_rv = findViewById(R.id.cert_rv);
        cert_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // mCertImageAdapter.setHeaderView(cert_rv);
        cert_rv.setAdapter(mCertImageAdapter);
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_cert_header, cert_rv, false);
        mCertImageAdapter.setHeaderView(inflate);
        mCertImageAdapter.setHeaderClickListener(new HeaderViewAdapter.HeaderClickListener() {
            @Override
            public void onHeaderClick() {
                addPhoto();
            }
        });
        mCertImageAdapter.setOnItemClickListener(new HeaderViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CardImage localMedia) {
                // 图片点击，放大
            }

            @Override
            public void onRemoveClick(CardImage localMedia) {
                // 删除图片
                boolean b = FileUtils.deleteFile(new File(localMedia.getFilePath()));
                mCardImageDao.delete(localMedia);
            }
        });
    }



    private void updateCert() {
        String etName = et_name.getText().toString();
        String etCardholder = et_cardholder.getText().toString();
        String etNumber = et_number.getText().toString();
        String etDate = et_date.getText().toString();
        String etContent = et_content.getText().toString();

        if (TextUtils.isEmpty(etName) || TextUtils.isEmpty(etCardholder) ||
                TextUtils.isEmpty(etNumber) || TextUtils.isEmpty(etDate) ||
                TextUtils.isEmpty(etContent)) {

            Toast.makeText(AddCertActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Certificate certificate = new Certificate(certID, etName, etNumber, etCardholder,etContent, Tools.string2Date(etDate));
            if (certIsInserted) {
                mCertificateDao.update(certificate);
            } else {
                mCertificateDao.insert(certificate);
            }
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    private void saveData(List<LocalMedia> localMedias) {
        // long certId = ConstansUtil.getUUIDNumber();
        Certificate certificate = new Certificate();
        certificate.setCertificateID(certID);


        List<CardImage> images = new ArrayList<>();

        // certificate.setCertificateID(certId);
        long id = mCertificateDao.insertOrReplace(certificate);
        certIsInserted = true;

        for (LocalMedia media : localMedias) {
            long imageId = ConstansUtil.getUUIDNumber();
            String fileName = imageId + "." + media.getMimeType().replace("image/","");
            // String saveFilePath = ConstansUtil
            //         .getExternalFilesPath(this, Constans.DATA_PATH_CERT, fileName);

            String compressPath = media.getCompressPath();
            File inputFile = new File(compressPath);
            File outputFile = ConstansUtil.getStorageDir(Constans.DATA_PATH_CERT);
            File file = new File(outputFile.getAbsolutePath(), fileName);
            Tools.copy(inputFile, file);
            CardImage image = new CardImage();
            image.setCardID(certificate.getCertificateID());
            image.setFileName(fileName);
            image.setFilePath(file.getPath());
            image.setFileSize(media.getSize());
            image.setImageID(imageId);
            images.add(image);
        }
        mCardImageDao.insertInTx(images);
        mCertImageAdapter.addDatas(images);

        QueryBuilder<Certificate> qb = mCertificateDao.queryBuilder();
        qb.where(CertificateDao.Properties.CertificateID.eq(id));

        Certificate unique = qb.uniqueOrThrow();
        // Certificate unique = qb.where(CertificateDao.Properties.CertificateID.eq(id)).unique();
        unique.resetCardImages();
        List<CardImage> cardImages = unique.getCardImages();

    }


    private void addPhoto() {
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
                        // List<File> files = new ArrayList<>();

                        for (LocalMedia localMedia : result) {

                            /*File file = null;
                            if (localMedia.isCompressed()) {
                                file = new File(localMedia.getCompressPath());
                            } else {
                                file = new File(localMedia.getPath());
                            }

                            files.add(file);*/

                            // LogUtils.e(localMedia.getSize() + "");
                            // LogUtils.e(localMedia.getPath() + "");
                            // LogUtils.e(localMedia.isCompressed() + "");
                            // LogUtils.e(localMedia.getCompressPath() + "");

                            Log.i(Constans.TAG, localMedia.getSize() + "");
                            Log.i(Constans.TAG, localMedia.getPath());
                            Log.i(Constans.TAG, localMedia.isCompressed() + "");
                            Log.i(Constans.TAG, localMedia.getCompressPath());

                        }

                        saveData(result);

                        // uploadFiles(files);
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });
    }

}
