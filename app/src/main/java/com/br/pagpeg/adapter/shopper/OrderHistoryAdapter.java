package com.br.pagpeg.adapter.shopper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Cart;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by brunolemgruber on 15/07/16.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    protected static final String TAG = "pagpeg";
    private final List<Cart> orders;
    private OrderOnClickListener orderOnClickListener;
    private final Context context;

    public OrderHistoryAdapter(OrderOnClickListener orderOnClickListener, Context context, List<Cart> orders) {
        this.context = context;
        this.orders = orders;
        this.orderOnClickListener = orderOnClickListener;
    }

    @Override
    public int getItemCount() {
        return this.orders.size();
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order_history, viewGroup, false);

        OrderViewHolder holder = new OrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrderViewHolder holder, final int position) {

        Cart odr = orders.get(position);

        holder.progressBar.setVisibility(View.VISIBLE);
        holder.name.setText(odr.getStoreDetail().getName());
        holder.address.setText(odr.getStoreDetail().getAddress());
        Glide.with(context).load(odr.getStoreDetail().getImg()).listener(new RequestListener<String, GlideDrawable>() {
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

        if (orderOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderOnClickListener.onClickSticker(holder.itemView, position);
                }
            });
        }
    }

    public interface OrderOnClickListener  {
        public void onClickSticker(View view, int idx);
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView name,address,date;
        public ImageView img;
        public ProgressBar progressBar;

        public OrderViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.address);
            img = (ImageView) view.findViewById(R.id.img);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

}
