package com.br.pagpeg.activity.shopper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.utils.EnumStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductQuantityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProductCart productCart;
    private TextView txtQuantity,name,detail,quantity;
    private EditText editQuantity;
    private ImageView img;
    private ProgressBar progressBar;
    private Button btnAddCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_shopper_product_quantity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Quantidade do produto");
        setSupportActionBar(toolbar);

        productCart = (ProductCart) getIntent().getSerializableExtra("productCart");

        btnAddCart = (Button) findViewById(R.id.addCart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productCart.setStatus(EnumStatus.Status.PRODUCT_FIND.getName());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("productCart",productCart);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        quantity = (TextView) findViewById(R.id.quantity);
        txtQuantity = (TextView) findViewById(R.id.txt_quantity);
        editQuantity = (EditText) findViewById(R.id.edit_quantity);
        name = (TextView) findViewById(R.id.name);
        detail = (TextView) findViewById(R.id.detail);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        img = (ImageView) findViewById(R.id.img);

        name.setText(productCart.getProduct().getName() + " " + productCart.getProduct().getUnit_quantity());
        detail.setText(productCart.getProduct().getUnit_measurement() + " ( " + productCart.getProduct().getShort_unit_measurement() + " )" + " , " + productCart.getProduct().getCategory());
        quantity.setText(String.valueOf(productCart.getQuantity()) + "x");
        txtQuantity.setText("/ " + String.valueOf(productCart.getQuantity()));
        editQuantity.setHint(String.valueOf(productCart.getQuantity()));
        Glide.with(this).load(productCart.getProduct().getImg()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
