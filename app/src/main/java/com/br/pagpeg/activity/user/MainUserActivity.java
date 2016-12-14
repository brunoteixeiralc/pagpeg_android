package com.br.pagpeg.activity.user;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.br.pagpeg.R;
import com.br.pagpeg.fragment.user.CartFragment;
import com.br.pagpeg.fragment.user.MapFragment;
import com.br.pagpeg.fragment.user.StepToPickUpFragment;
import com.br.pagpeg.fragment.user.UserProfileFragment;
import com.br.pagpeg.model.User;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 14/07/16.
 */

public class MainUserActivity extends AppCompatActivity {

    public BottomBar mBottomBar;
    private Toolbar toolbar;
    private Fragment fragment;
    private Bundle bundle;
    public BottomBarBadge bottomBarBadge;
    private String userUid;
    private String status;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Lojas nas proximidades");
        setSupportActionBar(toolbar);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noNavBarGoodness();
        mBottomBar.setActiveTabColor(getResources().getColor(R.color.colorPrimary));
        mBottomBar.setItems(R.menu.bottombar_user_menu);

        //usuário clicou na notificação
        userUid = getIntent().getStringExtra("user_uid");
        status = getIntent().getStringExtra("status");

        if(userUid == null){
            mBottomBar.selectTabAtPosition(1,true);
        }else{
            mBottomBar.selectTabAtPosition(2,true);
        }

        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId){

                    case R.id.bottomBarItemOne:

                        user = (User) getIntent().getSerializableExtra("user");
                        bundle = new Bundle();
                        bundle.putSerializable("user",user);
                        fragment = new UserProfileFragment();
                        fragment.setArguments(bundle);

                        break;

                    case R.id.bottomBarItemTwo:

                        fragment = new MapFragment();

                        break;

                    case R.id.bottomBarItemThree:

                        if(userUid == null){
                            fragment = new CartFragment();
                        }else{
                            fragment = new StepToPickUpFragment(status,userUid);
                        }

                        break;
                }

                if(fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId){
                    case R.id.bottomBarItemOne:

                        user = (User) getIntent().getSerializableExtra("user");
                        bundle = new Bundle();
                        bundle.putSerializable("user",user);
                        fragment = new UserProfileFragment();
                        fragment.setArguments(bundle);

                        break;
                    case R.id.bottomBarItemTwo:

                        fragment = new MapFragment();

                        break;
                    case R.id.bottomBarItemThree:

                        if(userUid == null){
                            fragment = new CartFragment();
                        }else{
                            fragment = new StepToPickUpFragment(status,userUid);
                        }

                        break;

                    default:break;
                }

                if(fragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

        bottomBarBadge = mBottomBar.makeBadgeForTabAt(2, getResources().getColor(R.color.colorPrimary), 0);
        bottomBarBadge.setAutoShowAfterUnSelection(true);
        bottomBarBadge.show();
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
