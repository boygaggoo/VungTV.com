package com.vungtv.film.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;

import com.vungtv.film.R;
import com.vungtv.film.widget.VtvTextView;


public class PopupRating implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private Dialog dialog;

    private VtvTextView tvMsg;

    private RatingBar ratingBar;

    private Resources resources;

    private OnPopupRatingListener onPopupRatingListener;

    private int star = 1;

    /**
     * Contructor
     *
     * @param context c
     */
    public PopupRating(Context context) {

        resources = context.getApplicationContext().getResources();

        dialog = new Dialog(context, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_rating_movie);

        tvMsg = (VtvTextView) dialog.findViewById(R.id.pop_rating_text);

        ratingBar = (RatingBar) dialog.findViewById(R.id.pop_rating_rating);
        ratingBar.setOnRatingBarChangeListener(this);

        View btnSubmit = dialog.findViewById(R.id.pop_rating_btn_submit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();
        onPopupRatingListener.onSendRating(star);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        star = (int) v;
        switch (star) {
            case 0:
                tvMsg.setText(resources.getText(R.string.popup_rating_text_0));
                break;
            case 1:
                tvMsg.setText(resources.getText(R.string.popup_rating_text_1));
                break;
            case 2:
                tvMsg.setText(resources.getText(R.string.popup_rating_text_2));
                break;
            case 3:
                tvMsg.setText(resources.getText(R.string.popup_rating_text_3));
                break;
            case 4:
                tvMsg.setText(resources.getText(R.string.popup_rating_text_4));
                break;
            case 5:
                tvMsg.setText(resources.getText(R.string.popup_rating_text_5));
                break;
        }
    }

    public void setRating(float star) {
        ratingBar.setRating(star);
    }

    public boolean isShowing() {
        return !(dialog == null || !dialog.isShowing());
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void setOnPopupChangeNameListener(OnPopupRatingListener onPopupRatingListener) {
        this.onPopupRatingListener = onPopupRatingListener;
    }

    public interface OnPopupRatingListener {
        void onSendRating(int star);
    }
}
