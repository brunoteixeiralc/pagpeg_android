package com.br.pagpeg.activity.shopper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.ChooseActivity;
import com.br.pagpeg.model.Shopper;
import com.br.pagpeg.utils.ErrorException;
import com.br.pagpeg.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 26/07/16.
 */

public class LoginActivity extends Activity {

    private Button btnConfirm;
    private TextView register;
    private DatabaseReference mDatabase;
    private EditText accessCode,email;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String shopperId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_shopper);

        shopperId = getIntent().getStringExtra("shopper_uid");

        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        accessCode = (EditText) findViewById(R.id.code);
        email = (EditText) findViewById(R.id.email);

        if(com.br.pagpeg.BuildConfig.BUILD_TYPE.toString() == "debug"){
            email.setText("r@gmail.com");
            accessCode.setText("12345678");
        }

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginShopper();
            }
        });

        register = (TextView) findViewById(R.id.createShopper);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(LoginActivity.this);
                startActivity(new Intent(v.getContext(), com.br.pagpeg.activity.shopper.RegisterActivity.class));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void loginShopper(){

        Utils.openDialog(LoginActivity.this,"Entrando...");

        mAuth.signInWithEmailAndPassword("shopper_" + email.getText().toString(), accessCode.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Utils.closeDialog(LoginActivity.this);
                            Log.w("Firebase", "signInWithEmail:failed", task.getException());

                            Exception exc = task.getException();
                            new AlertDialog.Builder(LoginActivity.this,R.style.Dialog_Quantity)
                                    .setPositiveButton("OK", null)
                                    .setTitle("PagPeg")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setMessage(ErrorException.authFirebaseError(exc.getMessage(),LoginActivity.this))
                                    .create()
                                    .show();

                        }else{
                            getShopper(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        }
                    }
                });
    }

    private void getShopper(String uid){

        mDatabase.child("shoppers/" + uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Shopper shopper = dataSnapshot.getValue(Shopper.class);
                Intent intent = new Intent(LoginActivity.this,MainShopperActivity.class);
                if(shopperId != null)
                    intent.putExtra("shopper_uid",shopperId);
                intent.putExtra("shopper",shopper);
                startActivity(intent);

                Utils.closeDialog(LoginActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ChooseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
