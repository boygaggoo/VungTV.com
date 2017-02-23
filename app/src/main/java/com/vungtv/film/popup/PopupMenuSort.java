package com.vungtv.film.popup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.ApiQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopupMenuSort {

    private Context context;
    private ListAdapter adapter;
    private View anchor;
    private ListPopupWindow popupWindow;
    private List<HashMap<String, Object>> listData = new ArrayList<>();
    private int itemSelected = 0;
    private Drawable bgPopup;
    private OnMenuSortListener onMenuSortListener;


    public PopupMenuSort(Context context, View anchor) {
        this.context = context;
        this.anchor = anchor;
        bgPopup = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ds_bg_gray80_radius, null);

        setPopupWindow();
    }

    public void setOnMenuSortListener(OnMenuSortListener onMenuSortListener) {
        this.onMenuSortListener = onMenuSortListener;
    }

    public boolean isShowing() {
        return popupWindow != null && popupWindow.isShowing();
    }

    public void show() {
        if (popupWindow == null) setPopupWindow();
        addListData();
        setAdapter();
        popupWindow.setAdapter(adapter);
        popupWindow.show();
    }

    public void dismiss() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    public void setChecked(int id) {
        itemSelected = id;
    }

    private void setPopupWindow() {
        popupWindow = new ListPopupWindow(context);
        popupWindow.setAnchorView(anchor);
        popupWindow.setBackgroundDrawable(bgPopup);
        popupWindow.setVerticalOffset(context.getResources().getDimensionPixelSize(R.dimen.space_7));
        popupWindow.setHorizontalOffset(context.getResources().getDimensionPixelSize(R.dimen.margin));
        popupWindow.setWidth(context.getResources().getDimensionPixelSize(R.dimen.popup_menu_size_w));
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = i;
                popupWindow.dismiss();
                if (onMenuSortListener != null)
                    onMenuSortListener.onMenuSortItemSelected(ApiQuery.P_MOVIES_SORT[i]);
            }
        });
    }

    private void setAdapter() {
        adapter = new SimpleAdapter(context, listData,
                R.layout.item_menu_title_radio,
                new String[] {"TITLE", "ICON"},
                new int[] {R.id.item_menu_title, R.id.item_menu_radio});
    }

    private void addListData() {
        listData.clear();
        listData.add(createMap(R.string.popup_text_sort_view, 0));
        listData.add(createMap(R.string.popup_text_sort_nameaz, 1));
        listData.add(createMap(R.string.popup_text_sort_nameza, 2));
        listData.add(createMap(R.string.popup_text_sort_imdb, 3));
        listData.add(createMap(R.string.popup_text_sort_year, 4));
    }

    private HashMap<String, Object> createMap(int titleRes, int itemId) {
        int icon = R.drawable.icon_unselect;
        if (itemId == itemSelected)
            icon = R.drawable.icon_select;
        String title = context.getResources().getString(titleRes);

        HashMap<String, Object> map = new HashMap<>();
        map.put("TITLE", title);
        map.put("ICON", icon);
        return map;
    }

    public interface OnMenuSortListener {
        void onMenuSortItemSelected(String sapXep);
    }
}
