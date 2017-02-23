package com.vungtv.film.feature.buyvip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vungtv.film.R;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.PackageVip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by pc on 2/21/2017.
 */

public class BuyVipRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = BuyVipRecyclerAdapter.class.getSimpleName();
    private static final int TYPE_BLUE = 0;
    public static final int TYPE_ORANGE = 1;

    private final Context context;
    private ArrayList<PackageVip> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public BuyVipRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = null;
        switch (viewType) {
            case TYPE_BLUE:
                itemView = inflater.inflate(R.layout.item_buy_vip_blue, parent, false);
                return new ItemViewHolder(itemView);

            default:
                itemView = inflater.inflate(R.layout.item_buy_vip_orange, parent, false);
                return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).setInfo(list.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 1) {
            return TYPE_BLUE;
        }

        if ((position/2) % 2 >= 1) {
            return TYPE_ORANGE;
        }

        return TYPE_BLUE;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addItem(PackageVip object) {
        list.add(object);
        notifyItemInserted(list.size() - 1);
    }

    public void addItem(int pos, PackageVip object) {
        list.add(pos, object);
        notifyItemInserted(list.size() - 1);
    }

    public void addMultiItem(ArrayList<PackageVip> listData) {
        list.addAll(listData);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<PackageVip> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_buy_vip_tv_day)
        TextView tvVipDay;

        @BindView(R.id.item_buy_vip_tv_money)
        TextView tvVipMoney;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setInfo(PackageVip packageVip) {

            tvVipDay.setText(String.format(
                    context.getString(R.string.buyvip_package_day),
                    String.valueOf(packageVip.getDay()))
            );

            tvVipMoney.setText(String.format(
                    context.getString(R.string.buyvip_package_money),
                    String.valueOf(packageVip.getMoney()))
            );
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }
    }
}
