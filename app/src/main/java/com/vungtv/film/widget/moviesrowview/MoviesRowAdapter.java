package com.vungtv.film.widget.moviesrowview;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.vungtv.film.R;
import com.vungtv.film.interfaces.OnRecentInfoClickListener;
import com.vungtv.film.model.HotTopic;
import com.vungtv.film.model.Movie;
import com.vungtv.film.model.MovieRecent;
import com.vungtv.film.util.DrawableUtils;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;

public class MoviesRowAdapter extends RecyclerView.Adapter {
    private static final String TAG = MoviesRowAdapter.class.getSimpleName();
    public static final int ITEM_DEFAULT = 0;
    public static final int ITEM_RECOMMENT = 1;
    public static final int ITEM_RECENT = 2;
    public static final int ITEM_COMMING = 3;
    public static final int ITEM_HOT_TOPIC = 4;

    private Context context;
    private int itemType;
    private ArrayList<Object> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnRecentInfoClickListener onRecentInfoClick;

    private final int COLOR_BLUE, COLOR_YELLOW, COLOR_ORANGE, COLOR_BLUEGRAY;

    public MoviesRowAdapter(Context context, int itemType) {
        this.context = context;
        this.itemType = itemType;

        COLOR_BLUE = getColor(R.color.green);
        COLOR_YELLOW = getColor(R.color.yellow);
        COLOR_ORANGE = getColor(R.color.orange);
        COLOR_BLUEGRAY = getColor(R.color.green_dark);
    }

    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = null;

        switch (viewType) {
            case ITEM_RECENT:
                itemView = inflater.inflate(R.layout.item_widget_movies_row_recent, parent, false);
                return new ItemRecentViewHolder(itemView);

            case ITEM_COMMING:
                itemView = inflater.inflate(R.layout.item_widget_movies_row_comming, parent, false);
                return new ItemCommingViewHolder(itemView);

            case ITEM_RECOMMENT:
                itemView = inflater.inflate(R.layout.item_widget_movies_row_recommend, parent, false);
                return new ItemDefaultViewHolder(itemView);

            case ITEM_HOT_TOPIC:
                itemView = inflater.inflate(R.layout.item_widget_movies_row_hot, parent, false);
                return new ItemHotTopicViewHolder(itemView);

            default:
                itemView = inflater.inflate(R.layout.item_widget_movies_row, parent, false);
                return new ItemDefaultViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemDefaultViewHolder) {
            ((ItemDefaultViewHolder) holder).setInfo((Movie) list.get(position));
        } else if (holder instanceof ItemRecentViewHolder) {
            ((ItemRecentViewHolder) holder).setInfo((MovieRecent) list.get(position));
        } else if (holder instanceof ItemCommingViewHolder) {
            ((ItemCommingViewHolder) holder).setInfo((Movie) list.get(position));
        } else if (holder instanceof ItemHotTopicViewHolder) {
            ((ItemHotTopicViewHolder) holder).setInfo((HotTopic) list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Object object) {
        list.add(object);
        notifyItemInserted(list.size() - 1);
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public int getMovieId(int pos) {

        Object obj = list.get(pos);

        LogUtils.d(TAG, "getMovieId: " + new Gson().toJson(obj));

        if (obj instanceof Movie) {
            return ((Movie) obj).getMovId();
        } else if (obj instanceof MovieRecent) {
            return ((MovieRecent) obj).getMovId();
        }

        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnRecentInfoClick(OnRecentInfoClickListener onRecentInfoClick) {
        this.onRecentInfoClick = onRecentInfoClick;
    }

    private int getColor(int resColor) {
        return ResourcesCompat.getColor(context.getResources(), resColor, null);
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
            //LogUtils.d(TAG, movie.getMovName());
            if (movie.getMovName() != null) {
                name.setText(movie.getMovName());
            }

            // set film info episode
            if (movie.getMovNumberEpisode() > 0) {
                episode.setVisibility(View.VISIBLE);
                episode.setText(String.format(
                        "%1$s/%2$s",
                        String.valueOf(movie.getMovCountEpisoder()), String.valueOf(movie.getMovNumberEpisode())
                ));
            } else {
                episode.setVisibility(View.GONE);
            }

            infoLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            String res = movie.getRes();
            if (res != null && res.length() > 1) {
                String[] quality = res.split(",");

                for (String s : quality) {
                    if (s.equalsIgnoreCase("HD")) {
                        addViewInfo(inflater, s, COLOR_BLUE);
                    } else if (s.equalsIgnoreCase("SD")) {
                        addViewInfo(inflater, s, COLOR_YELLOW);
                    }
                }
            }

            if (movie.getLt() != null && movie.getLt().length() > 0) {
                addViewInfo(inflater, movie.getLt(), COLOR_ORANGE);
            }

            if (movie.getTm() != null && movie.getTm().length() > 0) {
                addViewInfo(inflater, movie.getTm(), COLOR_BLUEGRAY);
            }
        }

        /**
         * Add info SD, HD, LT, TM
         * @param info SD, HD, LT, TM
         * @param color bg color
         */
        private void addViewInfo(LayoutInflater inflater, String info, int color) {
            TextView textView = (TextView) inflater.inflate(
                    R.layout.item_widget_movies_row_info, infoLayout, false);
            textView.setBackground(DrawableUtils.createBgMovieInfo(context, color));
            textView.setText(info);

            infoLayout.addView(textView);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                Movie movie = (Movie) list.get(getLayoutPosition());
                onItemClickListener.onItemClick(view, movie.getMovId());
            }
        }
    }

    /**
     * Class item view holder
     */
    private class ItemRecentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private VtvTextView name;
        private View btnInfo;
        private ProgressBar progressBar;

        public ItemRecentViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.item_home_row_film_img_poster);
            name = (VtvTextView) itemView.findViewById(R.id.item_home_row_film_tv_film_name);
            btnInfo = itemView.findViewById(R.id.item_home_row_film_recent_btn_info);
            progressBar = (ProgressBar) itemView.findViewById(R.id.item_home_row_film_recent_progress);

