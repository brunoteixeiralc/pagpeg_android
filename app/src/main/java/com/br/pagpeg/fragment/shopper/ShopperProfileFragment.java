package com.br.pagpeg.fragment.shopper;

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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Shopper;
import com.br.pagpeg.utils.EnumIconBar;
import com.br.pagpeg.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by brunolemgruber on 22/07/16.
 */

public class ShopperProfileFragment extends Fragment {

    private View view;
    private Button editProfile;
    private TextView shopperName,shopperEmail,shopperNumber,logout;
    private ImageView shopperImage;
    private Fragment fragment;
    private ProgressBar progressBar;
    private RatingBar shopperRatingBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_shopper_profile, container, false);

        Toolbar toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Minha conta");
        Utils.setIconBar(EnumIconBar.SHOPPERPROFILE,toolbar);

        shopperImage = (ImageView) view.findViewById(R.id.shopper_image);
        shopperEmail = (TextView) view.findViewById(R.id.shopper_email);
        shopperName = (TextView) view.findViewById(R.id.shopper_name);
        shopperNumber = (TextView) view.findViewById(R.id.shopper_number);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        shopperRatingBar = (RatingBar) view.findViewById(R.id.shopper_rating);

        final Shopper shopper = (Shopper) getArguments().getSerializable("shopper");

        shopperRatingBar.setRating(shopper.getRating());
        shopperNumber.setText(shopper.getNumber());
        shopperName.setText(shopper.getName());
        shopperEmail.setText(shopper.getEmail());
        Glide.with(ShopperProfileFragment.this).load(shopper.getUser_img()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(shopperImage);

        editProfile = (Button) view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("shopper",shopper);
                fragment = new EditShopperProfileFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        logout = (TextView) toolbar.findViewById(R.id.logout);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getFragmentManager().beginTransaction().remove(ShopperProfileFragment.this).commit();
                startActivity(new Intent(ShopperProfileFragment.this.getActivity(), com.br.pagpeg.activity.shopper.LoginActivity.class));
            }
        });

        return view;
    }
}
