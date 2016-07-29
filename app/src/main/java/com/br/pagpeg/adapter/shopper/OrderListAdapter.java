package com.br.pagpeg.adapter.shopper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Order;

import java.util.List;

/**
 * Created by brunolemgruber on 15/07/16.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    protected static final String TAG = "pagpeg";
    private final List<Order> orders;
    private OrderListOnClickListener orderListOnClickListener;
    private final Context context;

    public OrderListAdapter(OrderListOnClickListener orderProductOnClickListener, Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.orderListOnClickListener = orderProductOnClickListener;
    }

    @Override
    public int getItemCount() {
        //return this.stores.size();
        return 20;
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order, viewGroup, false);

        // Cria o ViewHolder
        OrderListViewHolder holder = new OrderListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrderListViewHolder holder, final int position) {
        // Atualiza a view
//        Friend a = friends.get(position);
//
//        holder.nome.setText(a.getNome());
//        holder.nome.setTypeface(FontUtils.getRegular(context));
//        holder.img.setImageResource(a.getImg());

        // Click
        if (orderListOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderListOnClickListener.onClickSticker(holder.itemView, position); // A variável position é final
                }
            });

        }



    }

    public interface OrderListOnClickListener {
        public void onClickSticker(View view, int idx);
    }


    // ViewHolder com as views
    public static class OrderListViewHolder extends RecyclerView.ViewHolder {

        //        public TextView nome;
//        public ImageView img;
//
//
        public OrderListViewHolder(View view) {
            super(view);
//            // Cria as views para salvar no ViewHolder
//            nome = (TextView) view.findViewById(R.id.nome);
//            img = (ImageView) view.findViewById(R.id.img);
//
        }
    }

}
