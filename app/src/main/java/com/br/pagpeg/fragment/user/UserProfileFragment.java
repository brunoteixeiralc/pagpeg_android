package com.br.pagpeg.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.br.pagpeg.R;

/**
 * Created by brunolemgruber on 22/07/16.
 */

public class UserProfileFragment extends Fragment {

    private View view;
    private Button editProfile;
    private Fragment fragment;
    private TextView manageCC,logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_user_profile, container, false);

        //Toolbar MainActivity
        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Minha conta");
        logout = (TextView) toolbarMainActivity.findViewById(R.id.logout);
        logout.setVisibility(View.VISIBLE);

        editProfile = (Button) view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout.setVisibility(View.GONE);
                fragment = new EditUserProfileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
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
