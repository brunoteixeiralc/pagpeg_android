package com.br.pagpeg.fragment.shopper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.ProductCart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_order_list, container, false);

        order = (Cart) getArguments().getSerializable("order");

        if(productCartsAll == null)
            productCartsAll = new ArrayList<>();

        for(Map.Entry<String, ProductCart> entry : order.getProducts().entrySet()) {

            final ProductCart value = entry.getValue();
            productCartsAll.add(value);

        }

        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Pedido Atual");
        mIconOrderCompleted = (ImageView) toolbarMainActivity.findViewById(R.id.ic_order_completed);
        mIconOrderCompleted.setVisibility(View.VISIBLE);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        int cor = getContext().getResources().getColor(R.color.white);
        tabLayout.setTabTextColors(cor, cor);
        tabLayout.addTab(tabLayout.newTab().setText("Itens"));
        tabLayout.addTab(tabLayout.newTab().setText("Achados"));
        tabLayout.addTab(tabLayout.newTab().setText("Não achados"));
        tabLayout.setOnTabSelectedListener(this);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new OrderTabAdapter(getChildFragmentManager()));

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

        private Fragment fragment;

        public OrderTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {

                bundle = new Bundle();
                bundle.putSerializable("productsCart", (Serializable) productCartsAll);
                fragment = new OrderListItemFragment();
                fragment.setArguments(bundle);

            } else if (position == 1) {

                fragment = new OrderListItemFoundFragment();

            }else if (position == 2) {

                fragment = new OrderListItemNotFoundFragment();

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
