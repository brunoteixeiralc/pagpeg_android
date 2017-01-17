package com.br.pagpeg.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.br.pagpeg.R;
import com.br.pagpeg.model.User;
import com.br.pagpeg.retrofit.RetrofitService;
import com.br.pagpeg.retrofit.ServiceGenerator;
import com.br.pagpeg.retrofit.model.Client;
import com.br.pagpeg.utils.UserSingleton;
import com.br.pagpeg.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 26/07/16.
 */
@RuntimePermissions
public class RegisterActivity extends Activity {

    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText email,password,name,number;
    private TextView nameLabel;
    private String user_img_url;
    private CircleImageView circleImageView;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmapUserImage =  null;
    private String deviceId = "";
    private String one_signal_key = "";
    private User user;
    private String actualNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Utils.hideKeyboard(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        number = (EditText) findViewById(R.id.user_number);
        email = (EditText) findViewById(R.id.user_email);
        password = (EditText) findViewById(R.id.user_password);
        name = (EditText) findViewById(R.id.user_name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameLabel = (TextView) findViewById(R.id.user_name_label);
                nameLabel.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.openDialog(v.getContext(),"Cadastrando...");
                createUser();
            }
        });

        circleImageView = (CircleImageView) findViewById(R.id.user_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivityPermissionsDispatcher.pickGalleryWithCheck(RegisterActivity.this);
            }
        });

        RegisterActivityPermissionsDispatcher.getDeviceIdWithCheck(RegisterActivity.this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                bitmapUserImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                circleImageView.setImageBitmap(bitmapUserImage);
                circleImageView.setDrawingCacheEnabled(true);
                circleImageView.buildDrawingCache();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        RegisterActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    private void createUser(){

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.e("Firebase", task.getException().getMessage());
                            Utils.closeDialog(RegisterActivity.this.getApplicationContext());
                        }else{
                            if(bitmapUserImage != null)
                                saveImageStorage();
                            else{
                                saveUser();
                            }
                        }
                    }
                });
    }

    private void saveUser(){

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                one_signal_key = userId;
            }
        });

        OneSignal.getTags(new OneSignal.GetTagsHandler() {
            @Override
            public void tagsAvailable(JSONObject tags) {

                if(tags != null){
                    try {
                        tags.put("pagpeg_user", "user");
                        OneSignal.sendTags(tags);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    OneSignal.sendTag("pagpeg_user","user");
                }
            }
        });

        user = new User(name.getText().toString(),number.getText().toString(),email.getText().toString(),user_img_url,deviceId,one_signal_key,"");

        registerClientIugu(user.getEmail(),user.getName());

    }

    @NeedsPermission(android.Manifest.permission.READ_PHONE_STATE)
    public void getDeviceId(){

        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null){
            deviceId = mTelephony.getDeviceId();
        }else{
            deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        actualNumber = Utils.getTelephoneNumber(this);
        if(actualNumber != null){
            number.setText(actualNumber);
        }
    }

    private void saveImageStorage(){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(com.br.pagpeg.BuildConfig.ENDPOINTSTORAGE);
        StorageReference imageRef = storageRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profile.jpg");

        Bitmap bitmap = circleImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                user_img_url = downloadUrl.toString();
                saveUser();
            }
        });

    }

    @NeedsPermission({android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE})
    public void pickGallery(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)),PICK_IMAGE_REQUEST);

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

                Utils.closeDialog(RegisterActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerClientIugu(String email,String name){

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<Client> call = service.clientRegister(email,name);

        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {

                if(response.isSuccessful()){

                    Client client = response.body();
                    user.setId_iugu(client.getId());
                    mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                    getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());

                }else{

                    Toast.makeText(getApplicationContext(),"Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    ResponseBody errorBody = response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Erro na chamada ao servidor", Toast.LENGTH_SHORT);
            }
        });
    }
}
