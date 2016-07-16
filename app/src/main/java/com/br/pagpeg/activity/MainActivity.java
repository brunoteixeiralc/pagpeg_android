package com.br.pagpeg.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.fragment.MapFragment;
import com.br.pagpeg.fragment.StoreListFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 14/07/16.
 */

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;
    private Toolbar toolbar;
    private Fragment fragment;
    private ImageView mIconListImageView;
    private ImageView mIconMapImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Lojas nas proximidades");
        setSupportActionBar(toolbar);

        mIconMapImageView = (ImageView) toolbar.findViewById(R.id.ic_mapStore);
        mIconMapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new MapFragment();
                if(fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    mIconMapImageView.setVisibility(View.GONE);
                    mIconListImageView.setVisibility(View.VISIBLE);
                }
            }
        });
        mIconListImageView = (ImageView) toolbar.findViewById(R.id.ic_listStore);
        mIconListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new StoreListFragment();
                if(fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    mIconMapImageView.setVisibility(View.VISIBLE);
                    mIconListImageView.setVisibility(View.GONE);
                }

            }
        });

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setActiveTabColor(getResources().getColor(R.color.colorPrimary));
        mBottomBar.useFixedMode();
        mBottomBar.noTopOffset();

        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId){
                    case R.id.bottomBarItemOne:
                        toolbar.setTitle("Bruno Lemgruber");
                        //fragment = new BarCodeFragment();
                        break;
                    case R.id.bottomBarItemTwo:
                        toolbar.setTitle("Lojas nas proximidades");
                        fragment = new MapFragment();
                        break;
                    case R.id.bottomBarItemThree:
                        toolbar.setTitle("Carrinho");
                        //fragment = new ComprasFragment();
                        break;
                }

                if(fragment != null)
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId){
                    case R.id.bottomBarItemOne:
                        //fragment = new BarCodeFragment();
                        break;
                    case R.id.bottomBarItemTwo:
                        fragment = new MapFragment();
                        break;
                    case R.id.bottomBarItemThree:
                        //fragment = new ComprasFragment();
                        break;
                    default:break;
                }

                if(fragment != null)
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
            }
        });

        mBottomBar.selectTabAtPosition(1,true);
        BottomBarBadge unreadMessages = mBottomBar.makeBadgeForTabAt(2, getResources().getColor(R.color.colorPrimary), 13);
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
