package com.vungtv.film.widget.sliderview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vungtv.film.R;
import com.vungtv.film.model.Slider;

import java.util.ArrayList;

public class SliderAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = SliderAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Slider> list = new ArrayList<>();

    public SliderAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return SliderFragment.instance(list.get(position));
    }

    @Override
    public float getPageWidth(int position) {
        if (context.getResources().getBoolean(R.bool.isTabletLand)) {
            return 0.8f;
        } else {
            return 1f;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addItem(Slider data) {
        list.add(data);
        notifyDataSetChanged();
    }

    public void addMultiItem(ArrayList<Slider> listSlider) {
        list.clear();
        list.addAll(listSlider);
        notifyDataSetChanged();
    }
}
