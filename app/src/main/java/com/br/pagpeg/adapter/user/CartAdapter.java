package com.br.pagpeg.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Product;
import com.br.pagpeg.model.ProductCart;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    protected static final String TAG = "pagpeg";
    private final List<ProductCart> products;
    private CartAdapter.CartOnClickListener cartOnClickListener;
    private CartAdapter.RemoveCartOnClickListener removeCartOnClickListener;
    private Boolean isInCart = false;
    private final Context context;
    private DatabaseReference mDatabase;

    public CartAdapter(CartAdapter.CartOnClickListener cartOnClickListener,CartAdapter.RemoveCartOnClickListener removeCartOnClickListener,Context context, List<ProductCart> products,DatabaseReference mDatabase) {
        this.context = context;
        this.products = products;
        this.cartOnClickListener = cartOnClickListener;
        this.removeCartOnClickListener = removeCartOnClickListener;
        this.mDatabase = mDatabase;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_cart, viewGroup, false);
        CartAdapter.CartViewHolder holder = new CartAdapter.CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CartAdapter.CartViewHolder holder, final int position) {

        final ProductCart pc = products.get(position);

        getProductDetail(pc,holder);

        if (cartOnClickListener != null) {
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartOnClickListener.onClick(holder.img, pc.getProduct());
                }
            });

        }

        if (removeCartOnClickListener != null) {
            holder.cartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCartOnClickListener.onClick(holder.cartImg, pc);
                }
            });

        }
    }

    public interface CartOnClickListener  {
        public void onClick(View view, Product product);
    }

    public interface RemoveCartOnClickListener  {
        public void onClick(View view, ProductCart productCart);
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder {

        public TextView quantity,price,name;
        public ImageView img,cartImg;
        public ProgressBar progressBar;

        public CartViewHolder(View view) {
            super(view);
            quantity = (TextView) view.findViewById(R.id.quantity);
            price = (TextView) view.findViewById(R.id.price);
            name = (TextView) view.findViewById(R.id.name);
            img = (ImageView) view.findViewById(R.id.img);
            cartImg = (ImageView) view.findViewById(R.id.delete_cart);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);

        }
    }

    private void getProductDetail(final ProductCart pc, final CartAdapter.CartViewHolder holder){

        mDatabase.child("product").child(pc.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Product product = dataSnapshot.getValue(Product.class);
                product.setName(pc.getName());
                pc.setProduct(product);

                holder.name.setText(pc.getName() + " " + pc.getProduct().getUnit_quantity());
                holder.price.setText("R$ " + pc.getPrice_total());
                holder.quantity.setText(String.valueOf(pc.getQuantity()) + " X");
                Glide.with(context).load(pc.getProduct().getImg()).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
