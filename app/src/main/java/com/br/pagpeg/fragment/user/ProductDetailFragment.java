package com.br.pagpeg.fragment.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.user.MainUserActivity;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.utils.EnumToolBar;
import com.br.pagpeg.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductDetailFragment extends Fragment {

    private View view;
    private Button btnAddCart,btnRemoveCart;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private TextView name,price,description;
    private EditText quantity;
    private ImageView img;
    private Cart cart;
    private Product product;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_product_detail, container, false);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        name = (TextView) view.findViewById(R.id.name);
        price = (TextView) view.findViewById(R.id.price);
        description = (TextView) view.findViewById(R.id.description);
        img = (ImageView) view.findViewById(R.id.img);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        product = (Product) getArguments().getSerializable("product");

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(product.getName() + " - Detalhes");

        Utils.setIconBar(EnumToolBar.PRODUCTSDETAIL,toolbar);

        name.setText(product.getName() + " " + product.getUnit_quantity());
        price.setText("R$ " + product.getPrice());
        description.setText(product.getDescription());
        Glide.with(ProductDetailFragment.this.getContext()).load(product.getImg()).listener(new RequestListener<String, GlideDrawable>() {
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

        View dialoglayout = ProductDetailFragment.this.getActivity().getLayoutInflater().inflate(R.layout.content_alert_dialog_user, null);
        quantity = (EditText) dialoglayout.findViewById(R.id.quantity);
        final AlertDialog builder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Quantity)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancelar", null)
                .setTitle("PagPeg")
                .setMessage("Quantidade do produto")
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

                      addProduct();
                      snackbar.show();

                      builder.dismiss();
                      getFragmentManager().popBackStack();

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

        snackbar = Snackbar
                .make(coordinatorLayout, "Produto adicionado.", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        removeProduct();

                        Snackbar snackbarUndo = Snackbar.make(coordinatorLayout, "Produto removido.", Snackbar.LENGTH_SHORT);
                        snackbarUndo.show();
                    }
                });

        btnAddCart = (Button) view.findViewById(R.id.addCart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               builder.show();
               Utils.openKeyboard(ProductDetailFragment.this.getActivity());

            }
        });

        btnRemoveCart = (Button) view.findViewById(R.id.removeCart);
        btnRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.show();
                Utils.openKeyboard(ProductDetailFragment.this.getActivity());

            }
        });

        cart = (Cart) getArguments().getSerializable("cart");
        if(cart != null){
           btnAddCart.setVisibility(View.VISIBLE);
           btnRemoveCart.setVisibility(View.GONE);
        }else{
           btnAddCart.setVisibility(View.GONE);
           btnRemoveCart.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void removeProduct(){

        cart.getProductCartList().remove(product);
        cart.setCount(cart.getCount() - 1);

        ((MainUserActivity)getActivity()).mBottomBar.makeBadgeForTabAt(2, getResources().getColor(R.color.colorPrimary), cart.getCount());

    }

    private void addProduct(){

        product.setQuantity(Integer.parseInt(quantity.getText().toString()));
        ProductCart productCart = new ProductCart();
        productCart.setPrice_total(product.getQuantity() * product.getPrice());
        productCart.setQuantity(product.getQuantity());

        cart.setCount(cart.getCount() + 1);

        ((MainUserActivity)getActivity()).mBottomBar.makeBadgeForTabAt(2, getResources().getColor(R.color.colorPrimary), cart.getCount());

    }
}

