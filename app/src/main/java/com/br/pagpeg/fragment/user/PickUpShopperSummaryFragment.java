package com.br.pagpeg.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyachi.stepview.HorizontalStepView;
import com.br.pagpeg.R;
import com.br.pagpeg.activity.user.AddCreditCardActivity;
import com.br.pagpeg.adapter.user.PickUpShopperSummaryAdapter;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.CreditCard;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.model.Shopper;
import com.br.pagpeg.model.User;
import com.br.pagpeg.notification.SendNotification;
import com.br.pagpeg.retrofit.RetrofitService;
import com.br.pagpeg.retrofit.ServiceGenerator;
import com.br.pagpeg.retrofit.model.Charge;
import com.br.pagpeg.utils.EnumStatus;
import com.br.pagpeg.utils.UserSingleton;
import com.br.pagpeg.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunolemgruber on 22/07/16.
 */

public class PickUpShopperSummaryFragment extends Fragment {

    private View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private HorizontalStepView stepView;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private Cart order;
    int count;
    private String userUid;
    private List<ProductCart> productCarts;
    private Button btnBuyOrder;
    private User user;
    private CreditCard creditCard;
    private TextView totalUser,totalShopper;

    public PickUpShopperSummaryFragment(){}

    public PickUpShopperSummaryFragment(HorizontalStepView stepView) {
        this.stepView = stepView;
    }

    public PickUpShopperSummaryFragment(HorizontalStepView stepView, String userUid) {
        this.stepView = stepView;
        this.userUid = userUid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_pickup_shopper_summary, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (productCarts == null)
            productCarts = new ArrayList<>();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Lista de produtos encontrados");

        totalUser = (TextView) view.findViewById(R.id.total_user);
        totalShopper = (TextView) view.findViewById(R.id.total_shopper);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(PickUpShopperSummaryFragment.this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        getOrder();
        stepView.setStepsViewIndicatorComplectingPosition(2);

        btnBuyOrder = (Button) view.findViewById(R.id.buy_order);
        btnBuyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openDialog(PickUpShopperSummaryFragment.this.getContext(), "Aguarde...");
                getUser(order.getUser());
            }
        });

        return view;
    }

    private PickUpShopperSummaryAdapter.PickUpSummaryOnClickListener onClickListener() {
        return new PickUpShopperSummaryAdapter.PickUpSummaryOnClickListener() {
            @Override
            public void onClickSticker(View view, int idx) {


            }
        };
    }

    private void getOrder() {

        //Utils.openDialog(PickUpShopperSummaryFragment.this.getContext(), "Carregando pedido");

        mDatabase.child("cart_online").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    order = dataSnapshot.getValue(Cart.class);
                    order.setUser(dataSnapshot.getKey());

                    for (Map.Entry<String, ProductCart> entry : order.getProducts().entrySet()) {

                        final ProductCart value = entry.getValue();

                        mDatabase.child("product").child(entry.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                count++;

                                if (dataSnapshot.hasChildren()) {
                                    Product p = dataSnapshot.getValue(Product.class);
                                    p.setName(dataSnapshot.getKey());
                                    value.setProduct(p);

                                    productCarts.add(value);
                                }

                                if (count >= order.getProducts().size()) {

                                    setTotal();

                                    mAdapter = new PickUpShopperSummaryAdapter(onClickListener(), PickUpShopperSummaryFragment.this.getContext(), productCarts);
                                    recyclerView.setAdapter(mAdapter);

                                    //Utils.closeDialog(PickUpShopperSummaryFragment.this.getContext());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("Firebase", databaseError.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", databaseError.getMessage());
            }
        });

    }

    private void getShopper(String uid) {

        mDatabase.child("shoppers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                        Shopper shopper = dataSnapshot.getValue(Shopper.class);
                        SendNotification.sendNotificationShopper(shopper.getName(), shopper.getOne_signal_key(), dataSnapshot.getKey(), "O usuário autorizou sua compra ",true);
                        mDatabase.child("cart_online").child(userUid).child("status").setValue(EnumStatus.Status.SHOPPER_PAYING.getName());

                        btnBuyOrder.setEnabled(false);
                        btnBuyOrder.setText("Aguarde um momento, shopper pagando!");
                        btnBuyOrder.setBackgroundColor(R.color.green_button);

                        Utils.closeDialog(PickUpShopperSummaryFragment.this.getContext());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", databaseError.getMessage());
            }
        });
    }

    private void getUser(String uuid){

        mDatabase.child("users").child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChildren()){
                    user = dataSnapshot.getValue(User.class);
                    getDefaultCard(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDefaultCard(String userUid) {

        mDatabase.child("credit_card/" + userUid).orderByChild("is_default").equalTo(true).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot ss: dataSnapshot.getChildren()) {
                        creditCard = ss.getValue(CreditCard.class);
                        charge();
                    }

                }else{

                    UserSingleton userSingleton = UserSingleton.getInstance();
                    Intent intent  = new Intent(PickUpShopperSummaryFragment.this.getContext(),AddCreditCardActivity.class);
                    intent.putExtra("id_client",userSingleton.getUser().getId_iugu());

                    startActivity(intent);

                    Utils.closeDialog(PickUpShopperSummaryFragment.this.getContext());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void charge(){

        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        //TODO mudar total para total_shopper
        Call<Charge> call = service.charge(creditCard.getToken(),user.getEmail(),"lista do pagpeg","1",String.valueOf(Math.round(order.getTotal() * 100)));

        call.enqueue(new Callback<Charge>() {
            @Override
            public void onResponse(Call<Charge> call, Response<Charge> response) {

                if(response.isSuccessful()){

                    Charge charge = response.body();
                    Log.i("Charge", charge.getMessage());

                    if(charge.isSuccess())
                        getShopper(order.getShopper());

                }else{
                    Toast.makeText(getContext(),"Resposta não foi sucesso", Toast.LENGTH_SHORT).show();
                    ResponseBody errorBody = response.errorBody();

                    Utils.closeDialog(PickUpShopperSummaryFragment.this.getContext());
                }
            }

            @Override
            public void onFailure(Call<Charge> call, Throwable t) {
                Toast.makeText(getContext(),"Erro na chamada ao servidor", Toast.LENGTH_SHORT);

                Utils.closeDialog(PickUpShopperSummaryFragment.this.getContext());
            }
        });

    }

    private void setTotal(){

        Double totalUserDouble = 0.0;
        Double totalShopperDouble = 0.0;

        for (ProductCart productCart:productCarts) {
            totalUserDouble = totalUserDouble + productCart.getPrice_total();
            if(productCart.getShopper_price_total() != null)
                totalShopperDouble = totalShopperDouble + productCart.getShopper_price_total();
        }

        totalShopper.setText("Shopper pedido: R$ " + totalShopperDouble);
        totalUser.setText("Pedido: R$ " + totalUserDouble);

    }
}
