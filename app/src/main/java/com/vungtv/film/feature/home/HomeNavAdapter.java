package com.vungtv.film.feature.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.model.NavItem;
import com.vungtv.film.model.User;
import com.vungtv.film.widget.VtvCircularImageView;
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;

public class HomeNavAdapter extends RecyclerView.Adapter {
    private static final String TAG = HomeNavAdapter.class.getSimpleName();

    private static final int COLOR_BG_NORMAL = R.drawable.ds_touchable_bg_gray8;
    private static final int COLOR_BG_ACTIVE = R.color.colorPrimary;

    private static final int ICON_RIGHT_NORMAL = R.drawable.icon_forward;
    private static final int ICON_RIGHT_ACTIVE = R.drawable.icon_lavung;

    private static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_WITH_ICON = 2;
    public static final int TYPE_WITH_ICON_LABEL = 3;
    public static final int TYPE_LINE = 4;

    private Context context;
    private ArrayList<NavItem> list = new ArrayList<>();
    private OnNavItemSelectedListener onNavItemSelectedListener;
    private int positionChecked = 1;

    public HomeNavAdapter(Context context) {
        this.context = context;

        // Add header account;
        list.add(0, new NavItem(TYPE_HEADER, context.getString(R.string.login_action_login), null));
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
            case TYPE_HEADER:
                itemView = inflater.inflate(R.layout.item_nav_header, parent, false);
                return new HeaderViewHolder(itemView);
            case TYPE_WITH_ICON:
                itemView = inflater.inflate(R.layout.item_nav_with_icon, parent, false);
                return new ItemViewIconHolder(itemView);
            case TYPE_WITH_ICON_LABEL:
                itemView = inflater.inflate(R.layout.item_nav_with_icon_label, parent, false);
                return new ItemViewIconLabelHolder(itemView);
            case TYPE_LINE:
                itemView = inflater.inflate(R.layout.item_nav_line, parent, false);
                return new ItemLineHolder(itemView);
            default:
                itemView = inflater.inflate(R.layout.item_nav, parent, false);
                return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder)holder).setInfo();
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder)holder).setInfo(list.get(position));
        } else if (holder instanceof ItemViewIconHolder) {
            ((ItemViewIconHolder)holder).setInfo(list.get(position));
        } else if (holder instanceof ItemViewIconLabelHolder) {
            ((ItemViewIconLabelHolder)holder).setInfo(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnNavItemSelectedListener(OnNavItemSelectedListener onNavItemSelectedListener) {
        this.onNavItemSelectedListener = onNavItemSelectedListener;
    }

    /**
     * Set item checked;
     *
     * @param position pos
     */
    public void setPosChecked(int position) {
        if (position >= getItemCount()) return;

        int curPos = positionChecked;
        positionChecked = position;
        notifyItemChanged(curPos);
        notifyItemChanged(positionChecked);
    }

    public void notifyAccountChange() {
        notifyItemChanged(0);
    }

    public void addItem(int pos, NavItem navItem) {
        list.add(pos, navItem);
        notifyItemInserted(pos);
    }

    public void addItem(NavItem navItem) {
        list.add(navItem);
        notifyItemInserted(list.size() - 1);
    }

    public void addAllItem(ArrayList<NavItem> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setListNavItem(ArrayList<NavItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public String getItemTitle(int pos) {
        return list.get(pos).getTitle();
    }

    /**
     * Class nav header, view user avatar and display name;
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private VtvCircularImageView imgAvatar;
        private VtvTextView tvDisplayname;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (VtvCircularImageView) itemView. findViewById(R.id.item_nav_header_avatar);
            tvDisplayname = (VtvTextView) itemView. findViewById(R.id.item_nav_header_username);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNavItemSelectedListener != null) {
                        onNavItemSelectedListener.onNavigationItemSelected(
                                getLayoutPosition(), list.get(getLayoutPosition()).getLink());
                    }
                }
            });
        }

        void setInfo() {
            User user = UserSessionManager.getCurrentUser(context);

            // Da dang nhap
            if (user != null) {
                if (user.getUserPhoto() == null || user.getUserPhoto().length() < 5) {
                    imgAvatar.setImageResource(R.drawable.icon_human1);
                } else {
                    Picasso.with(context)
                            .load(user.getUserPhoto())
                            .fit()
                            .placeholder(R.drawable.icon_human1)
                            .error(R.drawable.icon_human1)
                            .into(imgAvatar);
                }

                if (user.getUserDisplayName() != null && user.getUserDisplayName().length() > 1) {
                    tvDisplayname.setText(user.getUserDisplayName());
                } else {
                    tvDisplayname.setText(user.getUserEmail());
                }

                return;
            }

            // Chua dang nhap
            imgAvatar.setImageResource(R.drawable.icon_human2);
            tvDisplayname.setText(R.string.login_action_login);
        }
    }

    /**
     * Class item normal
     */
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ViewGroup rootLayout;
        private View lineActive;
        private VtvTextView textTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);

            rootLayout = (ViewGroup) itemView.findViewById(R.id.item_nav_root);
            lineActive = itemView.findViewById(R.id.item_nav_line);
            textTitle = (VtvTextView) itemView.findViewById(R.id.item_nav_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onNavItemSelectedListener != null) {
                onNavItemSelectedListener.onNavigationItemSelected(
                        getLayoutPosition(), list.get(getLayoutPosition()).getLink());
            }
        }

        public void setInfo(NavItem item) {

            if (item.getTitle() != null) {
                textTitle.setText(item.getTitle());
            }

            if (getLayoutPosition() == positionChecked) {
                rootLayout.setBackgroundResource(COLOR_BG_ACTIVE);
                lineActive.setVisibility(View.VISIBLE);
                textTitle.setCompoundDrawablesWithIntrinsicBounds(0,0, ICON_RIGHT_ACTIVE, 0);
            } else {
                rootLayout.setBackgroundResource(COLOR_BG_NORMAL);
                lineActive.setVisibility(View.INVISIBLE);
                textTitle.setCompoundDrawablesWithIntrinsicBounds(0,0, ICON_RIGHT_NORMAL,0);
            }
        }
    }

    /**
     * Class item with icon.
     */
    class ItemViewIconHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private VtvTextView textTitle;

        public ItemViewIconHolder(View itemView) {
            super(itemView);

            textTitle = (VtvTextView) itemView.findViewById(R.id.item_nav_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onNavItemSelectedListener != null) {
                onNavItemSelectedListener.onNavigationItemSelected(
                        getLayoutPosition(), list.get(getLayoutPosition()).getLink());
            }
        }

        public void setInfo(NavItem item) {

            if (item.getTitle() != null) {
                textTitle.setText(item.getTitle());
            }

            if (item.getIconLeftRes() != 0) {
                textTitle.setCompoundDrawablesWithIntrinsicBounds(item.getIconLeftRes(),0,ICON_RIGHT_NORMAL, 0);
            }
        }
    }

    /**
     * Class item with icon and label
     */
    class ItemViewIconLabelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private VtvTextView textTitle, textLabel;

        public ItemViewIconLabelHolder(View itemView) {
            super(itemView);

            textTitle = (VtvTextView) itemView.findViewById(R.id.item_nav_title);
            textLabel = (VtvTextView) itemView.findViewById(R.id.item_nav_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onNavItemSelectedListener != null) {
                onNavItemSelectedListener.onNavigationItemSelected(
                        getLayoutPosition(), list.get(getLayoutPosition()).getLink());
            }
        }

        public void setInfo(NavItem item) {

            if (item.getTitle() != null) {
                textTitle.setText(item.getTitle());
            }

            if (item.getIconLeftRes() != 0) {
                textTitle.setCompoundDrawablesWithIntrinsicBounds(item.getIconLeftRes(),0,0, 0);
            }

            textLabel.setText("99");
        }
    }

    /**
     * Class item line
     */
    class ItemLineHolder extends RecyclerView.ViewHolder {

        public ItemLineHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * ID of menu items
     */
    public static final class NAV_ITEMID {
        public static final int ACCOUNT = 0;
        public static final int HOME = 1;
        public static final int MOVIE = 2; //le
        public static final int TVSERIES = 3; // bo
        public static final int ANIME = 4;
        public static final int TVSHOW = 5;
        public static final int FILM18 = 6;
        public static final int CINEMA = 7;
        public static final int COMING = 8;
        public static final int IMDB = 9;
        public static final int FAVORITE = 10;
        public static final int FOLLOW = 11;
        public static final int DOWNLOAD = 12;
    }

    public interface OnNavItemSelectedListener {
        void onNavigationItemSelected(int position, String url);
    }
}
