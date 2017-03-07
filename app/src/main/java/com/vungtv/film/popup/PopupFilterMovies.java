package com.vungtv.film.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Spinner;

import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.model.ApiPopupFilterMovies;

import java.util.Arrays;

/**
 *
 * Created by Mr Cuong on 2/24/2017.
 */

public class PopupFilterMovies implements View.OnClickListener {
    private Context context;
    private Dialog dialog;
    private Spinner sapXepSpinner, danhMucSpinner, quocGiaSpinner, theLoaiSpinner, namSpinner;
    private OnPopupMoviesFilterListener onPopupMoviesFilterListener;

    private final String[] sapXepStr, danhMucStr, quocGiaStr, theLoaiStr, namStr;
    private final String[] sapXepValue, danhMucValue, quocGiaValue, theLoaiValue, namValue;

    /* Contructor */
    public PopupFilterMovies(Context context, ApiPopupFilterMovies.Data filterDatas) {
        this.context = context;

        sapXepStr = new String[filterDatas.getSapXep().size() + 1];
        sapXepStr[0] = "---";
        sapXepValue = new String[filterDatas.getSapXep().size() + 1];
        sapXepValue[0] = "";
        for (int i = 0; i < filterDatas.getSapXep().size(); i++) {
            sapXepStr[i+1] = filterDatas.getSapXep().get(i).getLabel();
            sapXepValue[i+1] = filterDatas.getSapXep().get(i).getSlug();
        }

        danhMucStr = new String[filterDatas.getDanhMuc().size() + 1];
        danhMucStr[0] = "---";
        danhMucValue = new String[filterDatas.getDanhMuc().size() + 1];
        danhMucValue[0] = "";
        for (int i = 0; i < filterDatas.getDanhMuc().size(); i++) {
            danhMucStr[i+1] = filterDatas.getDanhMuc().get(i).getLabel();
            danhMucValue[i+1] = filterDatas.getDanhMuc().get(i).getSlug();
        }

        quocGiaStr = new String[filterDatas.getQuocGia().size() + 1];
        quocGiaStr[0] = "---";
        quocGiaValue = new String[filterDatas.getQuocGia().size() + 1];
        quocGiaValue[0] = "";
        for (int i = 0; i < filterDatas.getQuocGia().size(); i++) {
            quocGiaStr[i+1] = filterDatas.getQuocGia().get(i).getLabel();
            quocGiaValue[i+1] = filterDatas.getQuocGia().get(i).getSlug();
        }

        theLoaiStr = new String[filterDatas.getTheLoai().size() + 1];
        theLoaiStr[0] = "---";
        theLoaiValue = new String[filterDatas.getTheLoai().size() + 1];
        theLoaiValue[0] = "";
        for (int i = 0; i < filterDatas.getTheLoai().size(); i++) {
            theLoaiStr[i+1] = filterDatas.getTheLoai().get(i).getLabel();
            theLoaiValue[i+1] = filterDatas.getTheLoai().get(i).getSlug();
        }

        namStr = new String[filterDatas.getNam().size() + 1];
        namStr[0] = "---";
        namValue = new String[filterDatas.getNam().size() + 1];
        namValue[0] = "";
        for (int i = 0; i < filterDatas.getNam().size(); i++) {
            namStr[i+1] = filterDatas.getNam().get(i).getLabel();
            namValue[i+1] = filterDatas.getNam().get(i).getSlug();
        }

        init();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pop_filter_movies_btn_submit) {
            dismiss();
            if (onPopupMoviesFilterListener != null) {
                onPopupMoviesFilterListener.onPopupMoviesFilterSubmit(
                        sapXepValue[sapXepSpinner.getSelectedItemPosition()],
                        danhMucValue[danhMucSpinner.getSelectedItemPosition()],
                        quocGiaValue[quocGiaSpinner.getSelectedItemPosition()],
                        theLoaiValue[theLoaiSpinner.getSelectedItemPosition()],
                        namValue[namSpinner.getSelectedItemPosition()]
                );
            }
        }
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    public void show() {
        if (dialog != null) {
            if (dialog.isShowing()) return;
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            if (!dialog.isShowing()) return;
            dialog.dismiss();
        }
    }

    public void setOnPopupMoviesFilterListener(OnPopupMoviesFilterListener onPopupMoviesFilterListener) {
        this.onPopupMoviesFilterListener = onPopupMoviesFilterListener;
    }

    private void init() {
        dialog = new Dialog(context, R.style.AppTheme_Dialog_FullWidthHeight);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_filter_movies);
        if (dialog.getWindow() != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(false);

        View btnSubmit = dialog.findViewById(R.id.pop_filter_movies_btn_submit);
        btnSubmit.setOnClickListener(this);
        sapXepSpinner = (Spinner) dialog.findViewById(R.id.pop_filter_movies_spinner_sapxep);
        danhMucSpinner = (Spinner) dialog.findViewById(R.id.pop_filter_movies_spinner_danhmuc);
        quocGiaSpinner = (Spinner) dialog.findViewById(R.id.pop_filter_movies_spinner_quocgia);
        theLoaiSpinner = (Spinner) dialog.findViewById(R.id.pop_filter_movies_spinner_theloai);
        namSpinner = (Spinner) dialog.findViewById(R.id.pop_filter_movies_spinner_nam);

        setupSpinner(sapXepSpinner, sapXepStr);
        setupSpinner(danhMucSpinner, danhMucStr);
        setupSpinner(quocGiaSpinner, quocGiaStr);
        setupSpinner(theLoaiSpinner, theLoaiStr);
        setupSpinner(namSpinner, namStr);
    }

    private void setupSpinner(Spinner spinner, String[] listTitle) {
        VtvSpinnerAdapter adapter = new VtvSpinnerAdapter(
                context,
                R.layout.item_popup_filter_movies,
                Arrays.asList(listTitle)
        );
        adapter.setDropDownViewResource(R.layout.item_popup_filter_movies_dropdown);;
        spinner.setAdapter(adapter);
    }

    public interface OnPopupMoviesFilterListener {
        void onPopupMoviesFilterSubmit(String sapXep, String danhMuc, String quocGia, String theLoai, String nam);
    }
}
