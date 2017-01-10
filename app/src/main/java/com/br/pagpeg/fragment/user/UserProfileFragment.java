package com.br.pagpeg.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.user.LoginActivity;
import com.br.pagpeg.model.User;
import com.br.pagpeg.utils.EnumToolBar;
import com.br.pagpeg.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by brunolemgruber on 22/07/16.
 */

public class UserProfileFragment extends Fragment {

    private View view;
    private Button editProfile,btnRegister;
    private Fragment fragment;
    private TextView manageCC,logout,email,number,name,labelName;
    private ImageView profile_user;
    private Bundle bundle;
    private Toolbar toolbar;
    private User user;
    private LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_user_profile, container, false);

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Minha conta");

        Utils.setIconBar(EnumToolBar.EDITPROFILE,toolbar);

        profile_user = (ImageView) view.findViewById(R.id.profile_image);
        labelName = (TextView) view.findViewById(R.id.label_name);
        email = (TextView) view.findViewById(R.id.user_email);
        name = (TextView) view.findViewById(R.id.user_name);
        number = (TextView) view.findViewById(R.id.user_phone);
        linearLayout = (LinearLayout) view.findViewById(R.id.ll);

        logout = (TextView) toolbar.findViewById(R.id.logout);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                getFragmentManager().beginTransaction().remove(UserProfileFragment.this).commit();
                Intent intent = new Intent(UserProfileFragment.this.getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnRegister = (Button) view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if(getArguments() != null){

            user = (User) getArguments().getSerializable("user");

            labelName.setText(user.getName());
            email.setText(user.getEmail());
            name.setText(user.getName());
            number.setText(user.getNumber());
            if(user.getUser_img() != null){
                Glide.with(this).load(user.getUser_img()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_user);
            }

            linearLayout.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);

        }else{

            linearLayout.setVisibility(View.GONE);
            btnRegister.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }

        editProfile = (Button) view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment = new EditUserProfileFragment();
                bundle = new Bundle();
                bundle.putSerializable("user",user);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).commit();
            }
        });

        manageCC = (TextView) view.findViewById(R.id.manage_cc);
        manageCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("id_client",user.getId_iugu());

                logout.setVisibility(View.GONE);
                fragment = new CreditCardFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });



        return view;
    }
}
