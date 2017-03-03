package com.vungtv.film.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.vungtv.film.R;
import com.vungtv.film.widget.VtvEditText;


public class PopupUserChangeName {
    private Dialog dialog;
    private VtvEditText editText;
    private OnPopupChangeNameListener onPopupChangeNameListener;

    public PopupUserChangeName(Context context) {
        dialog = new Dialog(context, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_user_change_name);

        editText = (VtvEditText) dialog.findViewById(R.id.pop_changename_ed_name);
        View btnSubmit = dialog.findViewById(R.id.pop_changename_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString();
                if (s.length() == 0) return;
                removeTextEdittext();
                dismiss();
                onPopupChangeNameListener.onChangename(s);
            }
        });
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

    public void removeTextEdittext() {
        editText.setText("");
    }

    public void setOnPopupChangeNameListener(OnPopupChangeNameListener onPopupChangeNameListener) {
        this.onPopupChangeNameListener = onPopupChangeNameListener;
    }

    public interface OnPopupChangeNameListener {
        void onChangename(String newName);
    }
}
