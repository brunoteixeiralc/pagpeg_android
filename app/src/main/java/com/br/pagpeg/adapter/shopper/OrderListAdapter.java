package com.br.pagpeg.adapter.shopper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.ProductCart;
import com.br.pagpeg.utils.EnumStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by brunolemgruber on 15/07/16.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    protected static final String TAG = "pagpeg";
    private OrderListOnClickListener orderListOnClickListener;
    private final Context context;
    private List<ProductCart> productsCart;

    public OrderListAdapter(OrderListOnClickListener orderProductOnClickListener, Context context, List<ProductCart> productsCart) {
        this.context = context;
        this.productsCart = productsCart;
        this.orderListOnClickListener = orderProductOnClickListener;
    }

    @Override
    public int getItemCount() {
        return productsCart.size();
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_order, viewGroup, false);

        OrderListViewHolder holder = new OrderListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final OrderListViewHolder holder, final int position) {

        ProductCart pc = productsCart.get(position);

        if(pc.getStatus().equalsIgnoreCase(EnumStatus.Status.PRODUCT_WAITING.getName())){
            holder.btnNotFind.setVisibility(View.VISIBLE);
            //holder.btnFind.setVisibility(View.GONE);
        }else if (pc.getStatus().equalsIgnoreCase(EnumStatus.Status.PRODUCT_NOT_FIND.getName())){
            //holder.btnFind.setVisibility(View.VISIBLE);
            holder.btnNotFind.setVisibility(View.GONE);
        }

        holder.name.setText(pc.getProduct().getName() + " " + pc.getProduct().getUnit_quantity());
        holder.quantity.setText(String.valueOf(pc.getQuantity()) + "x");
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

         holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderListOnClickListener.onClickItem(holder.itemView, position);
                }
         });

        holder.btnNotFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListOnClickListener.onClickButton(holder.itemView, position);
            }
        });

        holder.btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderListOnClickListener.onClickButton(holder.itemView, position);
            }
        });

    }

    public interface OrderListOnClickListener {
        public void onClickItem(View view, int idx);
        public void onClickButton(View view, int idx);
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder {

        public TextView name,quantity;
        public ImageView img;
        public ProgressBar progressBar;
        public Button btnNotFind,btnFind;

        public OrderListViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            img = (ImageView) view.findViewById(R.id.img);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            btnNotFind = (Button) view.findViewById(R.id.btn_not_find);
            btnFind = (Button) view.findViewById(R.id.btn_find);

        }
    }

}
