package com.vungtv.film.feature.personal;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vungtv.film.R;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.MenuItem;
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;

public class PersonalRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = PersonalRecyclerAdapter.class.getSimpleName();
    private static final int TYPE_TITLE = 0;
    public static final int TYPE_NORMAL = 1;
    private static final int TYPE_WITH_LABEL = 2;
    private Context context;
    private ArrayList<MenuItem> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private int numLabel = 0;

    public PersonalRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = null;

        switch (viewType) {
            case TYPE_TITLE:
                itemView = inflater.inflate(R.layout.item_personal_title, parent, false);
                return new TitleViewHolder(itemView);
            case TYPE_WITH_LABEL:
                itemView = inflater.inflate(R.layout.item_personal_label, parent, false);
                return new ItemViewLabelHolder(itemView);
            default:
                itemView = inflater.inflate(R.layout.item_personal, parent, false);
                return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder)holder).setInfo(list.get(position));
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder)holder).setInfo(list.get(position));
        } else if (holder instanceof ItemViewLabelHolder) {
            ((ItemViewLabelHolder)holder).setInfo(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Change data when user login or clearUserSession
     */
    public void notifyLoginStatusChange(boolean isLogin) {
        list.clear();
        if (isLogin) {
            addItemNavIsLogin();
        } else {
            addItemNavNotLogin();
        }
        notifyDataSetChanged();
    }

    /**
     * ntify when change data follow;
     */
    public void notifyChangeLabelItemFollow(int numLabel) {
        this.numLabel = numLabel;

        if (list.size() < 5) return;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == TYPE_WITH_LABEL) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    public long getItemId(int pos) {
        if (pos < list.size())
            return list.get(pos).getId();

        return -1;
    }

    /**
     * Add item menu to nav;
     */
    private void addItemNavNotLogin() {
        //add title
        addTolist(TYPE_TITLE, 0, R.string.personal_text_system, R.drawable.icon_setting);
        //add item menu;
        addTolist(USERPAGE_ITEMID.SETTING, R.string.personal_action_setting);
        addTolist(USERPAGE_ITEMID.APP_INFO, R.string.personal_action_app_info);
        addTolist(USERPAGE_ITEMID.CONTACT, R.string.personal_action_cantact);
    }

    /**
     * Add item menu to nav;
     */
    private void addItemNavIsLogin() {
        // Cá nhân
        addTolist(TYPE_TITLE, 0, R.string.personal_text_personal, R.drawable.icon_human3);
        addTolist(USERPAGE_ITEMID.USER_INFO, R.string.personal_action_user_info);
        addTolist(USERPAGE_ITEMID.CHANGE_PASS, R.string.personal_action_change_pass);
        addTolist(USERPAGE_ITEMID.INVITE_FRIENDS, R.string.personal_action_invite_friend);
        // Lịch sử
        addTolist(TYPE_TITLE, 0, R.string.personal_text_history, R.drawable.icon_history);
        addTolist(USERPAGE_ITEMID.FILM_VIEWED, R.string.personal_action_film_viewed);
        addTolist(USERPAGE_ITEMID.FAVORITE, R.string.home_text_phim_yeu_thich);
        addTolist(TYPE_WITH_LABEL, USERPAGE_ITEMID.FOLLOW, R.string.home_text_phim_theo_doi);
        // Hệ thống
        addTolist(TYPE_TITLE, 0, R.string.personal_text_system, R.drawable.icon_setting);
        addTolist(USERPAGE_ITEMID.SETTING, R.string.personal_action_setting);
        addTolist(USERPAGE_ITEMID.APP_INFO, R.string.personal_action_app_info);
        addTolist(USERPAGE_ITEMID.CONTACT, R.string.personal_action_cantact);
        addTolist(USERPAGE_ITEMID.LOGOUT, R.string.personal_action_logout);
    }

    private void addTolist(int itemid, int titleRes) {
        Resources res = context.getResources();
        list.add(new MenuItem(itemid, res.getString(titleRes)));
    }

    private void addTolist(int itemType, int itemid, int titleRes) {
        Resources res = context.getResources();
        list.add(new MenuItem(itemType, itemid, res.getString(titleRes)));
    }

    private void addTolist(int itemType, int itemid, int titleRes, int iconLeftRes) {
        Resources res = context.getResources();
        list.add(new MenuItem(itemType, itemid, res.getString(titleRes), iconLeftRes));
    }

    /**
     * Class item title
     */
    private class TitleViewHolder extends RecyclerView.ViewHolder {
        private VtvTextView textTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            textTitle = (VtvTextView) itemView. findViewById(R.id.item_userpage_title);
        }

        void setInfo(MenuItem navItem) {
            if (navItem.getTitle() != null) {
                textTitle.setText(navItem.getTitle());
            }
            if (navItem.getIconLeftRes() > 0)
                textTitle.setCompoundDrawablesWithIntrinsicBounds(navItem.getIconLeftRes(), 0, 0, 0);
        }
    }

    /**
     * Class item normal
     */
    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private VtvTextView textTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);

            textTitle = (VtvTextView) itemView.findViewById(R.id.item_userpage_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }

        public void setInfo(MenuItem item) {

            if (item.getTitle() != null) {
                textTitle.setText(item.getTitle());
            }
        }
    }

    /**
     * Class item with label
     */
    private class ItemViewLabelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private VtvTextView textTitle, textLabel;

        public ItemViewLabelHolder(View itemView) {
            super(itemView);

            textTitle = (VtvTextView) itemView.findViewById(R.id.item_userpage_title);
            textLabel = (VtvTextView) itemView.findViewById(R.id.item_userpage_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getLayoutPosition());
            }
        }

        public void setInfo(MenuItem item) {

            if (item.getTitle() != null) {
                textTitle.setText(item.getTitle());
            }

            textLabel.setText(String.valueOf(numLabel));
        }
    }

    /**
     * ID of menu items
     */
    public static final class USERPAGE_ITEMID {
        public static final int USER_INFO = 0;
        public static final int CHANGE_PASS = 1;
        public static final int INVITE_FRIENDS = 2;
        public static final int FILM_VIEWED = 3;
        public static final int FAVORITE = 4;
        public static final int FOLLOW = 5;
        public static final int SETTING = 6;
        public static final int APP_INFO = 7;
        public static final int CONTACT = 8;
        public static final int LOGOUT = 9;
    }
}
