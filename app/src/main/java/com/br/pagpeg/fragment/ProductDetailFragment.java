package com.br.pagpeg.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.MainActivity;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductDetailFragment extends Fragment {

    private View view;
    private CardView cardView;
    private Button btnAddCart;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_product_detail, container, false);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        final AlertDialog builder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Quantity)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancelar", null)
                .setTitle("PagPeg")
                .setMessage("Quantidade do produto")
                .setIcon(R.mipmap.ic_launcher)
                .setView(new EditText(ProductDetailFragment.this.getContext()))
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      snackbar.show();
                      ((MainActivity)getActivity()).mBottomBar.makeBadgeForTabAt(2, getResources().getColor(R.color.colorPrimary), 200);
                      builder.dismiss();
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
                        Snackbar snackbarUndo = Snackbar.make(coordinatorLayout, "Produto removido.", Snackbar.LENGTH_SHORT);
                        snackbarUndo.show();
                    }
                });

        //Toolbar MainActivity
        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Detalhe do produto");

        cardView = (CardView) view.findViewById(R.id.cardViewAddCart);
        btnAddCart = (Button) cardView.findViewById(R.id.addCart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               builder.show();

            }
        });

        return view;
    }
}
