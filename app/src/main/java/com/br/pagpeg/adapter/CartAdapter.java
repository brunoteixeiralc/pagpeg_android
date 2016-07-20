package com.br.pagpeg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Product;

import java.util.List;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    protected static final String TAG = "pagpeg";
    private final List<Product> products;
    private CartAdapter.CartOnClickListener cartOnClickListener;
    private Boolean isInCart = false;
    private final Context context;

    public CartAdapter(CartAdapter.CartOnClickListener cartOnClickListener, Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.cartOnClickListener = cartOnClickListener;
    }

    @Override
    public int getItemCount() {
        //return this.stores.size();
        return 10;
    }

    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, viewGroup, false);

        // Cria o ViewHolder
        CartAdapter.CartViewHolder holder = new CartAdapter.CartViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CartAdapter.CartViewHolder holder, final int position) {
        // Atualiza a view
//        Friend a = friends.get(position);
//
//        holder.nome.setText(a.getNome());
//        holder.nome.setTypeface(FontUtils.getRegular(context));
//        holder.img.setImageResource(a.getImg());

//        holder.cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                if(isInCart){
////                    holder.cart.setImageResource(R.drawable.cart_add);
////                    isInCart = false;
////                }else{
////                    holder.cart.setImageResource(R.drawable.cart_add_sucess);
////                    isInCart = true;
////                }
//
//            }
//        });

        // Click
        if (cartOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartOnClickListener.onClickSticker(holder.itemView, position); // A variável position é final
                }
            });

        }



    }

    public interface CartOnClickListener  {
        public void onClickSticker(View view, int idx);
    }


    // ViewHolder com as views
    public static class CartViewHolder extends RecyclerView.ViewHolder {

        //        public TextView nome;
        //public ImageView cart;
//
//
        public CartViewHolder(View view) {
            super(view);
//            // Cria as views para salvar no ViewHolder
//            nome = (TextView) view.findViewById(R.id.nome);
            //cart = (ImageView) view.findViewById(R.id.product_cart);
//
        }
    }
}
