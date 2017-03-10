package com.vungtv.film.popup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.vungtv.film.R;
import com.vungtv.film.util.TextUtils;
import com.vungtv.film.widget.VtvButton;
import com.vungtv.film.widget.VtvTextView;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/4/2017.
 * Email: vancuong2941989@gmail.com
 */

public class PopupUpdate implements View.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {

    private Dialog dialog;

    private TextView tvTitle;

    private TextView tvContent;

    private Button btnSubmit;

    private Button btnCancel;

    private Resources resources;

    private OnPopupUpdateListener onPopupUpdateListener;

    private boolean isCancel = false;

    public PopupUpdate(Context context) {
        init(context);
    }

    public PopupUpdate(Context context, boolean isCancel) {
        this.isCancel = isCancel;
        init(context);
    }

    protected void init(Context context) {
        resources = context.getApplicationContext().getResources();

        dialog = new Dialog(context, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_update);
        dialog.setOnDismissListener(this);
        dialog.setOnCancelListener(this);

        tvTitle = (VtvTextView) dialog.findViewById(R.id.pop_msg_tv_title);
        tvContent = (VtvTextView) dialog.findViewById(R.id.pop_msg_tv_content);
        btnSubmit = (VtvButton) dialog.findViewById(R.id.pop_msg_btn_submit);
        btnCancel = (VtvButton) dialog.findViewById(R.id.pop_msg_btn_cancel);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_msg_btn_submit:
                dismiss();
                if (onPopupUpdateListener != null) {
                    onPopupUpdateListener.onPopupUpdateBtnConfirmClick();
                }
                break;
            case R.id.pop_msg_btn_cancel:
                dismiss();
                break;
        }
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

    public void setOnPopupUpdateListener(OnPopupUpdateListener onPopupUpdateListener) {
        this.onPopupUpdateListener = onPopupUpdateListener;
    }

    public void setTextTitle(String title){
        if (title != null) tvTitle.setText(title);
    }

    public void setTextTitle(@StringRes int resId){
        tvTitle.setText(resources.getString(resId));
    }

    public void setTextContent(String content){
        if (content != null) tvContent.setText(content);
    }

    public void setTextContent(@StringRes int resId){
        tvContent.setText(resources.getString(resId));
    }

    public void setTextContentFromHtml(String content){
        if (content != null) {
            tvContent.setText(TextUtils.styleTextHtml(content));
        }
    }

    public void setTextBtnConfirm(String text){
        if (text != null) btnSubmit.setText(text);
    }

    public void setTextBtnConfirm(@StringRes int resId){
        btnSubmit.setText(resources.getString(resId));
    }

    public void setTextBtnCancel(String text){
        if (text != null) btnCancel.setText(text);
    }

    public void setTextBtnCancel(@StringRes int resId){
        btnCancel.setText(resources.getString(resId));
    }

    public void setBtnConfirmVisible(boolean visible) {
        btnSubmit.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setBtnCancelVisible(boolean visible) {
        btnCancel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (onPopupUpdateListener != null) {
            onPopupUpdateListener.onPopupUpdateBtnCancelClick();
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (isCancel && onPopupUpdateListener != null) {
            onPopupUpdateListener.onPopupUpdateBtnCancelClick();
        }
    }

    public interface OnPopupUpdateListener {

        void onPopupUpdateBtnConfirmClick();

        void onPopupUpdateBtnCancelClick();
    }
}
