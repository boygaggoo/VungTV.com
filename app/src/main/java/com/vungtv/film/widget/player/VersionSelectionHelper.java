package com.vungtv.film.widget.player;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.vungtv.film.R;
import com.vungtv.film.widget.VtvTextView;

/**
 *
 * Created by pc on 3/6/2017.
 */

public class VersionSelectionHelper implements View.OnClickListener {
    private final int padding;

    private Dialog dialog;

    private LinearLayout rootlayout;

    private LinearLayout.LayoutParams layoutParams;

    private OnItemSelectedListener onItemSelectedListener;

    public VersionSelectionHelper(Context context) {

        padding = context.getResources().getDimensionPixelSize(R.dimen.space_16);

        dialog = new Dialog(context, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_track_selection);
        rootlayout = (LinearLayout) dialog.findViewById(R.id.root);
        layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void show(String[] labels, int pos) {
        rootlayout.removeAllViews();

        for (int i = 0; i < labels.length; i++) {
            VtvTextView tvItem = new VtvTextView(dialog.getContext());
            tvItem.setPadding(padding, padding, padding, padding);
            tvItem.setText(labels[i]);
            tvItem.setTextColor(Color.argb(222, 0, 0, 0));
            tvItem.setId(i);
            tvItem.setOnClickListener(this);
            if (i == pos) {
                tvItem.setBackgroundResource(R.drawable.ds_touchable_bg_gray2);
            } else {
                tvItem.setBackgroundResource(R.drawable.ds_touchable_bg_gray1);
            }

            rootlayout.addView(tvItem, i, layoutParams);
        }

        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemVersionSelected(view.getId());
        }
    }

    public interface OnItemSelectedListener {

        void onItemVersionSelected(int pos);
    }
}
