package com.zenlabs.sevenminuteworkout.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.adapter.MenuListAdapter;
import com.zenlabs.sevenminuteworkout.database.Achievement;
import com.zenlabs.sevenminuteworkout.database.CompletedWorkout;
import com.zenlabs.sevenminuteworkout.database.DatabaseHelper;
import com.zenlabs.sevenminuteworkout.database.Exercise;
import com.zenlabs.sevenminuteworkout.database.ExerciseForWorkout;
import com.zenlabs.sevenminuteworkout.database.Workout;
import com.zenlabs.sevenminuteworkout.fragment.GoProFragment;
import com.zenlabs.sevenminuteworkout.fragment.HomeFragment;
import com.zenlabs.sevenminuteworkout.fragment.WorkoutLogFragment;
import com.zenlabs.sevenminuteworkout.iap.utils.IabBroadcastReceiver;
import com.zenlabs.sevenminuteworkout.iap.utils.IabHelper;
import com.zenlabs.sevenminuteworkout.iap.utils.IabResult;
import com.zenlabs.sevenminuteworkout.iap.utils.Inventory;
import com.zenlabs.sevenminuteworkout.iap.utils.Purchase;
import com.zenlabs.sevenminuteworkout.utils.AchievementsCheckBadgesUtils;
import com.zenlabs.sevenminuteworkout.utils.App;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.MenuItemEnum;
import com.zenlabs.sevenminuteworkout.utils.MenuListItem;
import com.zenlabs.sevenminuteworkout.utils.MenuResponseManager;
import com.zenlabs.sevenminuteworkout.utils.ResponseManagerString;
import com.zenlabs.sevenminuteworkout.utils.TipsDialog;
import com.zenlabs.sevenminuteworkout.utils.UtilsMethods;
import com.zenlabs.sevenminuteworkout.utils.UtilsValues;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends FragmentActivity implements IabBroadcastReceiver.IabBroadcastListener {

    private DrawerLayout mainDrawerLayout;
    private LinearLayout menuMainLinearLayout;

    private ImageView menuImageView, documentImageView;
    private TextView actionBarTitleTextView;

    private DatabaseHelper databaseHelper;

    //menu screen
    private ImageView menuBackImageView;
    private ListView menuListView;
    private MenuListAdapter menuListAdapter;

    //home
    private HomeFragment homeFragment;
    private WorkoutLogFragment workoutLogFragment;

    //go pro
    private GoProFragment goProFragment;
    private View goProFramlayout;

    //iAP

    // Does the user have the premium upgrade?
    boolean isProSettingsAvailable = false;
    boolean isAlternativeAvailable = false;
    boolean isAdvancedAvailable = false;
    boolean isRunnersAvailable = false;
    boolean isAllInOneAvailable = false;

    public static final String SETTINGS_IAP_ITEM = "settings_iap_item";
    public static final String ALTERNATIVE_WORKOUT_IAP_ITEM = "alternative_workout_iap_item";
    public static final String ADVANCED_WORKOUT_IAP_ITEM = "advanced_workout_iap_item";
    public static final String RUNNERS_WORKOUT_IAP_ITEM = "runners_workout_iap_item";
    public static final String ALL_IN_ONE_IAP_ITEM = "all_in_one_iap_item";

//    // How many units (1/4 tank is our unit) fill in the tank.
//    static final int TANK_MAX = 4;
//
//    // Current amount of gas in tank, in units
//    int mTank;

    // The helper object
    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAggKI1l+taN2BcPvcFTgvC0Rrj77VvbTEKA5YP2hkdO5osVQRpgbjjwrdu5cJMoAYvKc+v";

    public static final String TAG_IAP = "===IAP_TAG===";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        base64EncodedPublicKey += "QVUh4X7zHJTpaad4L9Fj0Vg0xDZIxvP3mdGy8SCHjySyDuhtVykV0DDFtasSCjtRWX+1nOexz8so1B4Tnm";

        setUpiAP();

        initialiseItems();

//        initHomeFragment();

    }

    @Override
    public void onBackPressed() {

        if (goProFramlayout.getVisibility() == View.VISIBLE) {
            goProFramlayout.setVisibility(View.GONE);
            mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {

            if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                LogService.Log("onBackPressed", "drawer is open");
                hideMenu();
            } else {
                LogService.Log("onBackPressed", "drawer is closed");
                if (workoutLogFragment != null && workoutLogFragment.isVisible()) {
                    LogService.Log("onBackPressed", "drawer is visible");
                    initHomeFragment(actionBarTitleTextView.getHeight());
                    workoutLogFragment = null;
                } else {
//                finish();
                    super.onBackPressed();
                }
            }
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        initMenuList();

        if (databaseHelper != null) {
            databaseHelper.openDataBase();
        }

        if (homeFragment != null && homeFragment.isVisible()) {

            ArrayList<Achievement> achievements = getAchivements();
            ArrayList<CompletedWorkout> completedWorkouts = getCompletedWorkouts();
            homeFragment.setSpecificAchivementPosition(getSpecificAchivementPosition(achievements, AchievementsCheckBadgesUtils.ADJUST_WORKOUT_UNLOCK_BADGE_ID));
            homeFragment.setAchievements(achievements);
            homeFragment.setCompletedWorkouts(completedWorkouts);

            homeFragment.initTimes();

            homeFragment.updateVisibilityRightThreeLayouts();

        }

    }

    @Override
    protected void onPause() {

        super.onPause();

        if (databaseHelper != null) {
            databaseHelper.close();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogService.Log(TAG_IAP, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            LogService.Log(TAG_IAP, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void receivedBroadcast() {
        LogService.Log(TAG_IAP, "Received broadcast notification. Querying inventory.");
        mHelper.queryInventoryAsync(mGotInventoryListener);
    }

    private void setUpiAP() {

        base64EncodedPublicKey += "p5GvjNYYIEm5ibaduVG5sHDqmelhBt9LFm3rs5XmhAHQc2Tjj7YKpJQxY0qypVTdoGghjI/5onSBFqoDTAusD3bKcvHjyqu14ZP1xVRzWAUnuxVvh+OT8Y8ZGjkLJL612Gja0lDNdps86msD5jl/adqemVwgaojY+cJICW0MvGjUIh/6+Z3EovuuQ9XqAdwIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        LogService.Log(TAG_IAP, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                LogService.Log(TAG_IAP, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG_IAP, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            LogService.Log(TAG_IAP, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            LogService.Log(TAG_IAP, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase allInOnePurchase = inventory.getPurchase(ALL_IN_ONE_IAP_ITEM);
            isAllInOneAvailable = (allInOnePurchase != null && verifyDeveloperPayload(allInOnePurchase));
            LogService.Log(TAG_IAP, "IS isAllInOneAvailable " + isAllInOneAvailable);

            UtilsMethods.saveBooleanInSharedPreferences(MainActivity.this, ALL_IN_ONE_IAP_ITEM, isAllInOneAvailable);

            Purchase settingsPurchase = inventory.getPurchase(SETTINGS_IAP_ITEM);
            isProSettingsAvailable = (settingsPurchase != null && verifyDeveloperPayload(settingsPurchase));
            LogService.Log(TAG_IAP, "IS isProSettingsAvailable " + isProSettingsAvailable);

            UtilsMethods.saveBooleanInSharedPreferences(MainActivity.this, SETTINGS_IAP_ITEM, isProSettingsAvailable);

            Purchase alternativePurchase = inventory.getPurchase(ALTERNATIVE_WORKOUT_IAP_ITEM);
            isAlternativeAvailable = (alternativePurchase != null && verifyDeveloperPayload(alternativePurchase));
            LogService.Log(TAG_IAP, "IS isAlternativeAvailable " + isAlternativeAvailable);

            UtilsMethods.saveBooleanInSharedPreferences(MainActivity.this, ALTERNATIVE_WORKOUT_IAP_ITEM, isAlternativeAvailable);

            Purchase advancvedPurchase = inventory.getPurchase(ADVANCED_WORKOUT_IAP_ITEM);
            isAdvancedAvailable = (advancvedPurchase != null && verifyDeveloperPayload(advancvedPurchase));
            LogService.Log(TAG_IAP, "IS isAdvancedAvailable " + isAdvancedAvailable);

            UtilsMethods.saveBooleanInSharedPreferences(MainActivity.this, ADVANCED_WORKOUT_IAP_ITEM, isAdvancedAvailable);

            Purchase runnersPurchase = inventory.getPurchase(RUNNERS_WORKOUT_IAP_ITEM);
            isRunnersAvailable = (runnersPurchase != null && verifyDeveloperPayload(runnersPurchase));
            LogService.Log(TAG_IAP, "IS isRunnersAvailable " + isRunnersAvailable);

            UtilsMethods.saveBooleanInSharedPreferences(MainActivity.this, RUNNERS_WORKOUT_IAP_ITEM, isRunnersAvailable);

//            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
//            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
//            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
//                LogService.Log(TAG_IAP, "We have gas. Consuming it.");
//                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
//                return;
//            }

//            updateUi();
//            setWaitScreen(false);
//            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            LogService.Log(TAG_IAP, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            LogService.Log(TAG_IAP, "Purchase successful.");

            if (purchase.getSku().equals(SETTINGS_IAP_ITEM) || purchase.getSku().equals(ALTERNATIVE_WORKOUT_IAP_ITEM) || purchase.getSku().equals(ADVANCED_WORKOUT_IAP_ITEM) || purchase.getSku().equals(RUNNERS_WORKOUT_IAP_ITEM) || purchase.getSku().equals(ALL_IN_ONE_IAP_ITEM)) {
                // bought the premium upgrade!
                LogService.Log(TAG_IAP, "Purchase is: " + purchase.getSku());
                UtilsMethods.saveBooleanInSharedPreferences(MainActivity.this, purchase.getSku(), true);
                if (homeFragment != null) {
                    homeFragment.updateBottomView();
                }
                if (purchase.getSku().equals(SETTINGS_IAP_ITEM) || purchase.getSku().equals(ALL_IN_ONE_IAP_ITEM)) {
                    if (homeFragment != null) {
                        homeFragment.updateVisibilityRightThreeLayouts();
                    }
                }
            }

        }
    };

    void complain(String message) {
        Log.e(TAG_IAP, "**** TrivialDrive Error: " + message);
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

//    // Called when consumption is complete
//    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
//        public void onConsumeFinished(Purchase purchase, IabResult result) {
//            LogService.Log(TAG_IAP, "Consumption finished. Purchase: " + purchase + ", result: " + result);
//
//            // if we were disposed of in the meantime, quit.
//            if (mHelper == null) return;
//
//            // We know this is the "gas" sku because it's the only one we consume,
//            // so we don't check which sku was consumed. If you have more than one
//            // sku, you probably should check...
//            if (result.isSuccess()) {
//                // successfully consumed, so we apply the effects of the item in our
//                // game world's logic, which in our case means filling the gas tank a bit
//                LogService.Log(TAG_IAP, "Consumption successful. Provisioning.");
//                mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
////                saveData();
////                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
//            } else {
//                complain("Error while consuming: " + result);
//            }
////            updateUi();
////            setWaitScreen(false);
////            Log.d(TAG, "End consumption flow.");
//        }
//    };

    private void initHomeFragment(int listItemSize) {

        actionBarTitleTextView.setText("");
        documentImageView.setVisibility(View.VISIBLE);

        menuImageView.setImageResource(R.drawable.menu_btn);

        ArrayList<Workout> workouts = new ArrayList<Workout>();
        ArrayList<ExerciseForWorkout> exerciseForWorkouts = new ArrayList<ExerciseForWorkout>();
        ArrayList<Exercise> exercises = new ArrayList<Exercise>();

        try {
            databaseHelper = new DatabaseHelper(MainActivity.this);
            workouts = databaseHelper.getAllWorkouts();
            exerciseForWorkouts = databaseHelper.getAllExercisesForWorkout();
            exercises = databaseHelper.getAllExercises();
            LogService.Log("MainActivity", "workouts: " + workouts.toString());

        } catch (Exception ex) {
            LogService.Log("MainActivity", "db ex: " + ex.toString());
        }

        homeFragment = new HomeFragment();
        homeFragment.setHomeFragmentResponseManagerString(homeFragmentResponseManagerString);
        homeFragment.setGoProResponseManagerString(goProResponseManagerString);
        homeFragment.setWorkouts(workouts);
        homeFragment.setExerciseForWorkouts(exerciseForWorkouts);
        homeFragment.setExercises(exercises);
        homeFragment.setListItemViewSize(listItemSize);

        ArrayList<Achievement> achievements = getAchivements();
        ArrayList<CompletedWorkout> completedWorkouts = getCompletedWorkouts();
        homeFragment.setSpecificAchivementPosition(getSpecificAchivementPosition(achievements, AchievementsCheckBadgesUtils.ADJUST_WORKOUT_UNLOCK_BADGE_ID));
        homeFragment.setAchievements(achievements);
        homeFragment.setCompletedWorkouts(completedWorkouts);

        getFragmentManager().beginTransaction()
                .replace(R.id.mainScreenFragmetnFrameLayout,
                        homeFragment).commit();

    }

    private void initWorkoutLogFragment() {

        actionBarTitleTextView.setText(getResources().getString(R.string.workout_log_title));
        menuImageView.setImageResource(R.drawable.back_arrow);
        documentImageView.setVisibility(View.INVISIBLE);

        ArrayList<CompletedWorkout> completedWorkouts = new ArrayList<CompletedWorkout>();

        try {
            databaseHelper = new DatabaseHelper(MainActivity.this);
            completedWorkouts = databaseHelper.getAllCompletedWorkouts();
            LogService.Log("initWorkoutLogFragment", "completedWorkouts " + completedWorkouts.toString());
        } catch (Exception ex) {
            LogService.Log("initWorkoutLogFragment", "completedWorkouts ex: " + ex.toString());
        }

        workoutLogFragment = new WorkoutLogFragment();
        workoutLogFragment.setCompletedWorkouts(completedWorkouts);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainScreenFragmetnFrameLayout,
                        workoutLogFragment).commit();

    }

    private MenuResponseManager menuResponseManager = new MenuResponseManager() {
        @Override
        public void response(int id) {
            switch (id) {
                case MenuItemEnum.ADJUST_WORKOUTS:

                    ArrayList<Achievement> achievements = getAchivements();

                    if (UtilsMethods.isUnlocked(MainActivity.this, SETTINGS_IAP_ITEM) || AchievementsCheckBadgesUtils.showBadges(getSpecificAchivementPosition(achievements, AchievementsCheckBadgesUtils.ADJUST_WORKOUT_UNLOCK_BADGE_ID), achievements, getCompletedWorkouts())) {
                        //go to this screen
                        startActivity(new Intent(MainActivity.this, AdjustWorkoutsActivity.class));
                    } else {
                        showGoProFragment(SETTINGS_IAP_ITEM);
                    }

                    break;
                case MenuItemEnum.ULTIMATE_WARRIOR:
                    showGoProFragment("");
                    break;
                case MenuItemEnum.TICK_SOUNDS_EVERY_SECOND:
                    break;
                case MenuItemEnum.RESTORE_PURCHASES:
                    showGoProFragment("");
                    break;
                case MenuItemEnum.REMINDER:
                    ArrayList<Achievement> tmpAchievements = getAchivements();
                    if (UtilsMethods.isUnlocked(MainActivity.this, SETTINGS_IAP_ITEM) || AchievementsCheckBadgesUtils.showBadges(getSpecificAchivementPosition(tmpAchievements, AchievementsCheckBadgesUtils.WORKOUT_REMINDER_UNLOCK_BADGE_ID), tmpAchievements, getCompletedWorkouts())) {
                        startActivity(new Intent(MainActivity.this, ReminderActivity.class));
                    } else {
                        showGoProFragment(SETTINGS_IAP_ITEM);
                    }
                    break;
                case MenuItemEnum.FORUMS:
                    startActivity(new Intent(MainActivity.this, ForumActivity.class));
                    break;
                case MenuItemEnum.RATE_US:
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UtilsValues.GOOGLE_STORE_APP_URL + appPackageName)));
                    } catch (android.content.ActivityNotFoundException error) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UtilsValues.GOOGLE_STORE_URL + appPackageName)));
                    }
                    break;
                case MenuItemEnum.TIPS_SCREEN:
                    break;
                case MenuItemEnum.LEGAL_DISCLAIMER:
                    UtilsMethods.showLegalDisclaimer(MainActivity.this);
                    break;
                case MenuItemEnum.MORE_APPS:
                    if (UtilsMethods.hasInternetConnection(MainActivity.this)) {
                        startActivity(new Intent(MainActivity.this, MoreAppsActivity.class));
                    } else {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(getResources().getString(R.string.no_intertnet_connections))
                                .setMessage(getResources().getString(R.string.pls_check_intertnet_connections))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    break;
                case MenuItemEnum.FEEDBACK:

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_app)});
                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_feedback));

                    PackageInfo pInfo = null;
                    String versionName = "";
                    int versionCode = 0;
                    try {
                        pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        versionName = pInfo.versionName;
                        versionCode = pInfo.versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    String text = getResources().getString(R.string.feedback_app_name) + "\n" + getResources().getString(R.string.app_version) + " " + versionCode + "/" + versionName + "\n" + getResources().getString(R.string.device_model) + " " + Build.MANUFACTURER.toUpperCase() + ", " + Build.MODEL + "\n" + getResources().getString(R.string.os_version) + " " + android.os.Build.VERSION.RELEASE;

                    intent.putExtra(Intent.EXTRA_TEXT, text);

                    startActivity(Intent.createChooser(intent, getResources().getString(R.string.send_email)));

                    break;
                case MenuItemEnum.SHARE:
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
//                    sharingIntent.setType("message/rfc822");
//                    sharingIntent.setType("text/html");
                    String shareBody = getResources().getString(R.string.share_text);
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_subject));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

