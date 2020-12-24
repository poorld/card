package ooo.poorld.mycard.model.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.App;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.Card;
import ooo.poorld.mycard.entity.CardDao;
import ooo.poorld.mycard.entity.CardImage;
import ooo.poorld.mycard.entity.Certificate;
import ooo.poorld.mycard.entity.CertificateDao;
import ooo.poorld.mycard.entity.DaoSession;
import ooo.poorld.mycard.model.card.adapter.CertInfoAdapter;
import ooo.poorld.mycard.model.cert.AddCertActivity;
import ooo.poorld.mycard.model.cert.CertInfoAct;
import ooo.poorld.mycard.utils.Tools;

/**
 * author: teenyda
 * date: 2020/11/23
 * description:
 */
public class CardInfoAct extends AppCompatActivity {

    private TextView et_name;
    private TextView et_cardholder;
    private TextView et_number;
    private TextView et_date;
    private TextView et_content;
    private Button cert_edit;

    private RecyclerView cert_info_rv;
    private CertInfoAdapter mCertInfoAdapter;

    public static void startActivity(Context context, Long certId) {
        Intent intent = new Intent(context, CardInfoAct.class);
        intent.putExtra("cardId", certId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        init();

        getIntentData();
    }

    private void getIntentData() {
        Card card = getCert();

        et_name.setText(card.getCardName());
        et_cardholder.setText(card.getCardHolder());
        et_number.setText(card.getCardNumber());
        et_date.setText(Tools.data2String(card.getValidityDate()));
        et_content.setText(card.getNote());

        mCertInfoAdapter.addImages(card.getCardImages());
    }

    private Card getCert() {
        long cardId = getIntent().getLongExtra("cardId", 0);
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        CardDao cardDao = daoSession.getCardDao();
        Card card = cardDao.queryBuilder()
                .where(CardDao.Properties.Id.eq(cardId))
                .unique();
        return card;
    }

    private void init() {
        et_name = findViewById(R.id.et_name);
        et_cardholder = findViewById(R.id.et_cardholder);
        et_number = findViewById(R.id.et_number);
        et_date = findViewById(R.id.et_date);
        et_content = findViewById(R.id.et_content);
        cert_info_rv = findViewById(R.id.cert_info_rv);
        cert_edit = findViewById(R.id.cert_edit);

        mCertInfoAdapter = new CertInfoAdapter(this);
        cert_info_rv.setLayoutManager(new LinearLayoutManager(this));
        cert_info_rv.setAdapter(mCertInfoAdapter);

        cert_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card card = getCert();
                AddCardActivity.startActivity(CardInfoAct.this, card.getId());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getIntentData();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

}