            itemView.findViewById(R.id.item_home_row_film_icon).setOnClickListener(this);
            itemView.setOnClickListener(this);
            imageView.setOnClickListener(this);
            btnInfo.setOnClickListener(this);
            progressBar.setMax(100);
        }

        public void setInfo(final MovieRecent movie) {
            //File fileImg = ImageUtil.getFileImageRecentFromSD(movie.getMovFrameBg());
            if (movie.getMovFrameBg() != null) {
                Picasso.with(context)
                        .load(movie.getMovFrameBg())
                        .fit()
                        .error(R.drawable.default_poster)
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
                if (onRecentInfoClick != null) {
                    MovieRecent movie = (MovieRecent) list.get(getLayoutPosition());
                    onRecentInfoClick.onButtonInfoClick(movie.getMovId());
                }
            } else {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }
    }

    /**
     * Viewholder item comming;
     */
    private class ItemCommingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView poster;

        public ItemCommingViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.item_home_row_film_img_poster);

            itemView.setOnClickListener(this);
            poster.setOnClickListener(this);
        }

        public void setInfo(Movie movie) {

            String urlPoster = movie.getMovPoster();
            if (urlPoster != null && urlPoster.length() > 5) {
                Picasso.with(context)
                        .load(urlPoster.replace(" ", ""))
                        .fit()
                        .error(R.drawable.default_poster)
                        .into(poster);
            }
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                Movie movie = (Movie) list.get(getLayoutPosition());
                onItemClickListener.onItemClick(view, movie.getMovId());
            }
        }
    }

    /**
     * Viewholder item HOT;
     */
    private class ItemHotTopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView poster;

        public ItemHotTopicViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.item_home_row_film_img_poster);

            itemView.setOnClickListener(this);
        }

        public void setInfo(HotTopic hotTopic) {

            String urlPoster = hotTopic.getBanner();
            if (urlPoster != null && urlPoster.length() > 5) {
                Picasso.with(context)
                        .load(urlPoster.replace(" ", ""))
                        .fit()
                        .error(R.drawable.default_poster)
                        .into(poster);
            }
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                HotTopic hotTopic = (HotTopic) list.get(getLayoutPosition());
                onItemClickListener.onItemClick(view, hotTopic.getMovId());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int movieId);
    }
}
