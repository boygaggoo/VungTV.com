package com.vungtv.film.widget.moviesrowview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vungtv.film.R;
import com.vungtv.film.interfaces.OnRecentInfoClickListener;
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;

import static com.vungtv.film.widget.moviesrowview.MoviesRowAdapter.ITEM_DEFAULT;

/**
 *
 * Created by pc on 3/9/2017.
 */
public class VtvMovieRowView extends LinearLayout implements View.OnClickListener {
    public static final int ICON_TITLE_ROLL = R.drawable.icon_filmroll;

    public static final int STYLE_DEFAULT = 0;
    public static final int STYLE_BLACK = 1;
    public static final int STYLE_GRAY = 2;
    public static final int STYLE_BLACK_NO_TITLE = 3;
    public static final int STYLE_BLACK_NO_FILMNAME = 4;

    private MoviesRowAdapter adapter;

    private ImageView iconTitle;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private View btnViewMore;

    private OnViewMoreListener onViewMoreListener;

    private int style = STYLE_DEFAULT;
    private int itemType = ITEM_DEFAULT;

    public VtvMovieRowView(Context context) {
        super(context);
        init(context, null);
    }

    public VtvMovieRowView(Context context, int style, int itemType) {
        super(context);
        this.style = style;
        this.itemType = itemType;
        init(context, null);
    }

    public VtvMovieRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VtvMovieRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){

        String title = "Phim Vừng";
        int iconTitleRes = ICON_TITLE_ROLL;
        // Get AttributeSet XML;
        if (attrs != null) {
            TypedArray a = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.VtvMovieRowView, 0, 0);
            try {
                style = a.getInt(R.styleable.VtvMovieRowView_style, STYLE_DEFAULT);
                itemType = a.getInt(R.styleable.VtvMovieRowView_itemType, ITEM_DEFAULT);
                iconTitleRes = a.getResourceId(R.styleable.VtvMovieRowView_iconTitle, ICON_TITLE_ROLL);
                title = a.getString(R.styleable.VtvMovieRowView_title);
            } finally {
                a.recycle();
            }
        }

        // Get layout XML with style;
        int layout;
        switch (style) {
            default:
                layout = R.layout.widget_movies_row_style_1;
                break;
            case STYLE_BLACK:
                layout = R.layout.widget_movies_row_style_2;
                break;
            case STYLE_GRAY:
                layout = R.layout.widget_movies_row_style_3;
                break;
            case STYLE_BLACK_NO_FILMNAME:
                layout = R.layout.widget_movies_row_style_4;
                break;
            case STYLE_BLACK_NO_TITLE:
                layout = R.layout.widget_movies_row_style_5;
                break;
        }

        LayoutInflater.from(context).inflate(layout, this);

        // Retriever and setup view
        recyclerView = (RecyclerView) findViewById(R.id.widget_movies_row_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new MovieRowDecoration(context));
        adapter = new MoviesRowAdapter(getContext(), itemType);
        recyclerView.setAdapter(adapter);

        if (style != STYLE_BLACK_NO_TITLE) {

            iconTitle = (ImageView) findViewById(R.id.widget_movies_row_icon);
            if (iconTitleRes != 0 && iconTitle != null) {
                iconTitle.setImageResource(iconTitleRes);
            }

            tvTitle = (VtvTextView) findViewById(R.id.widget_movies_row_title);
            if (tvTitle != null) {
                tvTitle.setText(title);
            }

            btnViewMore = findViewById(R.id.widget_movies_row_viewmore);
            if (btnViewMore != null) {
                btnViewMore.setOnClickListener(this);
            }
        }
    }

    public void setTitle(String text) {
        if (text == null || tvTitle == null) return;
        tvTitle.setText(text);
    }

    public void setTitle(@StringRes int strRes) {
        if (tvTitle == null) return;
        tvTitle.setText(strRes);
    }

    public void setIconTitle(@DrawableRes int resIcon) {
        if (iconTitle == null) return;
        iconTitle.setImageResource(resIcon);
    }

    public void setButtonViewMoreVisible(boolean visible) {
        if (btnViewMore == null) return;
        btnViewMore.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setListAdapter(ArrayList<Object> listData) {
        if (adapter == null) return;
        adapter.setList(listData);
    }

    public void setOnItemClickListener(MoviesRowAdapter.OnItemClickListener onItemClickListener) {
        if (adapter == null) return;
        adapter.setOnItemClickListener(onItemClickListener);
    }

    public void setOnRecentInfoListener(OnRecentInfoClickListener onRecentInfoClick ) {
        if (adapter == null) return;
        adapter.setOnRecentInfoClick(onRecentInfoClick);
    }

    public void setOnViewMoreListener(OnViewMoreListener onViewMoreListener) {
        this.onViewMoreListener = onViewMoreListener;
    }

    public MoviesRowAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onClick(View view) {
        if (view == btnViewMore) {
            if (onViewMoreListener != null) {
                onViewMoreListener.onViewMoreClick();
            }
        }
    }

    public interface OnViewMoreListener {

        void onViewMoreClick();
    }
}
