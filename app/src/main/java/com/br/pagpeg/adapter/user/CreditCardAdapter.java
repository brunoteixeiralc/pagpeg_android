package com.br.pagpeg.adapter.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.pagpeg.R;
import com.br.pagpeg.model.CreditCard;

import java.util.List;

/**
 * Created by brunolemgruber on 19/07/16.
 */

public class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.CCViewHolder>{

    private final List<CreditCard> creditCards;
    private CreditCardAdapter.CCOnClickListener ccOnClickListener;
    private final Context context;

    public CreditCardAdapter(CreditCardAdapter.CCOnClickListener ccOnClickListener, Context context, List<CreditCard> creditCards) {
        this.context = context;
        this.creditCards = creditCards;
        this.ccOnClickListener = ccOnClickListener;
    }

    @Override
    public int getItemCount() {
        return creditCards.size();
    }

    @Override
    public CreditCardAdapter.CCViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_credit_card, viewGroup, false);
        CreditCardAdapter.CCViewHolder holder = new CreditCardAdapter.CCViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CreditCardAdapter.CCViewHolder holder, final int position) {

        final CreditCard cc = creditCards.get(position);

        holder.cc_number.setText(cc.getCc_number());
        holder.cc_flag_img.setImageResource(cc.getCc_flag_img());
        if(cc.isDefault())
            holder.check.setVisibility(View.VISIBLE);
        else
            holder.check.setVisibility(View.INVISIBLE);

        if (ccOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (CreditCard ccAll:creditCards) {
                        ccAll.setDefault(false);
                    }
                    cc.setDefault(true);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface CCOnClickListener  {
        public void onClickSticker(View view, int idx);
    }

    public static class CCViewHolder extends RecyclerView.ViewHolder {

        public TextView cc_number;
        public ImageView check,cc_flag_img;

        public CCViewHolder(View view) {
            super(view);
            cc_number = (TextView) view.findViewById(R.id.cc_number);
            check = (ImageView) view.findViewById(R.id.check);
            cc_flag_img = (ImageView) view.findViewById(R.id.cc_flag_img);

        }
    }

}
