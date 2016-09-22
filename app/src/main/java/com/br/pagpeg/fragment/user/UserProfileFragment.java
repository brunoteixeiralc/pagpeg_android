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
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.user.LoginActivity;
import com.br.pagpeg.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by brunolemgruber on 22/07/16.
 */

public class UserProfileFragment extends Fragment {

    private View view;
    private Button editProfile;
    private Fragment fragment;
    private TextView manageCC,logout,email,number,name,labelName;
    private ImageView profile_user;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_user_profile, container, false);

        profile_user = (ImageView) view.findViewById(R.id.profile_image);
        labelName = (TextView) view.findViewById(R.id.label_name);
        email = (TextView) view.findViewById(R.id.user_email);
        name = (TextView) view.findViewById(R.id.user_name);
        number = (TextView) view.findViewById(R.id.user_phone);

        final User user = (User) getArguments().getSerializable("user");
        if(user != null){
            labelName.setText(user.getName());
            email.setText(user.getEmail());
            name.setText(user.getName());
            number.setText(user.getNumber());
            if(user.getUser_img() != null){
                Glide.with(this).load(user.getUser_img()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_user);
            }

        }

        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Minha conta");
        logout = (TextView) toolbarMainActivity.findViewById(R.id.logout);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getFragmentManager().beginTransaction().remove(UserProfileFragment.this).commit();
                startActivity(new Intent(UserProfileFragment.this.getActivity(), LoginActivity.class));
            }
        });

        editProfile = (Button) view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout.setVisibility(View.GONE);
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

                logout.setVisibility(View.GONE);
                fragment = new CreditCardFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });



        return view;
    }
}