//                    https://play.google.com/store/apps/details?id=com.c10kforpink&hl=en

                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                    break;
                case MenuItemEnum.KIIP:

                    break;
            }
        }
    };

    private void initialiseItems() {

        mainDrawerLayout = (DrawerLayout) findViewById(R.id.mainScreenDraweLayout);
        menuMainLinearLayout = (LinearLayout) findViewById(R.id.mainMenuScreenDrawer);

        mainDrawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mainDrawerLayout.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        int width = mainDrawerLayout.getWidth();
                        menuMainLinearLayout.getLayoutParams().width = width;
                    }
                });

        menuImageView = (ImageView) findViewById(R.id.mainScreenActionBarMenuImageView);
        menuImageView.setOnClickListener(menuImageViewOnClickListener);

        actionBarTitleTextView = (TextView) findViewById(R.id.mainScreenActionBarTitleTextView);

        documentImageView = (ImageView) findViewById(R.id.mainScreenActionBarGoProImageView);
        documentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Achievement> achievements = getAchivements();

                if (UtilsMethods.isUnlocked(MainActivity.this, SETTINGS_IAP_ITEM) || AchievementsCheckBadgesUtils.showBadges(getSpecificAchivementPosition(achievements, AchievementsCheckBadgesUtils.WORKOUT_LOG_UNLOCK_BADGE_ID), achievements, getCompletedWorkouts())) {
                    initWorkoutLogFragment();
                } else {
                    showGoProFragment(SETTINGS_IAP_ITEM);
                }

            }
        });

        menuBackImageView = (ImageView) findViewById(R.id.menuScreenActionBarBackImageView);
        menuBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });

        menuListView = (ListView) findViewById(R.id.menuScreenListView);

        initMenuList();

        shoStartPopUp();

        ViewTreeObserver vto = actionBarTitleTextView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                actionBarTitleTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                actionBarTitleTextView.setText("");
                initHomeFragment(actionBarTitleTextView.getHeight());
            }
        });

        goProFramlayout = findViewById(R.id.mainScreenGoProFramlayout);

    }

    private void shoStartPopUp() {

        if (UtilsMethods.getBooleanFromSharedPreferences(MainActivity.this, UtilsValues.SHARED_PREFERENCES_INTRO_SCREEN, true)) {
            UtilsMethods.saveBooleanInSharedPreferences(MainActivity.this, UtilsValues.SHARED_PREFERENCES_INTRO_SCREEN, false);
            UtilsMethods.showTipScreenDialog(MainActivity.this);
        } else {
            checkForShowTips();
        }

    }

    private void initMenuList() {

        ArrayList<MenuListItem> menuItems = new ArrayList<MenuListItem>();

        menuItems.add(new MenuListItem(MenuItemEnum.ADJUST_WORKOUTS, R.drawable.adjust_workouts, getResources().getString(R.string.adjust_workouts)));
        menuItems.add(new MenuListItem(MenuItemEnum.ULTIMATE_WARRIOR, R.drawable.update_warrior, getResources().getString(R.string.ultimate_warrior)));
        menuItems.add(new MenuListItem(MenuItemEnum.TICK_SOUNDS_EVERY_SECOND, R.drawable.tick_sounds_every_second, getResources().getString(R.string.tick_sounds_every_second)));
        menuItems.add(new MenuListItem(MenuItemEnum.RESTORE_PURCHASES, R.drawable.restore_purchases, getResources().getString(R.string.restore_purchases)));
        menuItems.add(new MenuListItem(MenuItemEnum.REMINDER, R.drawable.reminder, getResources().getString(R.string.reminder)));
        menuItems.add(new MenuListItem(MenuItemEnum.FORUMS, R.drawable.forums, getResources().getString(R.string.forums)));
        menuItems.add(new MenuListItem(MenuItemEnum.RATE_US, R.drawable.rate_us, getResources().getString(R.string.rate_us)));
        menuItems.add(new MenuListItem(MenuItemEnum.TIPS_SCREEN, R.drawable.tips_screen, getResources().getString(R.string.tips_screen)));
        menuItems.add(new MenuListItem(MenuItemEnum.LEGAL_DISCLAIMER, R.drawable.legal_disclaimer, getResources().getString(R.string.legal_disclaimer)));
        menuItems.add(new MenuListItem(MenuItemEnum.MORE_APPS, R.drawable.more_apps, getResources().getString(R.string.more_apps)));
        menuItems.add(new MenuListItem(MenuItemEnum.FEEDBACK, R.drawable.feedback, getResources().getString(R.string.feedback)));
        menuItems.add(new MenuListItem(MenuItemEnum.SHARE, R.drawable.share, getResources().getString(R.string.share)));
        if (UtilsMethods.isUnlocked(MainActivity.this, SETTINGS_IAP_ITEM)) {
            menuItems.add(new MenuListItem(MenuItemEnum.KIIP, R.drawable.tips_screen, getResources().getString(R.string.kiip_rewards)));
        }

        menuListAdapter = new MenuListAdapter(MainActivity.this, menuItems, menuResponseManager);
        menuListView.setAdapter(menuListAdapter);

    }

    private View.OnClickListener menuImageViewOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (workoutLogFragment != null && workoutLogFragment.isVisible()) {
                initHomeFragment(actionBarTitleTextView.getHeight());
                workoutLogFragment = null;
            } else {
                mainDrawerLayout.openDrawer(menuMainLinearLayout);
            }
        }
    };

    private void hideMenu() {
        mainDrawerLayout.closeDrawers();
    }

    private void checkForShowTips() {

        if (UtilsMethods.getBooleanFromSharedPreferences(MainActivity.this, UtilsValues.SHARED_PREFERENCES_TIPS_SCREEN)) {

            String tip = "", quote = "";
            ArrayList<App> apps;

            final SharedPreferences preferences = getSharedPreferences(getString(R.string.PREFS), 1);
//        String tip = preferences.getString("tip", "");
            quote = preferences.getString("quote", "");
            Log.d("QUOTE", " original::" + quote);
            Gson tmp = new Gson();
            Type arrayListType = new TypeToken<ArrayList<App>>() {
            }.getType();
            apps = tmp.fromJson(preferences.getString("apps", ""), arrayListType);

            LogService.Log("checkForShowTips", "apps: " + apps);

            FragmentManager fm = getSupportFragmentManager();
            TipsDialog tipsDialog = new TipsDialog();
            tipsDialog.setQuote(quote);
            tipsDialog.setApps(apps);
            tipsDialog.setStyle(DialogFragment.STYLE_NORMAL,
                    R.style.Dialog_TipScreenDialog);
            tipsDialog.show(fm, "fragment_tips");

        }
    }

    public void setTitleActionBar(String title) {
        actionBarTitleTextView.setText(title);
    }

    private ResponseManagerString homeFragmentResponseManagerString = new ResponseManagerString() {
        @Override
        public void responseArrived(String text) {
            setTitleActionBar(text);
        }
    };

    private ResponseManagerString iApResponseManagerString = new ResponseManagerString() {
        @Override
        public void responseArrived(String text) {

            String payload = "";
            LogService.Log("iApResponseManagerString", "text: -" + text + "-");
            mHelper.launchPurchaseFlow(MainActivity.this, text, UtilsValues.RC_REQUEST,
                    mPurchaseFinishedListener, payload);

        }
    };

    private ResponseManagerString goProResponseManagerString = new ResponseManagerString() {
        @Override
        public void responseArrived(String text) {

            LogService.Log("goProResponseManagerString", "text: -" + text + "-");
            showGoProFragment(text);

        }
    };

    private void showGoProFragment(String iAPItem) {

        hideMenu();

        goProFragment = new GoProFragment();
        goProFragment.setBuyOptionIapItem(iAPItem);
        goProFragment.setWorkoutName(actionBarTitleTextView.getText().toString());
        goProFragment.setiApResponseManagerString(iApResponseManagerString);
        goProFragment.setOnBackPressedGoProFragmentResponseManagerString(onBackPressedGoProFragmentResponseManagerString);
        goProFragment.setInvetoryQueryResponseManagerString(invetoryQueryResponseManagerString);

        mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        goProFramlayout.setVisibility(View.VISIBLE);
        getFragmentManager().beginTransaction()
                .replace(R.id.mainScreenGoProFramlayout,
                        goProFragment).commit();

    }

    private ResponseManagerString onBackPressedGoProFragmentResponseManagerString = new ResponseManagerString() {
        @Override
        public void responseArrived(String text) {
            onBackPressed();
        }
    };

    private ResponseManagerString invetoryQueryResponseManagerString = new ResponseManagerString() {
        @Override
        public void responseArrived(String text) {

            if (mHelper != null) {
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (Exception ex) {
                    LogService.Log("invetoryQueryResponseManagerString", "ex: " + ex.toString());
                }

            }
        }
    };

    //iAP unclock from achivements
    private ArrayList<Achievement> getAchivements() {

        ArrayList<Achievement> achievements = new ArrayList<Achievement>();

        try {
            databaseHelper = new DatabaseHelper(MainActivity.this);

            achievements = databaseHelper.getAllAchivements();
            LogService.Log("MainActivity", "achievements: " + achievements.toString());

        } catch (Exception ex) {
            LogService.Log("MainActivity", "db ex: " + ex.toString());
        }

        return achievements;

    }

    private ArrayList<CompletedWorkout> getCompletedWorkouts() {

        ArrayList<CompletedWorkout> completedWorkouts = new ArrayList<CompletedWorkout>();

        try {

            databaseHelper = new DatabaseHelper(MainActivity.this);
            completedWorkouts = databaseHelper.getAllCompletedWorkouts();
            LogService.Log("MainActivity", "completedWorkouts: " + completedWorkouts.toString());

        } catch (Exception ex) {
            LogService.Log("MainActivity", "db ex: " + ex.toString());
        }

        Collections.sort(completedWorkouts);

        return completedWorkouts;

    }

    private int getSpecificAchivementPosition(ArrayList<Achievement> achievements, int specificId) {
        for (int i = 0; i < achievements.size(); ++i) {
            if (achievements.get(i).getUnlockId() == specificId) {
                return i;
            }
        }
        return -1;
    }

}
