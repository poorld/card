package ooo.poorld.mycard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import ooo.poorld.mycard.utils.Constans;
import ooo.poorld.mycard.utils.ConstansUtil;
import ooo.poorld.mycard.utils.ZipUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        upload = findViewById(R.id.upload);
        download = findViewById(R.id.download);
        upload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload:
                zip();
                break;
        }
    }

    private void zip() {
        File zipOutDir = ConstansUtil.getStorageDir(Constans.DATA_PATH_BACKUP);
        File dataDir = ConstansUtil.getBaseDir();

        File zipOutFile = new File(ConstansUtil.getStorage(), "back.zip");

        /*ZipUtils.compressFolder(zipOutFile.getPath(), null, dataDir.getPath(), new ExcludeFileFilter() {
            @Override
            public boolean isExcluded(File file) {
                if (Constans.DATA_PATH_BACKUP.equals(file.getName())) {
                    return true;
                }
                return false;
            }
        });*/
        ZipFile zipFile = new ZipFile(zipOutFile.getPath());
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Constans.COMP_DEFLATE); // 压缩方式
        parameters.setCompressionLevel(Constans.DEFLATE_LEVEL_NORMAL); // 压缩级别
        File[] list = dataDir.listFiles();
        // 遍历test文件夹下所有的文件、文件夹
        try {
            for (File f : list) {
                if (f.isDirectory()) {
                    zipFile.addFolder(f, parameters);
                } else {
                    zipFile.addFile(f, parameters);
                }
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
