package com.br.pagpeg.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.User;
import com.br.pagpeg.utils.ErrorException;
import com.br.pagpeg.utils.UserSingleton;
import com.br.pagpeg.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private Button btnLogin;
    private TextView txtCreateUser;
    private EditText email,password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userUID;
    private String status;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth.getInstance().signOut();
        setContentView(R.layout.activity_login_user);
        Utils.hideKeyboard(LoginActivity.this);

        userUID = getIntent().getStringExtra("user_uid");
        status = getIntent().getStringExtra("status");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Utils.openDialog(LoginActivity.this,"Entrando...");

                    FirebaseUser userLogged = firebaseAuth.getCurrentUser();
                    getUser(userLogged.getUid());

                    Log.d("Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d("Firebase", "onAuthStateChanged:signed_out");
                }

            }
        };

        email = (EditText) findViewById(R.id.user_email);
        password = (EditText) findViewById(R.id.user_password);

        txtCreateUser = (TextView) findViewById(R.id.createUser);
        txtCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(LoginActivity.this);
                startActivityForResult(new Intent(v.getContext(),RegisterActivity.class),1);
                //startActivity(new Intent(v.getContext(),RegisterActivity.class));
            }
        });

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(LoginActivity.this);
                loginUser();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getUser(String uid){

     mDatabase = FirebaseDatabase.getInstance().getReference();
     mDatabase.child("users/" + uid).addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {

             User user = dataSnapshot.getValue(User.class);

             Intent returnIntent = new Intent();
             returnIntent.putExtra("user",user);
             setResult(Activity.RESULT_OK,returnIntent);
             finish();

             UserSingleton userSingleton = UserSingleton.getInstance();
             userSingleton.setUser(user);

             Utils.closeDialog(LoginActivity.this);
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });

    }

    private void loginUser(){

        Utils.openDialog(this,"Entrando...");

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "signInWithEmail:failed", task.getException());

                            Exception exc = task.getException();
                            new AlertDialog.Builder(LoginActivity.this,R.style.Dialog_Quantity)
                                    .setPositiveButton("OK", null)
                                    .setTitle("PagPeg")
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setMessage(ErrorException.authFirebaseError(exc.getMessage(),LoginActivity.this))
                                    .create()
                                    .show();
                        }

                        Utils.closeDialog(LoginActivity.this);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){
            if(resultCode == RESULT_OK){

                User user = (User) data.getSerializableExtra("user");

                Intent returnIntent = new Intent();
                returnIntent.putExtra("user",user);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        }
    }
}
