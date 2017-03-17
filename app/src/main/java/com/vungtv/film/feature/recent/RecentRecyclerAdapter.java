package com.vungtv.film.feature.recent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.vungtv.film.R;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.interfaces.OnRecentInfoClickListener;
import com.vungtv.film.model.MovieRecent;
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by pc on 3/8/2017.
 */

public class RecentRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = RecentRecyclerAdapter.class.getSimpleName();

    private ArrayList<MovieRecent> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    private OnRecentInfoClickListener infoClickListener;

    private int itemHeight;

    public RecentRecyclerAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_movies, parent, false);
        return new ItemRecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemRecentViewHolder) {
            ((ItemRecentViewHolder) holder).setInfo(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnRecentInfoClickListener(OnRecentInfoClickListener infoClickListener) {
        this.infoClickListener = infoClickListener;
    }

    public MovieRecent getMovieRecent(int pos) {
        return list.get(pos);
    }

    public void addItem(MovieRecent movieRecent) {
        list.add(movieRecent);
        notifyItemInserted(list.size() - 1);
    }

    public void addAllItems(List<MovieRecent> list1) {
        list.addAll(list1);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<MovieRecent> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * Class item view holder
     */
    private class ItemRecentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewGroup rootlayout;
        private ImageView imageView;
        private VtvTextView name;
        private View btnInfo;
        private ProgressBar progressBar;

        public ItemRecentViewHolder(View itemView) {
            super(itemView);

            rootlayout = (ViewGroup) itemView.findViewById(R.id.layout_root);
            imageView = (ImageView) itemView.findViewById(R.id.item_home_row_film_img_poster);
            name = (VtvTextView) itemView.findViewById(R.id.item_home_row_film_tv_film_name);
            btnInfo = itemView.findViewById(R.id.item_home_row_film_recent_btn_info);
            progressBar = (ProgressBar) itemView.findViewById(R.id.item_home_row_film_recent_progress);

            itemView.setOnClickListener(this);
            imageView.setOnClickListener(this);
            itemView.findViewById(R.id.item_home_row_film_icon).setOnClickListener(this);
            btnInfo.setOnClickListener(this);
            progressBar.setMax(100);
        }

        public void setInfo(final MovieRecent movie) {

            if (movie.getMovFrameBg() != null) {
                Picasso.with(itemView.getContext())
                        .load(movie.getMovFrameBg())
                        .resize(480, 270)
                        .error(R.drawable.default_poster_land)
                        .into(imageView);
            } else {
                Picasso.with(itemView.getContext())
                        .load(R.drawable.default_poster_land)
                        .resize(480, 270)
                        .into(imageView);
            }

            if (movie.getMovName() != null)
                name.setText(movie.getMovName());

            long progress = 0;
            if (movie.getMovDuration() > 0) {
                progress = movie.getMovLastPlay() * 100 / movie.getMovDuration();
            }
            progressBar.setProgress((int) progress);
        }

        @Override
        public void onClick(View view) {

            if (view == btnInfo) {
                if (infoClickListener != null) {
                    MovieRecent movie = list.get(getLayoutPosition());
                    infoClickListener.onButtonInfoClick(movie.getMovId());
                }
            } else {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }
    }
}
