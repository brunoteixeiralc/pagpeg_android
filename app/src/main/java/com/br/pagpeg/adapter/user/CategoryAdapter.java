package com.br.pagpeg.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.StoreCategory;

import java.util.List;

/**
 * Created by brunolemgruber on 15/07/16.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    protected static final String TAG = "pagpeg";
    private final List<StoreCategory> storeCategories;
    private CategoryAdapter.CategoryOnClickListener categoryOnClickListener;
    private Context context;

    public CategoryAdapter(CategoryAdapter.CategoryOnClickListener categoryOnClickListener, Context context, List<StoreCategory> storeCategories) {
        this.context = context;
        this.storeCategories = storeCategories;
        this.categoryOnClickListener = categoryOnClickListener;
    }

    @Override
    public int getItemCount() {
        return storeCategories.size();
    }

    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_store_category, viewGroup, false);
        CategoryAdapter.CategoryViewHolder holder = new CategoryAdapter.CategoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.CategoryViewHolder holder, final int position) {

        StoreCategory sc = storeCategories.get(position);
        holder.name.setText(sc.getName());

        if (categoryOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryOnClickListener.onClickSticker(holder.itemView, position);
                }
            });

        }
    }

    public interface CategoryOnClickListener  {
        public void onClickSticker(View view, int idx);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public CategoryViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
        }

    }
}
