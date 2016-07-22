package com.br.pagpeg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public CategoryAdapter(CategoryAdapter.CategoryOnClickListener categoryOnClickListener, Context context, List<StoreCategory> storeCategories) {
        this.context = context;
        this.storeCategories = storeCategories;
        this.categoryOnClickListener = categoryOnClickListener;
    }

    @Override
    public int getItemCount() {
        //return this.stores.size();
        return 20;
    }

    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_store_category, viewGroup, false);

        // Cria o ViewHolder
        CategoryAdapter.CategoryViewHolder holder = new CategoryAdapter.CategoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.CategoryViewHolder holder, final int position) {
        // Atualiza a view
//        Friend a = friends.get(position);
//
//        holder.nome.setText(a.getNome());
//        holder.nome.setTypeface(FontUtils.getRegular(context));
//        holder.img.setImageResource(a.getImg());

        // Click
        if (categoryOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryOnClickListener.onClickSticker(holder.itemView, position); // A variável position é final
                }
            });

        }



    }

    public interface CategoryOnClickListener  {
        public void onClickSticker(View view, int idx);
    }


    // ViewHolder com as views
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        //        public TextView nome;
//        public ImageView img;
//
//
        public CategoryViewHolder(View view) {
            super(view);
//            // Cria as views para salvar no ViewHolder
//            nome = (TextView) view.findViewById(R.id.nome);
//            img = (ImageView) view.findViewById(R.id.img);
//
        }
    }    private final Context context;
}
