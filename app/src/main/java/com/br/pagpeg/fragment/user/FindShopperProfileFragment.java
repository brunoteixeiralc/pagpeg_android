package com.br.pagpeg.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.br.pagpeg.R;
import com.br.pagpeg.model.Shopper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class FindShopperProfileFragment extends Fragment {

    private View view;
    private Shopper shopper;
    private TextView name,email,number;
    private ImageView image;
    private RatingBar ratingBar;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private HorizontalStepView stepView;

    public FindShopperProfileFragment(HorizontalStepView stepView){
        this.stepView = stepView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_find_shopper_profile, container, false);

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Achamos seu shopper ideal");

        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        name = (TextView) view.findViewById(R.id.shopper_name);
        email = (TextView) view.findViewById(R.id.shopper_email);
        number = (TextView) view.findViewById(R.id.shopper_phone);
        image = (ImageView) view.findViewById(R.id.shopper_image);
        ratingBar = (RatingBar) view.findViewById(R.id.shopper_ratingBar);

        shopper = (Shopper) getArguments().getSerializable("shopper");

        name.setText(shopper.getName());
        number.setText(shopper.getNumber());
        email.setText(shopper.getEmail());
        ratingBar.setNumStars(shopper.getRating());
        Glide.with(FindShopperProfileFragment.this.getContext()).load(shopper.getUser_img()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(image);

        stepView.setStepsViewIndicatorComplectingPosition(1);

        return view;
    }
}
