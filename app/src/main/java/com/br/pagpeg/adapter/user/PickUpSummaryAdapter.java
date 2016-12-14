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
import com.br.pagpeg.model.ProductCart;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by brunolemgruber on 16/07/16.
 */

public class PickUpSummaryAdapter extends RecyclerView.Adapter<PickUpSummaryAdapter.PickUpSummaryViewHolder> {

    protected static final String TAG = "pagpeg";
    private List<ProductCart> products;
    private final Context context;

    public PickUpSummaryAdapter(Context context, List<ProductCart> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public PickUpSummaryAdapter.PickUpSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_summary_user, viewGroup, false);

        PickUpSummaryAdapter.PickUpSummaryViewHolder holder = new PickUpSummaryAdapter.PickUpSummaryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PickUpSummaryAdapter.PickUpSummaryViewHolder holder, final int position) {

        final ProductCart pc = products.get(position);

        holder.name.setText(pc.getProduct().getName());
        holder.price.setText("R$ " + pc.getShopper_price_unit());
        holder.priceTotal.setText("R$ " + pc.getShopper_price_total());
        holder.quantity.setText(String.valueOf(pc.getShopper_quantity()) + " X");
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

    public static class PickUpSummaryViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView name,quantity,price,priceTotal;
        private ProgressBar progressBar;

        public PickUpSummaryViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            price = (TextView) view.findViewById(R.id.price_unit);
            priceTotal = (TextView) view.findViewById(R.id.price);
            img = (ImageView) view.findViewById(R.id.img);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);

        }
    }
}
