package com.br.pagpeg.activity.shopper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private AlertDialog builder = null;

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

        weight.setText(productCart.getProduct().getUnit_measurement() + " ( " + productCart.getProduct().getShort_unit_measurement() + " )");
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

        View dialoglayout = this.getLayoutInflater().inflate(R.layout.content_alert_dialog, null);
        quantity = (EditText) dialoglayout.findViewById(R.id.quantity);
        builder = new AlertDialog.Builder(this, R.style.Dialog_Quantity)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancelar", null)
                .setTitle("PagPeg")
                .setMessage("Digite a quantidade que encontrou\n\nQuantidade pedida : " + productCart.getQuantity() + "\n")
                .setIcon(R.mipmap.ic_launcher)
                .setView(dialoglayout)
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        productCart.setShopper_find_quantity(Integer.parseInt(quantity.getText().toString()));
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("productCart",productCart);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();

                        builder.dismiss();

                        Toast.makeText(ProductDetailActivity.this, "Produto foi encontrado", Toast.LENGTH_LONG).show();

                    }
                });

                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        builder.dismiss();

                    }
                });
            }
        });

        cardView = (CardView)findViewById(R.id.cardViewAddCart);
        findIt = (Button) cardView.findViewById(R.id.findIt);
        findIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
