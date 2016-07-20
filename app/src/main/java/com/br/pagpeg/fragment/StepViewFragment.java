package com.br.pagpeg.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyachi.stepview.HorizontalStepView;
import com.br.pagpeg.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class StepViewFragment extends Fragment{

    private View view;
    private HorizontalStepView stepView;
    private Fragment fragment;
    private Handler myhandler;
    private Toolbar toolbarMainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.step_frag, container, false);

        myhandler = new Handler();

        myhandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                fragment = new ShopperProfileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_step, fragment).commit();

                stepView.setStepsViewIndicatorComplectingPosition(1);
                toolbarMainActivity.setTitle("Achamos seu shopper ideal");

            }

        }, 10000);

        //Toolbar MainActivity
        toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setTitle("Localizando shopper");

        stepView = (HorizontalStepView) view.findViewById(R.id.step_view);
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        stepView.setStepsViewIndicatorComplectingPosition(0)
                .setStepViewTexts(list)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getActivity(), android.R.color.white))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getActivity(), R.color.uncompleted_text_color))
                .setStepViewComplectedTextColor(ContextCompat.getColor(getActivity(), android.R.color.white))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getActivity(), R.color.uncompleted_text_color))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getActivity(), R.drawable.complted))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getActivity(), R.drawable.default_icon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getActivity(), R.drawable.attention));


        fragment = new FindShopperFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_step, fragment).commit();

        return view;
    }
}
