package com.vungtv.film.feature.home;

import android.content.Context;
import android.content.res.Resources;
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
    private static final int TYPE_WITH_ICON = 2;
    private static final int TYPE_WITH_ICON_LABEL = 3;
    private static final int TYPE_LINE = 4;
    private Context context;
    private ArrayList<NavItem> list = new ArrayList<>();
    private OnNavItemSelectedListener onNavItemSelectedListener;
    private int positionChecked = 1;

    public HomeNavAdapter(Context context) {
        this.context = context;
        addItemNav();
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

    /**
     * Add item menu to nav;
     */
    private void addItemNav() {
        //add header
        addTolist(TYPE_HEADER, NAV_ITEMID.ACCOUNT, R.string.login_action_login);
        //add item menu;
        addTolist(NAV_ITEMID.HOME, R.string.home_text_home);
        addTolist(NAV_ITEMID.TVSERIES, R.string.home_text_phim_bo);
        addTolist(NAV_ITEMID.MOVIE, R.string.home_text_phim_le);
        addTolist(NAV_ITEMID.ANIME, R.string.home_text_hoat_hinh);
        addTolist(NAV_ITEMID.TVSHOW, R.string.home_text_tvshow);
        addTolist(NAV_ITEMID.FILM18, R.string.home_text_phim_18);
        addTolist(NAV_ITEMID.CINEMA, R.string.home_text_phim_chieu_rap);
        addTolist(NAV_ITEMID.COMING, R.string.home_text_phim_sap_chieu);
        addTolist(NAV_ITEMID.IMDB, R.string.home_text_topIMDB);
        // add line
        addTolist(TYPE_LINE, 999, R.string.home_text_phim_da_tai, 0);

        addTolist(TYPE_WITH_ICON, NAV_ITEMID.FAVORITE, R.string.home_text_phim_yeu_thich, R.drawable.icon_heart3);
        addTolist(TYPE_WITH_ICON_LABEL, NAV_ITEMID.FOLLOW, R.string.home_text_phim_theo_doi, R.drawable.icon_bell3);
        addTolist(TYPE_WITH_ICON, NAV_ITEMID.DOWNLOAD, R.string.home_text_phim_da_tai, R.drawable.icon_download2);
    }

    private void addTolist(int itemid, int titleRes) {
        Resources res = context.getResources();
        list.add(new NavItem(itemid, res.getString(titleRes)));
    }

    private void addTolist(int itemType, int itemid, int titleRes) {
        Resources res = context.getResources();
        list.add(new NavItem(itemType, itemid, res.getString(titleRes)));
    }

    private void addTolist(int itemType, int itemid, int titleRes, int iconLeftRes) {
        Resources res = context.getResources();
        list.add(new NavItem(itemType, itemid, res.getString(titleRes), iconLeftRes));
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
                                view, list.get(getLayoutPosition()).getId());
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
                onNavItemSelectedListener.onNavigationItemSelected(view, list.get(getLayoutPosition()).getId());
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
                onNavItemSelectedListener.onNavigationItemSelected(view, list.get(getLayoutPosition()).getId());
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
                onNavItemSelectedListener.onNavigationItemSelected(view, list.get(getLayoutPosition()).getId());
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

        void onNavigationItemSelected(View v, int itemId);
    }
}
