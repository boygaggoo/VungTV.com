package com.vungtv.film.feature.aboutcontact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.RemoteConfigManager;
import com.vungtv.film.model.Config;
import com.vungtv.film.util.TextUtils;
import com.vungtv.film.widget.VtvToolbarSetting;

import butterknife.BindView;

public class AboutActivity extends BaseActivity implements VtvToolbarSetting.OnToolbarListener {

    public static final String INTENT_PAGE = "INTENT_PAGE";

    public static final int PAGE_ABOUT = 0;

    public static final int PAGE_CONTACT = 1;

    @BindView(R.id.about_toolbar)
    VtvToolbarSetting toolbar;

    @BindView(R.id.about_tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar.setOnToolbarListener(this);

        Config config = RemoteConfigManager.getConfigs(getApplicationContext());

        if (getIntent() == null || config == null) {
            tvContent.setText(R.string.error_no_data);
        } else if (getIntent().getIntExtra(INTENT_PAGE, 0) == PAGE_ABOUT) {
            toolbar.setTitle(getString(R.string.about_text_title));
            tvContent.setText(TextUtils.styleTextHtml(config.getAboutUsHtml()));
        } else {
            toolbar.setTitle(getString(R.string.about_text_title_contact));
            tvContent.setText(TextUtils.styleTextHtml(config.getAdvContactHtml()));
        }
    }

    public static Intent buildIntentAbout(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        intent.putExtra(INTENT_PAGE, PAGE_ABOUT);
        return intent;
    }

    public static Intent buildIntentContact(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        intent.putExtra(INTENT_PAGE, PAGE_CONTACT);
        return intent;
    }

    @Override
    public void onBtnBackClick() {
        finish();
    }
}
