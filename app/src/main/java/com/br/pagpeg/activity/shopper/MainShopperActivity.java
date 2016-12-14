package com.br.pagpeg.activity.shopper;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.fragment.shopper.GraphFragment;
import com.br.pagpeg.fragment.shopper.OrderFragment;
import com.br.pagpeg.fragment.shopper.OrderHistoryFragment;
import com.br.pagpeg.fragment.shopper.RatingFragment;
import com.br.pagpeg.fragment.shopper.ShopperProfileFragment;
import com.br.pagpeg.model.Shopper;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 26/07/16.
 */

public class MainShopperActivity extends AppCompatActivity {

    public BottomBar mBottomBar;
    private Toolbar toolbar;
    private Fragment fragment;
    private TextView mLogOut;
    private Bundle bundle;
    private Shopper shopper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Seu pedido");
        setSupportActionBar(toolbar);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noNavBarGoodness();
        mBottomBar.setActiveTabColor(getResources().getColor(R.color.colorPrimary));
        mBottomBar.setItems(R.menu.bottombar_shopper_menu);
        mBottomBar.selectTabAtPosition(1,true);

        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId){

                    case R.id.bottomBarItemOne:

                        shopper = (Shopper) getIntent().getSerializableExtra("shopper");
                        bundle = new Bundle();
                        bundle.putSerializable("shopper",shopper);
                        fragment = new ShopperProfileFragment();
                        fragment.setArguments(bundle);

                        break;

                    case R.id.bottomBarItemTwo:

                        bundle = new Bundle();
                        bundle.putString("shopper_uid",getIntent().getStringExtra("shopper_uid"));
                        fragment = new OrderFragment();
                        fragment.setArguments(bundle);

                        break;

                    case R.id.bottomBarItemThree:
                        fragment = new OrderHistoryFragment();
                        break;
                    case R.id.bottomBarItemFour:
                        fragment = new GraphFragment();
                        break;
                    case R.id.bottomBarItemFive:
                        fragment = new RatingFragment();
                        break;

                }

                if(fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId){
                    case R.id.bottomBarItemOne:

                        shopper = (Shopper) getIntent().getSerializableExtra("shopper");
                        bundle = new Bundle();
                        bundle.putSerializable("shopper",shopper);
                        fragment = new ShopperProfileFragment();
                        fragment.setArguments(bundle);

                        break;
                    case R.id.bottomBarItemTwo:

                        bundle = new Bundle();
                        bundle.putString("shopper_uid",getIntent().getStringExtra("shopper_uid"));
                        fragment = new OrderFragment();
                        fragment.setArguments(bundle);

                        break;
                    case R.id.bottomBarItemThree:
                        fragment = new OrderHistoryFragment();
                        break;
                    case R.id.bottomBarItemFour:
                        fragment = new GraphFragment();
                        break;
                    case R.id.bottomBarItemFive:
                        fragment = new RatingFragment();
                        break;
                }

                if(fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

        BottomBarBadge unreadMessages = mBottomBar.makeBadgeForTabAt(1, getResources().getColor(R.color.colorPrimaryDark), 1);
        unreadMessages.setAutoShowAfterUnSelection(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
