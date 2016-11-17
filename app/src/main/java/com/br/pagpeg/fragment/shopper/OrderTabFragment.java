package com.br.pagpeg.fragment.shopper;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.model.User;
import com.br.pagpeg.notification.SendNotification;
import com.br.pagpeg.utils.EnumStatus;
import com.br.pagpeg.utils.EnumToolBar;
import com.br.pagpeg.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by brunolemgruber on 28/07/16.
 */

public class OrderTabFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private View view;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ImageView mIconOrderCompleted;
    private Cart order;
    private Bundle bundle;
    private List<ProductCart> productCartsAll;
    private List<ProductCart> productCartNotFind;
    private List<ProductCart> productCartFind;
    private ArrayList<Fragment> mFragmentList;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_order_list, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new OrderListItemFragment());
        mFragmentList.add(new OrderListItemFoundFragment());
        mFragmentList.add(new OrderListItemNotFoundFragment());

        order = (Cart) getArguments().getSerializable("order");

        if(productCartsAll == null)
        productCartsAll = new ArrayList<>();

        if(productCartNotFind == null)
            productCartNotFind = new ArrayList<>();

        if(productCartFind == null)
            productCartFind = new ArrayList<>();

        orderList();

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Pedido Atual");
        Utils.setIconBar(EnumToolBar.SHOPPERORDER,toolbar);

        mIconOrderCompleted = (ImageView) toolbar.findViewById(R.id.ic_order_completed);
        mIconOrderCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("users").child(order.getUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User user = dataSnapshot.getValue(User.class);
                        SendNotification.sendNotificationUser(user.getName(),user.getOne_signal_key(),dataSnapshot.getKey());
                        mDatabase.child("cart_online").child(dataSnapshot.getKey()).child("status").setValue(EnumStatus.Status.WAITING_USER_APPROVE.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        int cor = getContext().getResources().getColor(R.color.white);
        tabLayout.setTabTextColors(cor, cor);
        tabLayout.addTab(tabLayout.newTab().setText("Itens"));
        tabLayout.addTab(tabLayout.newTab().setText("Achados"));
        tabLayout.addTab(tabLayout.newTab().setText("Não achados"));
        tabLayout.setOnTabSelectedListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new OrderTabAdapter(getChildFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.i(TAG, String.valueOf(position));

                orderList();

                switch (position) {
                    case 0: {
                        ((OrderListItemFragment) mFragmentList.get(0)).updateView(productCartsAll);
                        break;
                    }
                    case 1: {
                        ((OrderListItemFoundFragment) mFragmentList.get(1)).updateView(productCartFind);
                        break;
                    }
                    case 2: {
                        ((OrderListItemNotFoundFragment) mFragmentList.get(2)).updateView(productCartNotFind);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class OrderTabAdapter extends FragmentPagerAdapter {

        public OrderTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            bundle = new Bundle();

            if (position == 0) {
                bundle.putSerializable("productsCart", (Serializable) productCartsAll);
                ((OrderListItemFragment) mFragmentList.get(0)).user = order.getUser();
            } else if (position == 1) {
                bundle.putSerializable("productsCart", (Serializable) productCartFind);
                ((OrderListItemFoundFragment) mFragmentList.get(1)).user = order.getUser();
            }else if (position == 2) {
                bundle.putSerializable("productsCart", (Serializable) productCartNotFind);
                ((OrderListItemNotFoundFragment) mFragmentList.get(2)).user = order.getUser();
            }

            mFragmentList.get(position).setArguments(bundle);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void orderList(){

        productCartsAll.clear();
        productCartFind.clear();
        productCartNotFind.clear();

        for(Map.Entry<String, ProductCart> entry : order.getProducts().entrySet()) {

            final ProductCart value = entry.getValue();
            if(value.getStatus().equalsIgnoreCase(EnumStatus.Status.PRODUCT_WAITING.getName())){
                productCartsAll.add(value);
                continue;
            }
            if(value.getStatus().equalsIgnoreCase(EnumStatus.Status.PRODUCT_FIND.getName())){
                productCartFind.add(value);
                continue;
            }
            if(value.getStatus().equalsIgnoreCase(EnumStatus.Status.PRODUCT_NOT_FIND.getName())){
                productCartNotFind.add(value);
                continue;
            }
        }
    }
}
