package com.vungtv.film.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vungtv.film.R;

public class VtvTwoTextView extends RelativeLayout {
    public static final int ICON_RIGHT_EDIT = R.drawable.icon_edit;
    public static final int ICON_RIGHT_TICK= R.drawable.icon_tick;

    private VtvTextView textLeft, textRight;
    private ImageView btnEdit;
    private ImageView iconLeft;

    private String textLeftValue, textRightValue;
    private Drawable iconLeftDrawable, iconRightDrawable;

    private OnVtvTwoTextViewListener onVtvTwoTextViewListener;

    public VtvTwoTextView(Context context) {
        super(context);
        init(null);
    }

    public VtvTwoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VtvTwoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvTwoTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.VtvTwoTextView, 0, 0);
            try {
                textLeftValue = a.getString(R.styleable.VtvTwoTextView_textLeft);
                textRightValue = a.getString(R.styleable.VtvTwoTextView_textRight);
                iconLeftDrawable = a.getDrawable(R.styleable.VtvTwoTextView_iconLeft);
                iconRightDrawable = a.getDrawable(R.styleable.VtvTwoTextView_iconRight);
            } finally {
                a.recycle();
            }
        }
        retrieverViews();
        setDataDefault();
    }

    protected void retrieverViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_two_text_view, this);
        textLeft = (VtvTextView) findViewById(R.id.towTextView_tv_left);
        textRight = (VtvTextView) findViewById(R.id.towTextView_tv_right);
        iconLeft = (ImageView) findViewById(R.id.towTextView_icon_left);
        btnEdit = (ImageView) findViewById(R.id.towTextView_btn_edit);
        btnEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onVtvTwoTextViewListener != null) {
                    onVtvTwoTextViewListener.onBtnEditClick();
                }
            }
        });
    }

    protected void setDataDefault() {
        if (textLeftValue != null)
            textLeft.setText(textLeftValue);

        if (textRightValue != null)
            textRight.setText(textRightValue);

        if (iconLeftDrawable == null) {
            iconLeft.setVisibility(GONE);
        } else {
            iconLeft.setVisibility(VISIBLE);
            iconLeft.setImageDrawable(iconLeftDrawable);
        }

        if (iconRightDrawable == null) {
            btnEdit.setVisibility(GONE);
        } else {
            btnEdit.setVisibility(VISIBLE);
            btnEdit.setImageDrawable(iconRightDrawable);
        }
    }

    public void setTextLeft(String text) {
        textLeftValue = text;
        if (textLeftValue == null) return;
        textLeft.setText(textLeftValue);
    }

    public void setTextLeft(@StringRes int stringRes) {
        if (stringRes > 0)
            setTextLeft(getResources().getString(stringRes));
    }

    public void setTextRight(String text) {
        textRightValue = text;
        if (textRightValue == null) return;
        textRight.setText(textRightValue);
    }

    public void setTextRight(@StringRes int stringRes) {
        if (stringRes > 0)
            setTextRight(getResources().getString(stringRes));
    }

    public void setIconLeft(@DrawableRes int iconRes) {
        if (iconRes == 0) return;
        iconLeftDrawable = ResourcesCompat.getDrawable(getResources(), iconRes, null);
        if (iconLeftDrawable == null) {
            iconLeft.setVisibility(GONE);
        } else {
            iconLeft.setVisibility(VISIBLE);
            iconLeft.setImageDrawable(iconLeftDrawable);
        }
    }

    public void setIconRight(@DrawableRes int iconRes) {
        if (iconRes == 0) return;
        iconRightDrawable = ResourcesCompat.getDrawable(getResources(), iconRes, null);
        if (iconRightDrawable == null) {
            btnEdit.setVisibility(GONE);
        } else {
            btnEdit.setVisibility(VISIBLE);
            btnEdit.setImageDrawable(iconRightDrawable);
        }
    }

    public void setIconRightEdit() {
        setIconRight(ICON_RIGHT_EDIT);
        btnEdit.setEnabled(true);
    }

    public void setIconRightTick() {
        setIconRight(ICON_RIGHT_TICK);
        btnEdit.setEnabled(false);
    }

    public void setOnVtvTwoTextViewListener(OnVtvTwoTextViewListener onVtvTwoTextViewListener) {
        this.onVtvTwoTextViewListener = onVtvTwoTextViewListener;
    }

    public interface OnVtvTwoTextViewListener {
        void onBtnEditClick();
    }
}
