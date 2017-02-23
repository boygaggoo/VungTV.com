package com.vungtv.film.widget.moviesrowview;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vungtv.film.R;
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;

import static com.vungtv.film.widget.moviesrowview.MoviesRowAdapter.ITEM_DEFAULT;

public class VtvMoviesRowView extends LinearLayout{
    public static final int ICON_TITLE_ROLL = R.drawable.icon_filmroll;
    public static final int ICON_TITLE_LA = R.drawable.icon_lavung;
    public static final int ICON_TITLE_STAR = R.drawable.icon_star1;
    private static final int ICON_MORE_BLUE = R.drawable.icon_xemthem2;
    private static final int ICON_MORE_GRAY = R.drawable.icon_xemthem1;

    public static final int COLOR_WHITE = R.color.white;
    public static final int COLOR_GRAY = R.color.gray_2;
    public static final int COLOR_BLUE = R.color.green;
    public static final int COLOR_BLACK = R.color.colorPrimary;

    public static final int STYLE_DEFAULT = 0;
    public static final int STYLE_BLACK = 1;
    public static final int STYLE_GRAY = 2;
    public static final int STYLE_BLACK_NO_TITLE = 3;
    public static final int STYLE_BLACK_NO_FILMNAME = 4;

    private MoviesRowAdapter adapter;
    private ViewGroup rootLayout;
    private ViewGroup titleLayout;
    private ImageView iconTitle;
    private VtvTextView tvTitle;
    private RecyclerView recyclerView;
    private View btnViewMore;
    private View divider;
    private View lineTop;
    private View lineBottom;

    private int style = STYLE_DEFAULT;
    private int itemType = ITEM_DEFAULT;
    private int iconTitleRes = ICON_TITLE_ROLL;
    private String title = "";

    public VtvMoviesRowView(Context context) {
        super(context);
        init(null);
    }

