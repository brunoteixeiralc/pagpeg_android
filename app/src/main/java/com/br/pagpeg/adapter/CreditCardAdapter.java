package com.br.pagpeg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.pagpeg.R;
import com.br.pagpeg.model.Store;

import java.util.List;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.CCViewHolder>{

    protected static final String TAG = "pagpeg";
    private final List<Store> stores;
    private CreditCardAdapter.CCOnClickListener ccOnClickListener;
    private final Context context;

    public CreditCardAdapter(CreditCardAdapter.CCOnClickListener ccOnClickListener, Context context, List<Store> stores) {
        this.context = context;
        this.stores = stores;
        this.ccOnClickListener = ccOnClickListener;
    }

    @Override
    public int getItemCount() {
        //return this.stores.size();
        return 3;
    }

    @Override
    public CreditCardAdapter.CCViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_credit_card, viewGroup, false);

        // Cria o ViewHolder
        CreditCardAdapter.CCViewHolder holder = new CreditCardAdapter.CCViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CreditCardAdapter.CCViewHolder holder, final int position) {
        // Atualiza a view
//        Friend a = friends.get(position);
//
//        holder.nome.setText(a.getNome());
//        holder.nome.setTypeface(FontUtils.getRegular(context));
//        holder.img.setImageResource(a.getImg());

        // Click
        if (ccOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ccOnClickListener.onClickSticker(holder.itemView, position); // A variável position é final
                }
            });

        }



    }

    public interface CCOnClickListener  {
        public void onClickSticker(View view, int idx);
    }


    // ViewHolder com as views
    public static class CCViewHolder extends RecyclerView.ViewHolder {

        //        public TextView nome;
//        public ImageView img;
//
//
        public CCViewHolder(View view) {
            super(view);
//            // Cria as views para salvar no ViewHolder
//            nome = (TextView) view.findViewById(R.id.nome);
//            img = (ImageView) view.findViewById(R.id.img);
//
        }
    }
}
