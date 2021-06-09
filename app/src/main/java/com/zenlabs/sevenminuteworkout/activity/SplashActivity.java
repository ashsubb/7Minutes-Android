package com.zenlabs.sevenminuteworkout.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.plistparser.Array;
import com.zenlabs.sevenminuteworkout.plistparser.Dict;
import com.zenlabs.sevenminuteworkout.plistparser.MyString;
import com.zenlabs.sevenminuteworkout.plistparser.PList;
import com.zenlabs.sevenminuteworkout.plistparser.PListXMLHandler;
import com.zenlabs.sevenminuteworkout.plistparser.PListXMLParser;
import com.zenlabs.sevenminuteworkout.utils.App;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.UtilsMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsValues;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    private ImageView loadingImageView;
    private AnimationDrawable loadingAnimation;
    private MediaPlayer mediaPlayer;

    private TextView bottomTextView;

    //tips
    private App featuredApp = new App();
    private String tip = "", quote = "";
    private ArrayList<App> apps;
    private boolean isTipsDownloaded = false;
    private boolean isAnimationFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialiseItems();

        new GetListOfTipsAsyncTask().execute();

        startTheAnimations();

    }

    private void initialiseItems() {

        loadingImageView = (ImageView) findViewById(R.id.splashScreenLoadingImageView);
        loadingImageView.setImageResource(R.drawable.splash_animation);
        loadingAnimation = (AnimationDrawable) loadingImageView.getDrawable();

//        mediaPlayer = MediaPlayer.create(SplashActivity.this, R.raw.loading_scr_sound);
        mediaPlayer = new MediaPlayer();
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.loading_sound);
        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mediaPlayerOnCompletionListener);
        } catch (IllegalArgumentException e) {
            LogService.Log("initialiseItems", "error: " + e.toString());
        } catch (IllegalStateException e) {
            LogService.Log("initialiseItems", "error: " + e.toString());
        } catch (IOException e) {
            LogService.Log("initialiseItems", "error: " + e.toString());
        }

        bottomTextView = (TextView) findViewById(R.id.splashScreenBottomTextView);

    }

    private void startTheAnimations() {
        loadingAnimation.start();
        mediaPlayer.start();
        bottomTextView.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.alpha_animation));
    }


    private MediaPlayer.OnCompletionListener mediaPlayerOnCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {

            LogService.Log("mediaPlayerOnCompletionListener", "onCompletion");

            isAnimationFinished = true;

            goToTheNextScreen();

        }

    };

    public class GetListOfTipsAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... arg0) {

            try {
                PListXMLParser parser = new PListXMLParser();
                PListXMLHandler handler = new PListXMLHandler();
                parser.setHandler(handler);

                String plist_url = UtilsValues.PLIST_URLS[UtilsValues.STORE];
                String s = UtilsMethods.readFile(plist_url);
                parser.parse(s);

                String moreApps = "";

                if (UtilsValues.STORE == UtilsValues.AMAZON_STORE) {
                    s = UtilsMethods.readFile(UtilsValues.AMAZON_PLIST);
                } else if (UtilsValues.STORE == UtilsValues.GOOGLE_STORE) {
                    s = UtilsMethods.readFile(UtilsValues.GOOGLE_PLIST);
                }

                moreApps = UtilsMethods.readFile(UtilsValues.PLIST_URLS[UtilsValues.STORE]);
                LogService.Log("GetListOfTipsAsyncTask", "moreApps: " + moreApps);
                UtilsMethods.setMoreApps(getBaseContext(), moreApps);
                parser.parse(moreApps);


                PList actualPList = ((PListXMLHandler) parser.getHandler())
                        .getPlist();

                Array featured_app_array = ((Dict) actualPList.getRootElement())
                        .getConfigurationArray("featured");

                featuredApp.setName(((Dict) featured_app_array.get(0))
                        .getConfiguration("name").getValue());

                featuredApp.setUrl(((Dict) featured_app_array.get(0))
                        .getConfiguration("url").getValue());

                featuredApp.setDesc(((Dict) featured_app_array.get(0))
                        .getConfiguration("description").getValue());

                featuredApp.setImage(((Dict) featured_app_array.get(0))
                        .getConfiguration("icon").getValue());

                Random rand = new Random();

                Array apps_array = ((Dict) actualPList.getRootElement())
                        .getConfigurationArray("apps");

                LogService.Log("GetListOfTipsAsyncTask", "apps_array: " + apps_array);

                Array tips_array = ((Dict) actualPList.getRootElement())
                        .getConfigurationArray("tips");

                LogService.Log("GetListOfTipsAsyncTask", "tips: " + tips_array);

                int rand_tip = rand.nextInt(tips_array.size());

                tip = ((MyString) tips_array.get(rand_tip)).getValue();

                Array quotes_array = ((Dict) actualPList.getRootElement())
                        .getConfigurationArray("quotes");

                LogService.Log("GetListOfTipsAsyncTask", "quotes_array: " + quotes_array);

                int rand_quote = rand.nextInt(quotes_array.size());

                quote = ((MyString) quotes_array.get(rand_quote)).getValue();

                apps = new ArrayList<App>();


                for (int i = 0; i < apps_array.size(); i++) {

                    App app = new App();

                    app.setName(((Dict) apps_array.get(i)).getConfiguration(
                            "name").getValue());
                    app.setUrl(((Dict) apps_array.get(i)).getConfiguration(
                            "url").getValue());

                    app.setImage(((Dict) apps_array.get(i)).getConfiguration(
                            "icon").getValue());

                    apps.add(app);
                }

                LogService.Log("GetListOfTipsAsyncTask","apps: "+apps.toString());

                return true;

            } catch (Throwable t) {
                LogService.Log("GetListOfTipsAsyncTask", "Throwable: " + t.toString());
                t.printStackTrace();
                return false;
            }

        }

        protected void onPostExecute(Boolean result) {
            if (result) {

                Type arrayListType = new TypeToken<ArrayList<App>>() {
                }.getType();
                Gson tmp = new Gson();

                SharedPreferences preferences = getSharedPreferences(
                        getString(R.string.PREFS), 1);

                preferences.edit().putString("tip", tip).commit();
                preferences.edit().putString("quote", quote).commit();

                LogService.Log("TAG", "apps size " + apps.size());
                preferences.edit()
                        .putString("apps", tmp.toJson(apps, arrayListType))
                        .commit();

            }
            isTipsDownloaded = true;

            goToTheNextScreen();

        }
    }

    private void goToTheNextScreen() {
        if (isAnimationFinished && isTipsDownloaded) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

}
