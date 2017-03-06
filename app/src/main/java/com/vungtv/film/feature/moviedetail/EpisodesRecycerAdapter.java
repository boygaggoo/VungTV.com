package com.vungtv.film.feature.moviedetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vungtv.film.R;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.Episode;

import java.util.ArrayList;

/**
 *
 * Created by pc on 3/3/2017.
 */

public class EpisodesRecycerAdapter extends RecyclerView.Adapter {
    private static final String TAG = EpisodesRecycerAdapter.class.getSimpleName();

    private ArrayList<Episode> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    int posChecked = 0;

    public EpisodesRecycerAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_movie_episode, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).setInfo(list.get(position).getEpsTitle());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemChecked(int pos) {
        this.posChecked = pos;
    }

    public void addItem(Episode episode) {
        list.add(episode);
        notifyItemInserted(list.size() - 1);
    }

    public void addItem(int pos, Episode episode) {
        list.add(pos, episode);
        notifyItemInserted(pos);
    }

    public void addAll(ArrayList<Episode> listx) {
        list.addAll(listx);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<Episode> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setListAndChecked(ArrayList<Episode> list, int posChecked) {
        this.posChecked = posChecked;
        this.list = list;
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public Episode getEpisode(int pos) {
        return list.get(pos);
    }

    public ArrayList<Episode> getList() {
        return list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Class view holder;
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.item_movie_episode_text);
            itemView.setOnClickListener(this);
        }

        public void setInfo(String label) {
            if (label != null) textView.setText(label);

            if (posChecked == getLayoutPosition()) {
                textView.setBackgroundResource(R.drawable.ds_touchable_bg_green_radius);
            } else {
                textView.setBackgroundResource(R.drawable.ds_touchable_bg_gray6_radius);
            }
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }
    }
}
