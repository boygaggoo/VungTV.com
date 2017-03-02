package com.vungtv.film.feature.filtermovies;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.vungtv.film.R;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.Movie;
import com.vungtv.film.util.DrawableUtils;
import com.vungtv.film.util.FontUtils;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;


public class FilterMoviesAdapter extends RecyclerView.Adapter {
    private static final String TAG = FilterMoviesAdapter.class.getSimpleName();
    private static final int ITEM_TYPE_ADS = 1;
    private static final int ITEM_TYPE_DEFAULT = 0;
    private final int blue, yellow, orange, blueGray;
    private float itemWidth;
    private final int padding;
    private final float textSize;

    private Context context;
    private ArrayList<Object> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public FilterMoviesAdapter(Context context, float itemWidth) {
        this.context = context;
        this.itemWidth = itemWidth;
        blue = ResourcesCompat.getColor(context.getResources(), R.color.green, null);
        yellow = ResourcesCompat.getColor(context.getResources(), R.color.yellow, null);
        orange = ResourcesCompat.getColor(context.getResources(), R.color.orange, null);
        blueGray = ResourcesCompat.getColor(context.getResources(), R.color.green_dark, null);
        padding = context.getResources().getDimensionPixelSize(R.dimen.space_4);
        textSize = context.getResources().getDimensionPixelSize(R.dimen.font_tiny);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            return ITEM_TYPE_ADS;
        }

        return ITEM_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = null;
        switch (viewType) {
            case ITEM_TYPE_ADS:
                itemView = inflater.inflate(R.layout.item_native_adslayout, parent, false);
                return new AdsViewHolder(itemView);

            default:
                itemView = inflater.inflate(R.layout.item_filter_movies, parent, false);
                return new ItemDefaultViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemDefaultViewHolder) {
            ((ItemDefaultViewHolder) holder).setInfo((Movie) list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getItemMovieId(int pos) {
        return ((Movie)list.get(pos)).getMovId();
    }

    public ArrayList<Object> getList() {
        return list;
    }

    public void addItem(Object object) {
        list.add(object);
        notifyItemInserted(list.size() - 1);
    }

    public void addItem(int pos, Object object) {
        list.add(pos, object);
        notifyItemInserted(list.size() - 1);
    }

    public void addMultiItem(ArrayList<Movie> listData) {
        list.addAll(listData);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Viewholder item default;
     */
    private class ItemDefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private VtvTextView name, episode;
        private LinearLayout infoLayout;

        public ItemDefaultViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.item_home_row_film_img_poster);
            name = (VtvTextView) itemView.findViewById(R.id.item_home_row_film_tv_film_name);
            episode = (VtvTextView) itemView.findViewById(R.id.item_home_row_film_tv_film_episode);
            infoLayout = (LinearLayout) itemView.findViewById(R.id.item_home_row_film_layout_info);

            itemView.setOnClickListener(this);
            poster.setOnClickListener(this);
        }

        public void setInfo(Movie movie) {
            poster.getLayoutParams().width = (int) itemWidth;
            poster.getLayoutParams().height = (int) (itemWidth * 1.42);

            //LogUtils.i(TAG, "setInfo: itemWidth = " + poster.getLayoutParams().width + " item height = " + poster.getLayoutParams().height);

            name.getLayoutParams().width = (int) itemWidth;
            // set image poster
            String urlPoster = movie.getMovPoster();
            if (urlPoster != null && urlPoster.length() > 5) {
                Picasso.with(context)
                        .load(urlPoster.replace(" ", ""))
                        .fit()
                        .error(R.drawable.default_poster)
                        .into(poster);
            }

            // set film name
            if (movie.getMovName() != null) {
                name.setText(movie.getMovName());
            }

            // set film info episode
            episode.setText(String.format(
                    "%1$s/%2$s",
                    String.valueOf(movie.getMovCountEpisoder()), String.valueOf(movie.getMovNumberEpisode())
            ));

            infoLayout.removeAllViews();
            String res = movie.getRes();
            if (res != null && res.length() > 1) {
                String[] quality = res.split(",");

                for (String s : quality) {
                    if (s.equalsIgnoreCase("HD")) {
                        addViewInfo(s, blue);
                    } else if (s.equalsIgnoreCase("SD")) {
                        addViewInfo(s, yellow);
                    }
                }
            }

            if (movie.getLt() != null && movie.getLt().length() > 0) {
                addViewInfo(movie.getLt(), orange);
            }

            if (movie.getTm() != null && movie.getTm().length() > 0) {
                addViewInfo(movie.getTm(), blueGray);
            }
        }

        /**
         * Add info SD, HD, LT, TM
         * @param info SD, HD, LT, TM
         * @param color bg color
         */
        private void addViewInfo(String info, int color) {
            VtvTextView textView = new VtvTextView(itemView.getContext());
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, padding);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTextColor(Color.WHITE);
            textView.setTextStyle(FontUtils.BOLD);
            textView.setTextSize(textSize/2);
            textView.setBackground(DrawableUtils.createBgMovieInfo(context, color));
            textView.setLayoutParams(layoutParams);
            textView.setAllCaps(true);
            textView.setText(info);

            infoLayout.addView(textView);
        }

        @Override
        public void onClick(View view) {
            LogUtils.e(TAG, "openActMovieDetail movId 1 = " + getLayoutPosition());
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(view, getLayoutPosition());
        }
    }

    class AdsViewHolder extends RecyclerView.ViewHolder {
        private ViewGroup adsLayout;
        public AdsViewHolder(View itemView) {
            super(itemView);

            adsLayout = (ViewGroup) itemView.findViewById(R.id.native_adslayout_root);
        }
    }
}
