package com.br.pagpeg.activity.shopper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.br.pagpeg.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductDetailActivity extends AppCompatActivity {

    private View view;
    private CardView cardView;
    private Button findIt;
    private Toolbar toolbar;
    private ImageView mIconBarCode;
    private Snackbar snackbar;
    private Fragment fragment;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_shopper_product_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhe do produto");
        mIconBarCode = (ImageView) toolbar.findViewById(R.id.ic_bar_code);
        mIconBarCode.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);

        cardView = (CardView)findViewById(R.id.cardViewAddCart);
        findIt = (Button) cardView.findViewById(R.id.findIt);
        findIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(ProductDetailActivity.this,ProductQuantityActivity.class));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        view = inflater.inflate(R.layout.content_shopper_product_detail, container, false);
//
////        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
//
////        snackbar = Snackbar
////                .make(coordinatorLayout, "Produto adicionado.", Snackbar.LENGTH_LONG)
////                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
////                .setAction("UNDO", new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        Snackbar snackbarUndo = Snackbar.make(coordinatorLayout, "Produto removido.", Snackbar.LENGTH_SHORT);
////                        snackbarUndo.show();
////                    }
////                });
//
//        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
//        toolbarMainActivity.setVisibility(View.VISIBLE);
//        toolbarMainActivity.setTitle("Detalhe do produto");
//
//        cardView = (CardView) view.findViewById(R.id.cardViewAddCart);
//        findIt = (Button) cardView.findViewById(R.id.findIt);
//        findIt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragment = new ProductQuantityFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
//            }
//        });
//
//        return view;
//    }
}
