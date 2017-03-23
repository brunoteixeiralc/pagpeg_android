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
import com.br.pagpeg.utils.UserSingleton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    protected static final String TAG = "pagpeg";
    private final List<Product> products;
    private ProductAdapter.ProductOnClickListener productOnClickListener;
    private ProductAdapter.CartOnClickListener cartOnClickListener;
    private final Context context;
    private UserSingleton userSingleton;

    public ProductAdapter(ProductAdapter.CartOnClickListener cartOnClickListener,ProductAdapter.ProductOnClickListener storeOnClickListener, Context context, List<Product> products,UserSingleton userSingleton) {
        this.context = context;
        this.products = products;
        this.productOnClickListener = storeOnClickListener;
        this.cartOnClickListener = cartOnClickListener;
        this.userSingleton = userSingleton;
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_product, viewGroup, false);
        ProductAdapter.ProductViewHolder holder = new ProductAdapter.ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.ProductViewHolder holder, final int position) {

       final Product p = products.get(position);

       if(p.isInCart()){
           holder.cart.setImageResource(R.drawable.cart_sucess);
       }else{
           holder.cart.setImageResource(R.drawable.cart_add);
       }
       holder.name.setText(p.getName() + " " + p.getUnit_quantity());
       holder.price.setText("R$ " + p.getPrice());
       Glide.with(context).load(p.getImg()).listener(new RequestListener<String, GlideDrawable>() {
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

        if (productOnClickListener != null) {
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productOnClickListener.onClick(holder.img, position);
                }
            });

        }

        if (cartOnClickListener != null) {
            holder.cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   if(userSingleton.getUser() != null){

                       if(p.isInCart()){
                           holder.cart.setImageResource(R.drawable.cart_add);
                           p.setInCart(false);
                       }else{
                           holder.cart.setImageResource(R.drawable.cart_sucess);
                           p.setInCart(true);
                       }
                   }

                    cartOnClickListener.onClick(holder.cart, position);
                }
            });
        }
    }

    public interface ProductOnClickListener  {
        public void onClick(View view, int idx);
    }

    public interface CartOnClickListener  {
        public void onClick(View view, int idx);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView name,price;
        public ImageView img,cart;
        public ProgressBar progressBar;

        public ProductViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            cart = (ImageView) view.findViewById(R.id.product_cart);
            img = (ImageView) view.findViewById(R.id.img);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }
}
