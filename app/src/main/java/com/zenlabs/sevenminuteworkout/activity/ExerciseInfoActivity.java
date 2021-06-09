package com.zenlabs.sevenminuteworkout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.database.Exercise;
import com.zenlabs.sevenminuteworkout.utils.BaseActivity;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.UtilsMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsValues;

import me.kiip.sdk.Kiip;
import me.kiip.sdk.Poptart;

public class ExerciseInfoActivity extends BaseActivity {

    private Exercise exerciseItem;

    private ImageView closeImageView;
    private TextView titleTextView;
    private TextView preparationTextView, executionTextView;
    private WebView webView;

    public static final String EXERCISE_ACTIVITY_EXTRA_INFO = "EXERCISE_ACTIVITY_EXTRA_INFO";

    private boolean isKiipAlreadyShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info);

        Bundle extras = getIntent().getExtras();
        String objectString = extras.getString(EXERCISE_ACTIVITY_EXTRA_INFO, "");

        if (!objectString.equals("")) {
            exerciseItem = new Gson().fromJson(objectString, Exercise.class);
        }

        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        showKiip();
    }

    private void initViews() {

        closeImageView = (ImageView) findViewById(R.id.exerciseInfoScreenActionBarBackImageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        titleTextView = (TextView) findViewById(R.id.exerciseInfoScreeActionBarTitleTextView);

        preparationTextView = (TextView) findViewById(R.id.exerciseInfoScreenPreparationDescTextView);

        executionTextView = (TextView) findViewById(R.id.exerciseInfoScreenExecutionDescTextView);

        YoutubeWebViewClient youtubeWebViewClient = new YoutubeWebViewClient();
        YoutubeWebChromeClient youtubeWebChromeClient = new YoutubeWebChromeClient();

        webView = (WebView) findViewById(R.id.exerciseInfoScreenWebView);
        webView.setWebChromeClient(youtubeWebChromeClient);
        webView.setWebViewClient(youtubeWebViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        loadContent();

    }

    private void loadContent() {

        if (exerciseItem != null) {
            titleTextView.setText(exerciseItem.getName());
            preparationTextView.setText(exerciseItem.getDescPre());
            executionTextView.setText(exerciseItem.getDescExe());

//            webView.loadUrl("<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/c4DAnQ6DtF8\" frameborder=\"0\" allowfullscreen></iframe>");

//            ViewTreeObserver vto = webView.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    webView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    webView.loadData("<iframe width=\"" + webView.getWidth() + "\" height=\"" + webView.getHeight() + "\" src=\"" + "https://www.youtube.com/embed/c4DAnQ6DtF8\\" + " frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
//                }
//            });

            String url = exerciseItem.getYoutubeLink();

            if (url.contains("watch?v=")) {
                url = url.replace("watch?v=", "embed/");
            }

            LogService.Log("ExerciseInfoActivity", "loadContent url: " + url);

            webView.loadUrl(url);

            final String finalUrl = url;
            View clicklabelView = findViewById(R.id.exerciseInfoScreenWebViewClickableView);
            clicklabelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ExerciseInfoActivity.this,YoutubeWebViewActivity.class);
                    intent.putExtra(YoutubeWebViewActivity.YOUTUBE_WEBVIEW_ACTIVITY_URL_EXTRA_DATA, finalUrl);
                    startActivity(intent);
                }
            });

        }

    }

    private class YoutubeWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class YoutubeWebChromeClient extends WebChromeClient {


    }

    private void showKiip() {

        if (!isKiipAlreadyShown) {

            if (UtilsMethods.getBooleanFromSharedPreferences(ExerciseInfoActivity.this, UtilsValues.SHARED_PREFERENCES_KIIP_REWARDS)) {
                Kiip.getInstance().saveMoment(UtilsValues.KIIP_MOMENT_WORKOUT_COMPLETED, new Kiip.Callback() {

                    @Override
                    public void onFinished(Kiip kiip, Poptart reward) {
                        LogService.Log("kiip_fragment_tag", "onFinished");
                        if (reward == null) {
                            LogService.Log("kiip_fragment_tag", "Successful moment but no reward to give.");
//                            Toast.makeText(ExerciseInfoActivity.this, "Kiip: Successful moment but no reward to give.", Toast.LENGTH_LONG).show();
                        } else {
                            onPoptart(reward);
                        }
                        isKiipAlreadyShown = true;
                    }

                    @Override
                    public void onFailed(Kiip kiip, Exception exception) {
                        LogService.Log("kiip_fragment_tag", "onFailed ex: " + exception.toString());
                    }
                });
            }

        }

    }

}
