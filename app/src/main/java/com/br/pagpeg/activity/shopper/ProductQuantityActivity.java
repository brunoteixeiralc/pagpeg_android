package com.br.pagpeg.activity.shopper;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.br.pagpeg.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductQuantityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView mIconBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_shopper_product_quantity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Quantidade do produto");
        mIconBarCode = (ImageView) toolbar.findViewById(R.id.ic_bar_code);
        mIconBarCode.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
