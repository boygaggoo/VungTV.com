package com.vungtv.film.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.vungtv.film.R;

public class VtvFooterView extends LinearLayout {
    private static final String TAG = VtvFooterView.class.getSimpleName();
    private View btnMessenge;
    private View btnFacebook;
    private View btnRequest;
    private View btnReport;

    private OnFooterViewListener onFooterViewListener;

    public VtvFooterView(Context context) {
        super(context);
        init();
    }

    public VtvFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VtvFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        retrieverView();
        registerListener();
    }

    private void retrieverView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_footer_view, this);

        btnMessenge = findViewById(R.id.footer_messenge);
        btnFacebook = findViewById(R.id.footer_facebook);
        btnRequest = findViewById(R.id.footer_request);
        btnReport = findViewById(R.id.footer_report);
    }

    private void registerListener() {
        CompomentListener compomentListener = new CompomentListener();
        btnMessenge.setOnClickListener(compomentListener);
        btnFacebook.setOnClickListener(compomentListener);
        btnRequest.setOnClickListener(compomentListener);
        btnReport.setOnClickListener(compomentListener);
    }

    public void setOnFooterViewListener(OnFooterViewListener onFooterViewListener) {
        this.onFooterViewListener = onFooterViewListener;
    }

    private void sendRepost() {

    }

    private final class CompomentListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (onFooterViewListener == null) return;
            switch (view.getId()) {
                case R.id.footer_messenge:
                    onFooterViewListener.onSendMessenge();
                    break;
                case R.id.footer_facebook:
                    onFooterViewListener.onOpenFanpage();
                    break;
                case R.id.footer_request:
                    onFooterViewListener.onSendEmail();
                    break;
                case R.id.footer_report:
                    onFooterViewListener.onSendReport();
                    break;
            }
        }
    }

    public interface OnFooterViewListener {
        void onSendMessenge();
        void onOpenFanpage();
        void onSendEmail();
        void onSendReport();
    }
}
