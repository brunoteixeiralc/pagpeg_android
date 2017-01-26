package com.br.pagpeg.fragment.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.br.pagpeg.R;
import com.br.pagpeg.activity.BarCodeActivity;
import com.br.pagpeg.activity.user.LoginActivity;
import com.br.pagpeg.activity.user.MainUserActivity;
import com.br.pagpeg.adapter.user.ProductAdapter;
import com.br.pagpeg.model.Cart;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.model.Promotion;
import com.br.pagpeg.model.PromotionProduct;
import com.br.pagpeg.model.Store;
import com.br.pagpeg.model.StoreCategory;
import com.br.pagpeg.utils.DividerItemDecoration;
import com.br.pagpeg.utils.EnumStatus;
import com.br.pagpeg.utils.EnumToolBar;
import com.br.pagpeg.utils.UserSingleton;
import com.br.pagpeg.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductListFragment extends Fragment {

    private static View view;
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Fragment fragment;
    private ImageView mIconBarCode;
    private Snackbar snackbar;
    private AlertDialog builder = null;
    private AlertDialog builderLogin = null;
    private StoreCategory category;
    private DatabaseReference mDatabase;
    private List<Product> products = new ArrayList<>();
    private Cart cart;
    private ProductCart selectedProduct;
    private EditText quantity;
    private Toolbar toolbar;
    private Store store;
    private Promotion promotion;
    private List<Promotion> promotions;
    private ImageView noContentImg;
    private TextView noContentTxt;
    private LinearLayout llEmpty;
    private CoordinatorLayout coordinatorLayout;
    private UserSingleton userSingleton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userSingleton = UserSingleton.getInstance();

        category = (StoreCategory) getArguments().getSerializable("category");
        store = (Store) getArguments().getSerializable("store");

        Utils.openDialog(ProductListFragment.this.getContext(),"Carregando produtos");

        if(cart == null){
            cart = new Cart();
            cart.setProductCartList(new ArrayList<ProductCart>());
        }

        getProducts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.list_product, container, false);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        llEmpty = (LinearLayout) view.findViewById(R.id.ll_empty);
        noContentImg = (ImageView) view.findViewById(R.id.no_content_img);
        noContentTxt = (TextView) view.findViewById(R.id.no_content_txt);

        toolbar =(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(category.getName());

        Utils.setIconBar(EnumToolBar.STOREPRODUCTS,toolbar);

        mIconBarCode = (ImageView) toolbar.findViewById(R.id.ic_bar_code);
        mIconBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(ProductListFragment.this).setCaptureActivity(BarCodeActivity.class).initiateScan();
            }
        });

        if(recyclerView == null){

            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            mLayoutManager = new LinearLayoutManager(ProductListFragment.this.getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(ProductListFragment.this.getContext(),LinearLayoutManager.VERTICAL));

        }

        snackbar = Snackbar
                .make(coordinatorLayout, "Produto adicionado.", Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        removeProduct();

                        Snackbar snackbarUndo = Snackbar.make(ProductListFragment.this.getView(), "Produto removido.", Snackbar.LENGTH_SHORT);
                        snackbarUndo.show();
                    }
                });

        View dialoglayout = ProductListFragment.this.getActivity().getLayoutInflater().inflate(R.layout.content_alert_dialog_user, null);
        quantity = (EditText) dialoglayout.findViewById(R.id.quantity);
        builder = new AlertDialog.Builder(getActivity(), R.style.Dialog_Quantity)
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

                        snackbar.show();
                        addProduct();

                        builder.dismiss();
                        quantity.setText("");
                    }
                });

                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        builder.dismiss();
                        quantity.setText("");

                    }
                });
            }
        });

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null){
                getBarCode(result.getContents());
            } else {
                Toast.makeText(getActivity(), "Produto não encontrado", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private ProductAdapter.ProductOnClickListener onClickListener() {
        return new ProductAdapter.ProductOnClickListener() {
            @Override
            public void onClick(View view, int idx) {

                Product product = products.get(idx);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product",product);
                bundle.putSerializable("cart",cart);

                fragment = new ProductDetailFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        };
    }

    private ProductAdapter.CartOnClickListener onClickListenerCart() {
        return new ProductAdapter.CartOnClickListener() {
            @Override
            public void onClick(View view, int idx) {

                if(userSingleton.getUser() != null){

                    selectedProduct = new ProductCart();
                    selectedProduct.setProduct(products.get(idx));
                    selectedProduct.setName(products.get(idx).getName());

                    if(selectedProduct.getProduct().isInCart()){

                        builder.show();
                        Utils.openKeyboard(ProductListFragment.this.getActivity());

                    }else{

                        removeProduct();

                        Snackbar snackbarUndo = Snackbar.make(coordinatorLayout, "Produto removido.", Snackbar.LENGTH_LONG);
                        snackbarUndo.show();
                    }

                }else{

                    builderLogin = new AlertDialog.Builder(getActivity(),R.style.Dialog_Quantity)
                            .setPositiveButton("OK", null)
                            .setNegativeButton("Cancelar", null)
                            .setTitle("PagPeg")
                            .setMessage("Para continuar é necessário fazer login ou se cadastrar.")
                            .setIcon(R.mipmap.ic_launcher)
                            .create();
                    builderLogin.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            final Button btnAccept = builderLogin.getButton(AlertDialog.BUTTON_POSITIVE);
                            btnAccept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    builderLogin.dismiss();
                                    startActivityForResult(new Intent(ProductListFragment.this.getActivity(),LoginActivity.class),2);
                                }
                            });

                            final Button btnDecline = builderLogin.getButton(DialogInterface.BUTTON_NEGATIVE);
                            btnDecline.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    builderLogin.dismiss();

                                }
                            });
                        }
                    });
                    builderLogin.show();
                }
            }
        };
    }

    private void getProducts(){

        mDatabase.child("product").orderByChild("category").equalTo(category.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                products.clear();

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        Product product = st.getValue(Product.class);
                        product.setName(st.getKey());
                        products.add(product);
                    }
                }

                if(products.size() != 0){

                    recyclerView.setVisibility(View.VISIBLE);
                    llEmpty.setVisibility(View.GONE);
                    mAdapter = new ProductAdapter(onClickListenerCart(),onClickListener(),ProductListFragment.this.getContext(),products,userSingleton);
                    recyclerView.setAdapter(mAdapter);

                }else{

                    recyclerView.setVisibility(View.GONE);
                    llEmpty.setVisibility(View.VISIBLE);
                    noContentTxt.setText(R.string.no_product);
                    noContentImg.setBackground(getResources().getDrawable(R.drawable.no_product));

                }

                    Utils.closeDialog(ProductListFragment.this.getContext());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addProduct(){

      selectedProduct.setQuantity(Integer.parseInt(quantity.getText().toString()));

      cart.setCount(cart.getCount() + 1);
      ((MainUserActivity)getActivity()).bottomBarBadge.setCount(cart.getCount());
      ((MainUserActivity)getActivity()).bottomBarBadge.show();

      validatePromotionNetwork();

    }

    private void removeProduct(){

        cart.getProductCartList().remove(selectedProduct);

        cart.setCount(cart.getCount() - 1);
        ((MainUserActivity)getActivity()).bottomBarBadge.setCount(cart.getCount());
        ((MainUserActivity)getActivity()).bottomBarBadge.show();

        deleteProductCart();
    }

    private void deleteProductCart(){

       mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("products").child(selectedProduct.getName()).removeValue();
           }
           @Override
             public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void saveProductCart(final Double wholesale_price){

            mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    ProductCart productCart = new ProductCart();
                    productCart.setStatus(EnumStatus.Status.PRODUCT_WAITING.getName());
                    productCart.setQuantity(selectedProduct.getQuantity());
                    productCart.setPrice_unit(wholesale_price == 0.0 ? selectedProduct.getProduct().getPrice() : wholesale_price);
                    productCart.setPrice_total(selectedProduct.getQuantity() * (wholesale_price == 0.0 ? selectedProduct.getProduct().getPrice() : wholesale_price));

                    mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("products").child(selectedProduct.getProduct().getName()).setValue(productCart);
                    mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("store").setValue(store.getKeyStore());
                    mDatabase.child("cart_online").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("network").setValue(store.getNetwork());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    private void getBarCode(String code){

        mDatabase.child("product").orderByChild("bar_code").equalTo(Long.parseLong(code)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        selectedProduct = new ProductCart();
                        selectedProduct.setProduct(st.getValue(Product.class));
                        selectedProduct.setName(st.getKey());

                        builder.show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validatePromotionNetwork(){

        mDatabase.child("promotions").child(store.getNetwork()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                promotions = new ArrayList<Promotion>();
                java.util.Date date = new java.util.Date();

                if (dataSnapshot.hasChildren()) {

                    for (DataSnapshot st : dataSnapshot.getChildren()) {

                        promotion = st.getValue(Promotion.class);
                        promotion.setKey(st.getKey());
                        boolean isValidate = validateDates((date.getTime()/1000),promotion.getStart(),promotion.getEnd());
                        if(isValidate)
                            promotions.add(promotion);
                    }

                    if(promotions.size() != 0) {

                       for (Promotion p : promotions) {
                           validatePromotionProduct(p.getKey());
                       }

                    }else{
                        saveProductCart(0.0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validatePromotionProduct(String keyName){

        mDatabase.child("promotion_product").child(store.getNetwork()).child(keyName).child(selectedProduct.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                java.util.Date date = new java.util.Date();

                if (dataSnapshot.exists()) {

                        PromotionProduct promotionProduct = dataSnapshot.getValue(PromotionProduct.class);
                        boolean isValidate = validateDates((date.getTime()/1000),promotionProduct.getStart(),promotionProduct.getEnd());
                        if(isValidate){

                            if(selectedProduct.getQuantity() >= promotionProduct.getQuantity()){

                                cart.getProductCartList().add(selectedProduct);
                                saveProductCart(selectedProduct.getProduct().getWholesale_price());

                            }else{
                                saveProductCart(0.0);
                            }
                        }

                }else{
                    saveProductCart(0.0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private boolean validateDates(Long dateNow, Long date1, Long date2){

        Date dNow = new Date(dateNow * 1000);
        Date d1 = new Date(date1 * 1000);
        Date d2 = new Date(date2 * 1000);

        Calendar calNow = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        calNow.setTime(dNow);
        cal1.setTime(d1);
        cal2.setTime(d2);

        if(calNow.after(cal1) || calNow.equals(cal1)){
            if(calNow.before(cal2) || calNow.equals(cal2)){
                return true;
            }
        }
        return false;
    }
}
