package com.br.pagpeg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.br.pagpeg.R;
import com.br.pagpeg.fragment.SlideFragment;
import com.github.paolorotolo.appintro.AppIntro2;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 14/07/16.
 */

public class IntroActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(SlideFragment.newInstance(R.layout.wizard1));
        addSlide(SlideFragment.newInstance(R.layout.wizard2));
        addSlide(SlideFragment.newInstance(R.layout.wizard3));

    }

    private void loadMainActivity(){

        Intent intent = new Intent(IntroActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {

        super.onSkipPressed(currentFragment);
        loadMainActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {

        super.onDonePressed(currentFragment);
        loadMainActivity();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
