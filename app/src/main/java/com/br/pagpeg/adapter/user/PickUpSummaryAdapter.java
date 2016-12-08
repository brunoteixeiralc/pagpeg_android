package com.br.pagpeg.adapter.user;

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

public class PickUpSummaryAdapter extends RecyclerView.Adapter<PickUpSummaryAdapter.PickUpSummaryViewHolder> {

    protected static final String TAG = "pagpeg";
    private final List<Product> products;
    private PickUpSummaryAdapter.PickUpSummaryOnClickListener pickUpSummaryOnClickListener;
    private final Context context;

    public PickUpSummaryAdapter(PickUpSummaryAdapter.PickUpSummaryOnClickListener pickUpSummaryOnClickListener, Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.pickUpSummaryOnClickListener = pickUpSummaryOnClickListener;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public PickUpSummaryAdapter.PickUpSummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_cart, viewGroup, false);

        // Cria o ViewHolder
        PickUpSummaryAdapter.PickUpSummaryViewHolder holder = new PickUpSummaryAdapter.PickUpSummaryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PickUpSummaryAdapter.PickUpSummaryViewHolder holder, final int position) {

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

        public PickUpSummaryViewHolder(View view) {
            super(view);

        }
    }
}
