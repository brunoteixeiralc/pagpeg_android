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

public class PickUpShopperSummaryAdapter extends RecyclerView.Adapter<PickUpShopperSummaryAdapter.PickUpSummaryViewHolder> {

    protected static final String TAG = "pagpeg";
    private List<ProductCart> products;
    private PickUpShopperSummaryAdapter.PickUpSummaryOnClickListener pickUpSummaryOnClickListener;
    private final Context context;

    public PickUpShopperSummaryAdapter(PickUpShopperSummaryAdapter.PickUpSummaryOnClickListener pickUpSummaryOnClickListener, Context context, List<ProductCart> products) {
        this.context = context;
        this.products = products;
        this.pickUpSummaryOnClickListener = pickUpSummaryOnClickListener;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public PickUpShopperSummaryAdapter.PickUpSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_summary_shopper, viewGroup, false);
        PickUpShopperSummaryAdapter.PickUpSummaryViewHolder holder = new PickUpShopperSummaryAdapter.PickUpSummaryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PickUpShopperSummaryAdapter.PickUpSummaryViewHolder holder, final int position) {

        final ProductCart pc = products.get(position);

        holder.name.setText(pc.getProduct().getName() + " " + pc.getProduct().getUnit_quantity());
        holder.price.setText("R$ " + pc.getPrice_total());
        holder.priceUnit.setText("R$ " + pc.getPrice_unit());
        holder.priceUnitShopper.setText("R$ " + pc.getShopper_price_unit());
        holder.priceShopper.setText("R$ " + pc.getShopper_price_total());
        holder.quantity.setText(String.valueOf(pc.getQuantity()) + " X");
        holder.quantityShopper.setText(String.valueOf(pc.getShopper_quantity()) + " X");
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

        // Click
        if (pickUpSummaryOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickUpSummaryOnClickListener.onClickSticker(holder.itemView, position); // A variável position é final
                }
            });

        }
    }

    public interface PickUpSummaryOnClickListener  {
        public void onClickSticker(View view, int idx);
    }


    // ViewHolder com as views
    public static class PickUpSummaryViewHolder extends RecyclerView.ViewHolder {

        public TextView name,price,priceShopper,quantity,quantityShopper,priceUnitShopper,priceUnit;
        public ImageView img;
        public ProgressBar progressBar;

        public PickUpSummaryViewHolder(View view) {
            super(view);
            quantity = (TextView) view.findViewById(R.id.quantity);
            quantityShopper = (TextView) view.findViewById(R.id.quantity_shopper);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            priceUnit = (TextView) view.findViewById(R.id.price_unit_user);
            priceUnitShopper = (TextView) view.findViewById(R.id.price_unit_shopper);
            priceShopper = (TextView) view.findViewById(R.id.price_shopper);
            img = (ImageView) view.findViewById(R.id.img);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
        }
    }
}
