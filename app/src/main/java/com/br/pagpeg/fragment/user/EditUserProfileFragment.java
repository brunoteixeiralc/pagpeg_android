package com.br.pagpeg.fragment.user;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.User;
import com.br.pagpeg.utils.EnumIconBar;
import com.br.pagpeg.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;

/**
 * Created by brunolemgruber on 22/07/16.
 */
@RuntimePermissions
public class EditUserProfileFragment extends Fragment {

    private View view;
    private EditText email,name,number;
    private TextView changePhoto;
    private ImageView profile_user;
    private Button btnUpdate;
    private DatabaseReference mDatabase;
    private User user;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmapUserImage =  null;
    private CircleImageView circleImageView;
    private String user_img_url;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_edit_user_profile, container, false);

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Editar Perfil");

        Utils.setIconBar(EnumIconBar.EDITPROFILE,toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        circleImageView = (CircleImageView) view.findViewById(R.id.user_image);
        number = (EditText) view.findViewById(R.id.user_number);
        email = (EditText) view.findViewById(R.id.user_email);
        name = (EditText) view.findViewById(R.id.user_name);
        changePhoto = (TextView) view.findViewById(R.id.change_image);
        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickGallery();
            }
        });

        btnUpdate = (Button) view.findViewById(R.id.edit_profile);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Utils.openDialog(EditUserProfileFragment.this.getContext(),"Atualizando...");

               if(bitmapUserImage != null){
                   saveImageStorage();
               }else{
                   updateUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
               }

            }
        });

        user = (User) getArguments().getSerializable("user");
        if(user != null){
            email.setText(user.getEmail());
            name.setText(user.getName());
            number.setText(user.getNumber());
            if(user.getUser_img() != null){
                Glide.with(this).load(user.getUser_img()).diskCacheStrategy(DiskCacheStrategy.ALL).into(circleImageView);
            }

        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EditUserProfileFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                bitmapUserImage = MediaStore.Images.Media.getBitmap(EditUserProfileFragment.this.getContext().getContentResolver(), uri);
                circleImageView.setImageBitmap(bitmapUserImage);
                circleImageView.setDrawingCacheEnabled(true);
                circleImageView.buildDrawingCache();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void pickGallery(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)),PICK_IMAGE_REQUEST);

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
                updateUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

    }

    private void updateUser(String uid){

        user = new User(name.getText().toString(),number.getText().toString(),email.getText().toString(),user.getUser_img() != null ? user.getUser_img() : "",user.getDevice_id());
        if(!user_img_url.isEmpty()){
            user.setUser_img(user_img_url);
        }

        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user",user);

        Fragment fragment = new UserProfileFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commit();

        Utils.closeDialog(EditUserProfileFragment.this.getContext());
    }
}
