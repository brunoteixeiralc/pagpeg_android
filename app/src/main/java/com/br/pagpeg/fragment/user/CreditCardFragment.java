package com.br.pagpeg.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.activity.user.AddCreditCardActivity;
import com.br.pagpeg.adapter.user.CreditCardAdapter;
import com.br.pagpeg.model.CreditCard;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.br.pagpeg.utils.EnumToolBar;
import com.br.pagpeg.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class CreditCardFragment extends Fragment {


    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ImageView mAddCreditCard;
    private List<CreditCard> creditCards = new ArrayList<>();
    private DatabaseReference mDatabase;
    private Toolbar toolbar;
    private String id_client;
    private ImageView noContentImg;
    private TextView noContentTxt;
    private LinearLayout llEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list, container, false);

        id_client = getArguments().getString("id_client");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        llEmpty = (LinearLayout) view.findViewById(R.id.ll_empty);
        noContentImg = (ImageView) view.findViewById(R.id.no_content_img);
        noContentTxt = (TextView) view.findViewById(R.id.no_content_txt);

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("Gerenciar seus cartões");

        Utils.setIconBar(EnumToolBar.ADDCREDITCARD,toolbar);

        mAddCreditCard = (ImageView) toolbar.findViewById(R.id.ic_add_credit_card);
        mAddCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(CreditCardFragment.this.getContext(),AddCreditCardActivity.class);
                intent.putExtra("id_client",id_client);

                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(CreditCardFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(CreditCardFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        mDatabase.child("credit_card/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                creditCards.clear();

                if(dataSnapshot.hasChildren()){

                    for (DataSnapshot ss: dataSnapshot.getChildren()) {
                        CreditCard creditCard = ss.getValue(CreditCard.class);
                        creditCard.setKey(ss.getKey());
                        creditCards.add(creditCard);
                    }

                    if(creditCards.size() == 0){

                        recyclerView.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                        noContentTxt.setText(R.string.no_credit_card);
                        noContentImg.setBackground(getResources().getDrawable(R.drawable.no_credit_card));

                    }else{

                        recyclerView.setVisibility(View.VISIBLE);
                        llEmpty.setVisibility(View.GONE);
                        mAdapter = new CreditCardAdapter(onClickListener(),CreditCardFragment.this.getContext(),creditCards,mDatabase);
                        recyclerView.setAdapter(mAdapter);
                    }

                }else{

                    recyclerView.setVisibility(View.GONE);
                    llEmpty.setVisibility(View.VISIBLE);
                    noContentTxt.setText(R.string.no_credit_card);
                    noContentImg.setBackground(getResources().getDrawable(R.drawable.no_credit_card));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return  view;
    }

    private CreditCardAdapter.CCOnClickListener onClickListener() {
        return new CreditCardAdapter.CCOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {

            }
        };
    }
}
