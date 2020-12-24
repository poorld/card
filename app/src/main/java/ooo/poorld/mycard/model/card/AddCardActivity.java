package ooo.poorld.mycard.model.card;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import ooo.poorld.mycard.entity.Card;
import ooo.poorld.mycard.entity.CardDao;
import ooo.poorld.mycard.entity.CardImage;
import ooo.poorld.mycard.entity.CardImageDao;
import ooo.poorld.mycard.entity.Certificate;
import ooo.poorld.mycard.entity.CertificateDao;
import ooo.poorld.mycard.entity.DaoSession;
import ooo.poorld.mycard.model.cert.AddCertActivity;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.FileUtils;
import ooo.poorld.mycard.utils.GlideEngine;
import ooo.poorld.mycard.utils.PxUtils;
import ooo.poorld.mycard.utils.Tools;


/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class AddCardActivity extends AppCompatActivity {

    private TextView btn_add;
    private TextView et_date;
    private EditText et_content;

    private EditText et_name;
    private EditText et_cardholder;
    private EditText et_number;

    // private List<LocalMedia> mLocalMedia;
    private List<File> mFiles;
    private RecyclerView cert_rv;
    private HeaderViewAdapter mCertImageAdapter;

    private boolean certIsInserted;

    private Long cardID;
    private DaoSession mDaoSession;
    private CardImageDao mCardImageDao;
    private CardDao mCardDao;

    public static void startActivity(Context context, Long certId) {
        Intent intent = new Intent(context, AddCardActivity.class);
        if (certId != null) {
            intent.putExtra("cardId", certId);
        }
        ((Activity)context).startActivityForResult(intent, 1111);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_add);

        init();

        getIntentData();
    }

    private void getIntentData() {
        long id = getIntent().getLongExtra("cardId", -1);
        if (id == -1) {
            return;
        }
        cardID = id;
        certIsInserted = true;
        btn_add.setText("编辑");
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        CardDao cardDao = daoSession.getCardDao();
        Card cert = cardDao.queryBuilder()
                .where(CardDao.Properties.Id.eq(cardID))
                .unique();

        et_name.setText(TextUtils.isEmpty(cert.getCardName()) ? "" : cert.getCardName());
        et_cardholder.setText(TextUtils.isEmpty(cert.getCardHolder()) ? "" : cert.getCardHolder());
        et_number.setText(TextUtils.isEmpty(cert.getCardNumber()) ? "" : cert.getCardNumber());
        et_date.setText(Tools.data2String(cert.getValidityDate()));
        et_content.setText(TextUtils.isEmpty(cert.getNote()) ? "" : cert.getNote());

        mCertImageAdapter.addDatas(cert.getCardImages());
    }

    private void init() {
        mDaoSession = ((App) getApplication()).getDaoSession();
        mCardImageDao = mDaoSession.getCardImageDao();
        mCardDao = mDaoSession.getCardDao();

        /**
         * 初始化certID
         */
        cardID = ConstansUtil.getUUIDNumber();

        btn_add = findViewById(R.id.btn_add);
        et_date = findViewById(R.id.et_date);
        et_content = findViewById(R.id.et_content);

        et_name = findViewById(R.id.et_name);
        et_cardholder = findViewById(R.id.et_cardholder);
        et_number = findViewById(R.id.et_number);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddCardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(AddCardActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*CertEntity entity = new CertEntity();
                // entity.setTitle(et_title.getText().toString());
                entity.setTitle(et_content.getText().toString());
                entity.setFiles(mFiles);*/


                // entity.setLocalMedia(mLocalMedia);

                updateCert();
                setResult(RESULT_OK);
                finish();
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tools.showDatePickDialog(AddCardActivity.this,new DatePickerDialog.OnDateSetListener() {
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

            Toast.makeText(AddCardActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Card certificate = new Card(cardID, etName, etNumber, etCardholder,etContent, Tools.string2Date(etDate));
            if (certIsInserted) {
                mCardDao.update(certificate);
            } else {
                mCardDao.insert(certificate);
            }

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }


    }

    private void saveData(List<LocalMedia> localMedias) {
        // long certId = ConstansUtil.getUUIDNumber();
        Card certificate = new Card();
        certificate.setId(cardID);


        List<CardImage> images = new ArrayList<>();

        // certificate.setCertificateID(certId);
        long id = mCardDao.insertOrReplace(certificate);
        certIsInserted = true;

        for (LocalMedia media : localMedias) {
            long imageId = ConstansUtil.getUUIDNumber();
            String fileName = imageId + "." + media.getMimeType().replace("image/","");
            // String saveFilePath = ConstansUtil
            //         .getExternalFilesPath(this, Constans.DATA_PATH_CERT, fileName);

            String compressPath = media.getCompressPath();
            File inputFile = new File(compressPath);
            File outputFile = ConstansUtil.getStorageDir(Constans.DATA_PATH_CARD);
            File file = new File(outputFile.getAbsolutePath(), fileName);
            Tools.copy(inputFile, file);
            CardImage image = new CardImage();
            image.setCardID(certificate.getId());
            image.setFileName(fileName);
            image.setFilePath(file.getPath());
            image.setFileSize(media.getSize());
            image.setImageID(imageId);
            images.add(image);
        }

        mCardImageDao.insertInTx(images);
        mCertImageAdapter.addDatas(images);

        QueryBuilder<Card> qb = mCardDao.queryBuilder();
        qb.where(CardDao.Properties.Id.eq(id));

        Card unique = qb.uniqueOrThrow();
        unique.resetCardImages();
        // Certificate unique = qb.where(CertificateDao.Properties.CertificateID.eq(id)).unique();
        // unique.resetCardImages();
        // List<CardImage> cardImages = unique.getCardImages();

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
