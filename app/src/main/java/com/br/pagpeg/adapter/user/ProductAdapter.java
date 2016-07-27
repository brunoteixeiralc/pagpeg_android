package com.br.pagpeg.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Store;

import java.util.List;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    protected static final String TAG = "pagpeg";
    private final List<Store> stores;
    private ProductAdapter.ProductOnClickListener productOnClickListener;
    private Boolean isInCart = false;

    public ProductAdapter(ProductAdapter.ProductOnClickListener storeOnClickListener, Context context, List<Store> stores) {
        this.context = context;
        this.stores = stores;
        this.productOnClickListener = storeOnClickListener;
    }

    @Override
    public int getItemCount() {
        //return this.stores.size();
        return 20;
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_product, viewGroup, false);

        // Cria o ViewHolder
        ProductAdapter.ProductViewHolder holder = new ProductAdapter.ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.ProductViewHolder holder, final int position) {
        // Atualiza a view
//        Friend a = friends.get(position);
//
//        holder.nome.setText(a.getNome());
//        holder.nome.setTypeface(FontUtils.getRegular(context));
//        holder.img.setImageResource(a.getImg());

        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isInCart){
                    holder.cart.setImageResource(R.drawable.cart_add);
                    isInCart = false;
                }else{
                    holder.cart.setImageResource(R.drawable.cart_sucess);
                    isInCart = true;
                }

            }
        });

        // Click
        if (productOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productOnClickListener.onClickSticker(holder.itemView, position); // A variável position é final
                }
            });

        }



    }

    public interface ProductOnClickListener  {
        public void onClickSticker(View view, int idx);
    }


    // ViewHolder com as views
    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        //        public TextView nome;
        public ImageView cart;
//
//
        public ProductViewHolder(View view) {
            super(view);
//            // Cria as views para salvar no ViewHolder
//            nome = (TextView) view.findViewById(R.id.nome);
            cart = (ImageView) view.findViewById(R.id.product_cart);
//
        }
    }    private final Context context;
}
