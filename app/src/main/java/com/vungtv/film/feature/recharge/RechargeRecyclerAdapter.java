package com.vungtv.film.feature.recharge;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vungtv.film.R;
import com.vungtv.film.model.CardValue;
import com.vungtv.film.util.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by pc on 2/21/2017.
 */

public class RechargeRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = RechargeRecyclerAdapter.class.getSimpleName();
    private final Context context;
    private ArrayList<CardValue> list = new ArrayList<>();

    public RechargeRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_recharge, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).setInfo(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(CardValue object) {
        list.add(object);
        notifyItemInserted(list.size() - 1);
    }

    public void addItem(int pos, CardValue object) {
        list.add(pos, object);
        notifyItemInserted(list.size() - 1);
    }

    public void addMultiItem(ArrayList<CardValue> listData) {
        list.addAll(listData);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<CardValue> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_recharge_tv_value)
        TextView tvValue;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setInfo(CardValue cardValue) {
            if (!StringUtils.isEmpty(cardValue.money) && !StringUtils.isEmpty(cardValue.vung)) {
                tvValue.setText(String.format(
                        context.getString(R.string.recharge_text_money_vung),
                        cardValue.money,
                        cardValue.vung
                ));
            }
        }
    }
}
