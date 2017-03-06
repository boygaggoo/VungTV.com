package com.vungtv.film.popup;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.vungtv.film.R;
import com.vungtv.film.feature.moviedetail.EpisodesRecycerAdapter;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.Episode;
import com.vungtv.film.widget.MarginDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by pc on 3/6/2017.
 */

public class PopupListEpisode implements TextWatcher {

    private Dialog dialog;

    private EditText searchView;

    private RecyclerView recyclerView;

    private EpisodesRecycerAdapter adapter;

    public PopupListEpisode(Context context) {

        dialog = new Dialog(context, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_list_episode);

        recyclerView = (RecyclerView) dialog.findViewById(R.id.pop_eps_recyclerView);

        recyclerView.addItemDecoration(new MarginDecoration(context, R.dimen.space_4));
        recyclerView.setHasFixedSize(true);
        adapter = new EpisodesRecycerAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

            }
        });
        recyclerView.setAdapter(adapter);

        searchView = (EditText) dialog.findViewById(R.id.pop_eps_searview);
        searchView.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void setListEpisodes(ArrayList<Episode> list) {
        adapter.setList(list);
    }

    private int searchPosition(String query) {
        int pos = 0;

        final List<Episode> list = adapter.getList();
        for (Episode model : list) {
            final String text = model.getEpsTitle().toLowerCase();
            if (text.contains(query)) {

            }
        }
        return 0;
    }
}
