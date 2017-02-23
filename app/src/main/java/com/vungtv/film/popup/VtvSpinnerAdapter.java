package com.vungtv.film.popup;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vungtv.film.util.FontUtils;

import java.util.List;

/**
 *
 * Created by Mr Cuong on 2/24/2017.
 */

public class VtvSpinnerAdapter extends ArrayAdapter<String> {
    // Initialise custom font, for example:
    private final Typeface FONT;

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);
    public VtvSpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        FONT = FontUtils.selectTypeface(context, FontUtils.NORMAL);
    }

    // Affects default (closed) state of the spinner
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(FONT);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(FONT);
        return view;
    }
}
