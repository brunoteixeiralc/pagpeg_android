package com.br.pagpeg.activity.shopper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.ProductCart;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductDetailActivity extends AppCompatActivity {

    private View view;
    private CardView cardView;
    private Button findIt;
    private Toolbar toolbar;
    private ProductCart productCart;
    private TextView quantity,name,weight,category,price;
    private ImageView img;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_shopper_product_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhe do produto");
        setSupportActionBar(toolbar);

        quantity = (TextView) findViewById(R.id.quantity);
        name = (TextView) findViewById(R.id.name);
        weight = (TextView) findViewById(R.id.weight);
        category = (TextView) findViewById(R.id.category);
        price = (TextView) findViewById(R.id.price);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        img = (ImageView) findViewById(R.id.img);

        productCart = (ProductCart) getIntent().getSerializableExtra("productCart");

        name.setText(productCart.getProduct().getName() + " " + productCart.getProduct().getUnit_quantity());
        quantity.setText(String.valueOf(productCart.getQuantity()) + "x");
        price.setText("R$ " + String.valueOf(productCart.getPrice_total()));
        category.setText(productCart.getProduct().getCategory());
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

        cardView = (CardView)findViewById(R.id.cardViewAddCart);
        findIt = (Button) cardView.findViewById(R.id.findIt);
        findIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Intent intent = new Intent(ProductDetailActivity.this,ProductQuantityActivity.class);
             intent.putExtra("productCart",productCart);
             startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
