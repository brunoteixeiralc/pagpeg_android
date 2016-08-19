package com.br.pagpeg.fragment.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.adapter.user.CartAdapter;
import com.br.pagpeg.utils.DividerItemDecoration;

/**
 * Created by brunolemgruber on 18/07/16.
 */

public class CartFragment  extends Fragment{

    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;
    private Button btnDiscount,btnBuy,btnCCManage;
    private ImageView mIconListImageView;
    private ImageView mIconMapImageView;
    private ImageView mIconBarCode;
    private ImageView mAddCreditCard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_cart, container, false);

        //Toolbar MainActivity
        Toolbar toolbarMainActivity =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbarMainActivity.setVisibility(View.VISIBLE);
        toolbarMainActivity.setTitle("Resumo do pedido (13 itens)");
        mIconMapImageView = (ImageView) toolbarMainActivity.findViewById(R.id.ic_mapStore);
        mIconListImageView = (ImageView) toolbarMainActivity.findViewById(R.id.ic_listStore);
        mIconBarCode = (ImageView) toolbarMainActivity.findViewById(R.id.ic_bar_code);
        mAddCreditCard = (ImageView) toolbarMainActivity.findViewById(R.id.ic_add_credit_card);
        mIconListImageView.setVisibility(View.GONE);
        mIconMapImageView.setVisibility(View.GONE);
        mIconBarCode.setVisibility(View.GONE);
        mAddCreditCard.setVisibility(View.GONE);

        final AlertDialog builder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Quantity)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancelar", null)
                .setTitle("PagPeg")
                .setMessage("Código do desconto")
                .setIcon(R.mipmap.ic_launcher)
                .setView(new EditText(CartFragment.this.getContext()))
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

        btnDiscount = (Button) view.findViewById(R.id.btn_discount);
        btnDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.show();
            }
        });

        btnBuy = (Button) view.findViewById(R.id.buy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment = new StepToPickUpFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        btnCCManage = (Button) view.findViewById(R.id.ccManage);
        btnCCManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment = new CreditCardFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(CartFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        mAdapter = new CartAdapter(onClickListener(),CartFragment.this.getContext(),null);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(CartFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        return  view;
    }

    private CartAdapter.CartOnClickListener onClickListener() {
        return new CartAdapter.CartOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {

                fragment = new ProductDetailFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        };
    }
}
