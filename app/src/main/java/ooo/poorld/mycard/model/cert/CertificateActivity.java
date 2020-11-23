package ooo.poorld.mycard.model.cert;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.List;

import ooo.poorld.mycard.App;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.model.cert.adapter.CertAdapter;
import ooo.poorld.mycard.entity.Certificate;
import ooo.poorld.mycard.entity.CertificateDao;
import ooo.poorld.mycard.entity.DaoSession;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class CertificateActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fab;
    private CertAdapter mCertAdapter;
    private CertificateDao mCertificateDao;
    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        mDaoSession = ((App) getApplication()).getDaoSession();
        mCertificateDao = mDaoSession.getCertificateDao();

        rv = findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(gridLayoutManager);
        mCertAdapter = new CertAdapter(this);
        rv.setAdapter(mCertAdapter);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
                // startActivity();
                startActivityForResult(new Intent(CertificateActivity.this, AddCertActivity.class), 1001);
            }
        });

        loadData();
    }


    private void loadData() {
        List<Certificate> certificates = mCertificateDao.loadAll();
        mCertAdapter.addCerts(certificates);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // CertEntity entity = (CertEntity) data.getSerializableExtra("cert");
            // mCertAdapter.addCert(entity);
            loadData();
        }
    }
}
