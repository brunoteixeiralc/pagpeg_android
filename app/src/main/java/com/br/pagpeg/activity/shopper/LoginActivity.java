package com.br.pagpeg.activity.shopper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.br.pagpeg.R;
import com.br.pagpeg.model.Shopper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_shopper);

        FirebaseAuth.getInstance().signOut();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        accessCode = (EditText) findViewById(R.id.code);
        email = (EditText) findViewById(R.id.email);

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
                intent.putExtra("shopper",shopper);
                startActivity(intent);

                Utils.closeDialog(LoginActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
