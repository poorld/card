package ooo.poorld.mycard.model.data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.Tools;

/**
 * author: teenyda
 * date: 2020/11/27
 * description:
 */
public class DataMain extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_document;
    private LinearLayout ll_image;
    private LinearLayout ll_music;
    private LinearLayout ll_video;

    private static final String dataPath = Constans.BASE_PATH + File.separator + Constans.DATA_PATH_DATA;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DataMain.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        ll_document = findViewById(R.id.ll_document);
        ll_image = findViewById(R.id.ll_image);
        ll_music = findViewById(R.id.ll_music);
        ll_video = findViewById(R.id.ll_video);

        ll_document.setOnClickListener(this);
        ll_image.setOnClickListener(this);
        ll_music.setOnClickListener(this);
        ll_video.setOnClickListener(this);
    }


    private String getBaseDir() {
        File directory;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
            directory = Environment.getExternalStoragePublicDirectory(dataPath);

        } else {
            //没外部存储就使用内部存储
            ///data/data/app包名/files目录
            directory = new File(getFilesDir() + File.separator + dataPath);
        }
        return directory.getPath();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_document:
                startAct(Constans.DATA_PATH_DATA_DOCUMENT);
                break;
             case R.id.ll_image:
                 startAct(Constans.DATA_PATH_DATA_IMAGE);
                break;
             case R.id.ll_music:
                 startAct(Constans.DATA_PATH_DATA_MUSIC);
                break;
             case R.id.ll_video:
                 startAct(Constans.DATA_PATH_DATA_VIDEO);
                break;
        }

    }

    private void startAct(String type) {
        String dir = Tools.getBaseDir(this, dataPath) + File.separator + type;
        DataManageActivity.startActivity(this, dir, type);
    }
}