    public VtvMoviesRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VtvMoviesRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvMoviesRowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.VtvMoviesRowView, 0, 0);
            try {
                style = a.getInt(R.styleable.VtvMoviesRowView_style, STYLE_DEFAULT);
                itemType = a.getInt(R.styleable.VtvMoviesRowView_itemType, ITEM_DEFAULT);
                iconTitleRes = a.getResourceId(R.styleable.VtvMoviesRowView_iconTitle, ICON_TITLE_ROLL);
                title = a.getString(R.styleable.VtvMoviesRowView_title);
            } finally {
                a.recycle();
            }
        }

        retrieverViews();
        setStyle(style);
        setIconTitle(iconTitleRes);
        setTitle(title);
    }

    protected void retrieverViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_movies_row, this);

        rootLayout = (ViewGroup) findViewById(R.id.widget_movies_row_root);
        titleLayout = (ViewGroup) findViewById(R.id.widget_movies_row_title_layout);
        iconTitle = (ImageView) findViewById(R.id.widget_movies_row_icon);
        tvTitle = (VtvTextView) findViewById(R.id.widget_movies_row_title);
        recyclerView = (RecyclerView) findViewById(R.id.widget_movies_row_recyclerView);
        btnViewMore = findViewById(R.id.widget_movies_row_viewmore);
        divider = findViewById(R.id.divider_view_blue);
        lineTop = findViewById(R.id.widget_movies_row_line_top);
        lineBottom = findViewById(R.id.widget_movies_row_line_bottom);
    }

    public void setup(int itemType, ArrayList<Object> listData, int style, int resIconTitle, String srtTitle) {
        this.itemType = itemType;
        setStyle(style);
        setIconTitle(resIconTitle);
        setTitle(srtTitle);
        setDataListView(listData);
    }

    public void setStyle(int style) {
        this.style = style;
        switch (style) {
            case STYLE_BLACK:
                setBackgroundResource(COLOR_BLACK);
                titleLayout.setVisibility(VISIBLE);
                tvTitle.setTextColor(getColor(COLOR_GRAY));
                btnViewMore.setBackgroundResource(ICON_MORE_GRAY);
                divider.setVisibility(GONE);
                lineTop.setVisibility(GONE);
                lineBottom.setVisibility(GONE);
                break;
            case STYLE_GRAY:
                setBackgroundResource(COLOR_GRAY);
                titleLayout.setVisibility(VISIBLE);
                tvTitle.setTextColor(getColor(COLOR_BLUE));
                btnViewMore.setBackgroundResource(ICON_MORE_BLUE);
                divider.setVisibility(VISIBLE);
                lineTop.setVisibility(GONE);
                lineBottom.setVisibility(GONE);
                break;
            case STYLE_BLACK_NO_TITLE:
                setBackgroundResource(COLOR_BLACK);
                titleLayout.setVisibility(GONE);
                divider.setVisibility(GONE);
                lineTop.setVisibility(VISIBLE);
                lineBottom.setVisibility(VISIBLE);
                break;
            case STYLE_BLACK_NO_FILMNAME:
                setBackgroundResource(COLOR_BLACK);
                titleLayout.setVisibility(VISIBLE);
                tvTitle.setTextColor(getColor(COLOR_GRAY));
                btnViewMore.setBackgroundResource(ICON_MORE_GRAY);
                divider.setVisibility(VISIBLE);
                divider.setBackgroundResource(R.color.dividers_light);
                lineTop.setVisibility(GONE);
                lineBottom.setVisibility(GONE);
                break;
            default:
                setBackgroundResource(COLOR_WHITE);
                titleLayout.setVisibility(VISIBLE);
                tvTitle.setTextColor(getColor(COLOR_BLUE));
                btnViewMore.setBackgroundResource(ICON_MORE_BLUE);
                divider.setVisibility(VISIBLE);
                lineTop.setVisibility(GONE);
                lineBottom.setVisibility(GONE);
                break;
        }
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setIconTitle(int resIcon) {
        if (resIcon == 0) return;
        this.iconTitleRes = resIcon;
        iconTitle.setImageResource(resIcon);
    }

    public void setTitle(String title) {
        if (title == null) return;
        this.title = title;
        tvTitle.setText(title);
    }

    public MoviesRowAdapter getAdapter() {
        return adapter;
    }

    public void setDataListView(ArrayList<Object> listData) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new MoviesRowAdapter(getContext(), itemType);
        adapter.setList(listData);
        recyclerView.setAdapter(adapter);
    }

    public void setOnItemClickListener(MoviesRowAdapter.OnItemClickListener onItemClickListener) {
        if (adapter == null) return;
        adapter.setOnItemClickListener(onItemClickListener);
    }

    public void setOnRecentInfoClick(MoviesRowAdapter.OnRecentInfoClick onRecentInfoClick) {
        if (adapter == null) return;
        adapter.setOnRecentInfoClick(onRecentInfoClick);
    }

    public void setOnVtvMoviesRowListener(final OnVtvMoviesRowListener moreListener) {
        if (btnViewMore== null) return;
        btnViewMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moreListener != null) {
                    moreListener.onClickViewMore();
                }
            }
        });
    }

    private int getColor(int resColor) {
        return ResourcesCompat.getColor(getResources(), resColor, null);
    }

    public static class Builder {
        private VtvMoviesRowView homeRowMovie;

        public Builder(Context context) {
            this.homeRowMovie = new VtvMoviesRowView(context);
        }

        public Builder setStyle(int styleRow) {
            this.homeRowMovie.setStyle(styleRow);
            return this;
        }

        public Builder setIconTitle(int resIcon) {
            this.homeRowMovie.setIconTitle(resIcon);
            return this;
        }

        public Builder setIconTitleRoll() {
            this.homeRowMovie.setIconTitle(ICON_TITLE_ROLL);
            return this;
        }

        public Builder setIconTitleLa() {
            this.homeRowMovie.setIconTitle(ICON_TITLE_LA);
            return this;
        }

        public Builder setIconTitleStar() {
            this.homeRowMovie.setIconTitle(ICON_TITLE_STAR);
            return this;
        }

        public Builder setTitle(String title) {
            this.homeRowMovie.setTitle(title);
            return this;
        }

        public Builder setListData(int itemType, ArrayList<Object> listData) {
            this.homeRowMovie.setItemType(itemType);
            this.homeRowMovie.setDataListView(listData);
            return this;
        }

        public Builder addOnVtvMoviesRowListener(OnVtvMoviesRowListener moreListener){
            this.homeRowMovie.setOnVtvMoviesRowListener(moreListener);
            return this;
        }

        public Builder addOnItemClickListener(MoviesRowAdapter.OnItemClickListener onItemClickListener) {
            this.homeRowMovie.setOnItemClickListener(onItemClickListener);
            return this;
        }

        public VtvMoviesRowView build() {
            return this.homeRowMovie;
        }
    }

    public interface OnVtvMoviesRowListener {
        void onClickViewMore();
    }
}
