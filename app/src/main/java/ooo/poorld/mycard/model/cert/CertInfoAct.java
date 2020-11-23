package ooo.poorld.mycard.model.cert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.App;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.Certificate;
import ooo.poorld.mycard.entity.CertificateDao;
import ooo.poorld.mycard.entity.DaoSession;
import ooo.poorld.mycard.model.cert.adapter.CertInfoAdapter;
import ooo.poorld.mycard.utils.Tools;

/**
 * author: teenyda
 * date: 2020/11/23
 * description:
 */
public class CertInfoAct extends AppCompatActivity {

    private TextView et_name;
    private TextView et_cardholder;
    private TextView et_number;
    private TextView et_date;
    private TextView et_content;

    private RecyclerView cert_info_rv;
    private CertInfoAdapter mCertInfoAdapter;

    public static void startActivity(Context context, Long certId) {
        Intent intent = new Intent(context, CertInfoAct.class);
        intent.putExtra("certId", certId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cert_info);

        init();

        getIntentData();
    }

    private void getIntentData() {
        long certId = getIntent().getLongExtra("certId", 0);
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        CertificateDao certificateDao = daoSession.getCertificateDao();
        Certificate cert = certificateDao.queryBuilder()
                .where(CertificateDao.Properties.CertificateID.eq(certId))
                .unique();

        et_name.setText(cert.getCertificateName());
        et_cardholder.setText(cert.getCertificateHolder());
        et_number.setText(cert.getCertificateNumber());
        et_date.setText(Tools.data2String(cert.getCertificateDate()));
        et_content.setText(cert.getNote());

        mCertInfoAdapter.addImages(cert.getCardImages());
    }

    private void init() {
        et_name = findViewById(R.id.et_name);
        et_cardholder = findViewById(R.id.et_cardholder);
        et_number = findViewById(R.id.et_number);
        et_date = findViewById(R.id.et_date);
        et_content = findViewById(R.id.et_content);
        cert_info_rv = findViewById(R.id.cert_info_rv);

        mCertInfoAdapter = new CertInfoAdapter(this);
        cert_info_rv.setLayoutManager(new LinearLayoutManager(this));
        cert_info_rv.setAdapter(mCertInfoAdapter);
    }

}
