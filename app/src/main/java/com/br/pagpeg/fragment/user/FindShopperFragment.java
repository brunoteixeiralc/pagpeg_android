package com.br.pagpeg.fragment.user;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyachi.stepview.HorizontalStepView;
import com.br.pagpeg.R;
import com.br.pagpeg.model.Shopper;
import com.br.pagpeg.notification.SendNotification;
import com.br.pagpeg.utils.EnumStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class FindShopperFragment extends Fragment {

    private View view;
    private DatabaseReference mDatabase;
    private List<Shopper> shoppers;
    private Fragment fragment;
    private HorizontalStepView stepView;

    public FindShopperFragment(){}

    public FindShopperFragment(HorizontalStepView stepView){
        this.stepView = stepView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_find_shopper, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(shoppers == null)
            shoppers = new ArrayList<>();

        getShopper();

        return view;
    }

    private void getShopper(){

        mDatabase.child("shoppers").orderByChild("is_free").equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                shoppers.clear();

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        Shopper shopper = st.getValue(Shopper.class);
                        shopper.setKey(st.getKey());
                        shoppers.add(shopper);
                    }
                }

                if(shoppers.size() != 0){

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shopper",shoppers.get(0));

                    SendNotification.sendNotificationShopper(shoppers.get(0).getName(),shoppers.get(0).getOne_signal_key(),shoppers.get(0).getKey(),"Temos um novo pedido para você ",true);

                    fragment = new FindShopperProfileFragment(stepView);
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container_step, fragment).commit();

                    mDatabase.child("shoppers").child(shoppers.get(0).getKey()).child("is_free").setValue(false);
                    mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("shopper").setValue(shoppers.get(0).getKey());
                    mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(EnumStatus.Status.SHOPPER_BUYING.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
