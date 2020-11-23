package ooo.poorld.mycard.model.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.App;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.Card;
import ooo.poorld.mycard.entity.CardDao;
import ooo.poorld.mycard.entity.Certificate;
import ooo.poorld.mycard.entity.CertificateDao;
import ooo.poorld.mycard.entity.DaoSession;
import ooo.poorld.mycard.model.card.adapter.CardAdapter;
import ooo.poorld.mycard.model.card.adapter.CertAdapter;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class CardActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fab;
    private CardAdapter mCardAdapter;
    private CardDao mCardDao;
    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        mDaoSession = ((App) getApplication()).getDaoSession();
        mCardDao = mDaoSession.getCardDao();

        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);
        
        rv.setLayoutManager(new LinearLayoutManager(this));
        mCardAdapter = new CardAdapter(this);
        rv.setAdapter(mCardAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
                // startActivity();
                startActivityForResult(new Intent(CardActivity.this, AddCardActivity.class), 1001);
            }
        });

        loadData();
    }


    private void loadData() {
        List<Card> cards = mCardDao.loadAll();
        mCardAdapter.addAll(cards);
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
