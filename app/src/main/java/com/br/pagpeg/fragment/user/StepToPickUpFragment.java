package com.br.pagpeg.fragment.user;

import android.os.Bundle;
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
import com.br.pagpeg.utils.EnumStatus;
import com.br.pagpeg.utils.EnumToolBar;
import com.br.pagpeg.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class StepToPickUpFragment extends Fragment{

    private View view;
    public HorizontalStepView stepView;
    private Fragment fragment;
    private Toolbar toolbar;
    private String status = EnumStatus.Status.FINDING_SHOPPER.getName();
    private String userUid;

    public StepToPickUpFragment(){}

    public StepToPickUpFragment(String status){
        this.status = status;
    }

    public StepToPickUpFragment(String status,String userUid){
        this.status = status;
        this.userUid = userUid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_step_to_pickup, container, false);

//        myhandler = new Handler();
//
//        myhandler.postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//
//                fragment = new FindShopperProfileFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container_step, fragment).commit();
//                stepView.setStepsViewIndicatorComplectingPosition(1);
//                toolbar.setTitle("Achamos seu shopper ideal");
//                myhandler.postDelayed(new Runnable()
//
//                {
//                    @Override
//                    public void run() {
//
//                        fragment = new PickUpShopperSummaryFragment();
//                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                        transaction.replace(R.id.fragment_container_step, fragment).commit();
//                        stepView.setStepsViewIndicatorComplectingPosition(2);
//                        toolbar.setTitle("Resumo da compra do shopper");
//                        myhandler.postDelayed(new Runnable()
//
//                        {
//                           @Override
//                           public void run() {
//
//                               fragment = new PickUpReadyFragment();
//                               FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                               transaction.replace(R.id.fragment_container_step, fragment).commit();
//                               stepView.setStepsViewIndicatorComplectingPosition(3);
//                               toolbar.setTitle("Sua compra está pronta!");
//                               myhandler.postDelayed(new Runnable()
//
//                               {
//                                   @Override
//                                   public void run() {
//
//                                       fragment = new PickUpSummaryFragment();
//                                       FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                                       transaction.replace(R.id.fragment_container_step, fragment).commit();
//                                       stepView.setStepsViewIndicatorComplectingPosition(4);
//                                       toolbar.setTitle("Compra entregue.");
//                                   }
//                               },20000);
//                           }
//                        },20000);
//                    }
//                },20000);12345678
//          }
//
//        }, 20000);

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Localizando shopper");
        Utils.setIconBar(EnumToolBar.FINDINGSHOPPER,toolbar);

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

        switch (status){

            case "Finding Shopper":
                fragment = new FindShopperFragment(stepView);
                break;
            case "Shopper Buying":
                fragment = new FindShopperProfileFragment(stepView);
                break;
            case "Waiting User To Approve":
                fragment = new PickUpShopperSummaryFragment(stepView,userUid);
                break;
            default:
                break;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_step, fragment).commit();

        return view;
    }
}
