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

    private OnFooterViewListener onFooterViewListener;

    private OnRequestListener onRequestListener;

    public VtvFooterView(Context context) {
        this(context, null, 0);
    }

    public VtvFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VtvFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(getContext()).inflate(R.layout.widget_footer_view, this);
        View btnMessenge = findViewById(R.id.footer_messenge);
        View btnFacebook = findViewById(R.id.footer_facebook);
        View btnRequest = findViewById(R.id.footer_request);
        View btnReport = findViewById(R.id.footer_report);

        CompomentListener compomentListener = new CompomentListener();
        btnMessenge.setOnClickListener(compomentListener);
        btnFacebook.setOnClickListener(compomentListener);
        btnRequest.setOnClickListener(compomentListener);
        btnReport.setOnClickListener(compomentListener);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VtvFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, 0);
    }

    public void setOnFooterViewListener(OnFooterViewListener onFooterViewListener) {
        this.onFooterViewListener = onFooterViewListener;
    }

    public void setOnRequestListener(OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
    }

    private final class CompomentListener implements OnClickListener {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.footer_messenge:
                    if (onFooterViewListener != null) {
                        onFooterViewListener.onSendMessenge();
                    }
                    break;
                case R.id.footer_facebook:
                    if (onFooterViewListener != null) {
                        onFooterViewListener.onOpenFanpage();
                    }
                    break;
                case R.id.footer_request:
                    if (onFooterViewListener != null) {
                        onFooterViewListener.onSendEmail();
                    } else if (onRequestListener != null) {
                        onRequestListener.onRequest();
                    }
                    break;
                case R.id.footer_report:
                    if (onFooterViewListener != null) {
                        onFooterViewListener.onSendReport();
                    }
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

    public interface OnRequestListener {
        void onRequest();
    }
}
