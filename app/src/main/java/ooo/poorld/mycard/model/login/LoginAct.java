package ooo.poorld.mycard.model.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import ooo.poorld.mycard.MainActivity;
import ooo.poorld.mycard.R;

/**
 * author: teenyda
 * date: 2020/12/29
 * description:
 */
public class LoginAct extends AppCompatActivity {

    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this, MainActivity.class));
                finish();
            }
        });
    }
}
