package com.br.pagpeg.activity.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import com.br.pagpeg.R;
import com.br.pagpeg.utils.mail.Mail;
import com.br.pagpeg.utils.mail.SendEmailAsyncTask;
import com.br.pagpeg.utils.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 26/07/16.
 */
public class ProductRegisterActivity extends Activity {

    private Button btnRegister;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);
        Utils.hideKeyboard(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String[] recipients = { "bruno@br4dev.com","brunoteixeiralc@gmail.com"};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.activity = this;
        email.m = new Mail("pagpegcomvc@gmail.com", "pagpegcomvc2016"
                .toString());
        email.m.set_from("pagpegcomvc@gmail.com");
        email.m.setBody("Nome do produto:\nDescrição:\nCategoria:\nPreço:\nCódigo de barra(opcional):");
        email.m.set_to(recipients);
        email.m.set_subject("Novo Produto cadastrado");
        email.execute();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
