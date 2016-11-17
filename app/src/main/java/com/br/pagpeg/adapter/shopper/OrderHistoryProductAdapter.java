package com.br.pagpeg.adapter.shopper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Cart;

import java.util.List;

/**
 * Created by brunolemgruber on 15/07/16.
 */

public class OrderHistoryProductAdapter extends RecyclerView.Adapter<OrderHistoryProductAdapter.OrderProductViewHolder> {

    protected static final String TAG = "pagpeg";
    private final List<Cart> orders;
    private OrderOProductOnClickListener orderProductOnClickListener;
    private final Context context;

    public OrderHistoryProductAdapter(OrderOProductOnClickListener orderProductOnClickListener, Context context, List<Cart> orders) {
        this.context = context;
        this.orders = orders;
        this.orderProductOnClickListener = orderProductOnClickListener;
    }

    @Override
    public int getItemCount() {
        //return this.stores.size();
        return 10;
    }

    @Override
    public OrderProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order_history_product, viewGroup, false);

        // Cria o ViewHolder
        OrderProductViewHolder holder = new OrderProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrderProductViewHolder holder, final int position) {
        // Atualiza a view
//        Friend a = friends.get(position);
//
//        holder.nome.setText(a.getNome());
//        holder.nome.setTypeface(FontUtils.getRegular(context));
//        holder.img.setImageResource(a.getImg());

        // Click
        if (orderProductOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderProductOnClickListener.onClickSticker(holder.itemView, position); // A variável position é final
                }
            });

        }



    }

    public interface OrderOProductOnClickListener  {
        public void onClickSticker(View view, int idx);
    }


    // ViewHolder com as views
    public static class OrderProductViewHolder extends RecyclerView.ViewHolder {

        //        public TextView nome;
//        public ImageView img;
//
//
        public OrderProductViewHolder(View view) {
            super(view);
//            // Cria as views para salvar no ViewHolder
//            nome = (TextView) view.findViewById(R.id.nome);
//            img = (ImageView) view.findViewById(R.id.img);
//
        }
    }

}
