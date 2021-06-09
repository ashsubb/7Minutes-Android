package com.zenlabs.sevenminuteworkout.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.activity.MainActivity;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.ResponseManagerString;

public class GoProFragment extends Fragment {

    private ImageView closeImageView;
    private TextView actionBarTitleTextView;
    private TextView titleListTextView;
    private TextView buttonsSeparatorTextView;
    private LinearLayout ultimateWarriorListItemLinearLayout;
    private Button buyUltimateButton;
    private Button buyOptionButton;

    private ResponseManagerString onBackPressedGoProFragmentResponseManagerString;
    private ResponseManagerString iApResponseManagerString;
    private ResponseManagerString invetoryQueryResponseManagerString;

    private String buyOptionIapItem = "";
    private String workoutName = "";

    public GoProFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_go_pro,
                null);

        LinearLayout mainContainerLinearLayout = (LinearLayout) view.findViewById(R.id.goProScreenMainLinearLayout);
        mainContainerLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        closeImageView = (ImageView) view.findViewById(R.id.menuScreenActionBarBackImageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedGoProFragmentResponseManagerString.responseArrived("");
            }
        });

        actionBarTitleTextView = (TextView) view.findViewById(R.id.goProScreenActionBarTitleTextView);
        titleListTextView = (TextView) view.findViewById(R.id.goProTitleListTextView);
        ultimateWarriorListItemLinearLayout = (LinearLayout) view.findViewById(R.id.goProUltimateWarListItemLinearLayout);

        buyUltimateButton = (Button) view.findViewById(R.id.goProScreenUltimateWarriorButton);
        buyUltimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iApResponseManagerString.responseArrived(MainActivity.ALL_IN_ONE_IAP_ITEM);
            }
        });

        buttonsSeparatorTextView = (TextView) view.findViewById(R.id.goProScreenOrJustTextView);
        buyOptionButton = (Button) view.findViewById(R.id.goProScreenBuyOptionButton);

        setUpBuyOptins();

        buyOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iApResponseManagerString.responseArrived(buyOptionIapItem);
            }
        });

        return view;

    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getBuyOptionIapItem() {
        return buyOptionIapItem;
    }

    public void setBuyOptionIapItem(String buyOptionIapItem) {
        this.buyOptionIapItem = buyOptionIapItem;
    }

    public ResponseManagerString getiApResponseManagerString() {
        return iApResponseManagerString;
    }

    public void setiApResponseManagerString(ResponseManagerString iApResponseManagerString) {
        this.iApResponseManagerString = iApResponseManagerString;
    }

    public ResponseManagerString getOnBackPressedGoProFragmentResponseManagerString() {
        return onBackPressedGoProFragmentResponseManagerString;
    }

    public void setOnBackPressedGoProFragmentResponseManagerString(ResponseManagerString onBackPressedGoProFragmentResponseManagerString) {
        this.onBackPressedGoProFragmentResponseManagerString = onBackPressedGoProFragmentResponseManagerString;
    }

    public ResponseManagerString getInvetoryQueryResponseManagerString() {
        return invetoryQueryResponseManagerString;
    }

    public void setInvetoryQueryResponseManagerString(ResponseManagerString invetoryQueryResponseManagerString) {
        this.invetoryQueryResponseManagerString = invetoryQueryResponseManagerString;
    }

    private void setUpBuyOptins(){

        if (!buyOptionIapItem.equals("")) {

            RelativeLayout.LayoutParams buyOptionButtonLayoutParams = (RelativeLayout.LayoutParams) buyOptionButton.getLayoutParams();
            buyOptionButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            buyOptionButton.setLayoutParams(buyOptionButtonLayoutParams);
            buyOptionButton.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams buttonsSeparatorTextViewLayoutParams = (RelativeLayout.LayoutParams) buttonsSeparatorTextView.getLayoutParams();
            buttonsSeparatorTextViewLayoutParams.addRule(RelativeLayout.ABOVE, R.id.goProScreenBuyOptionButton);
            buttonsSeparatorTextView.setLayoutParams(buttonsSeparatorTextViewLayoutParams);
            buttonsSeparatorTextView.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams buyUltimateButtonLayoutParams = (RelativeLayout.LayoutParams) buyUltimateButton.getLayoutParams();
            buyUltimateButtonLayoutParams.addRule(RelativeLayout.ABOVE, R.id.goProScreenOrJustTextView);
            buyUltimateButtonLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            buyUltimateButton.setLayoutParams(buyUltimateButtonLayoutParams);

            setUpTextForBuyOptionButton();

        }

    }

    private void setUpTextForBuyOptionButton(){

        String text="";

        LogService.Log("GoProFragment","setUpTextForBuyOptionButton buyOptionIapItem: -"+buyOptionIapItem+"- -"+MainActivity.SETTINGS_IAP_ITEM+"-");

        if(buyOptionIapItem.equals(MainActivity.SETTINGS_IAP_ITEM)){
            text = getResources().getString(R.string.buy)+" "+getResources().getString(R.string.pro_settings_monney);
            LogService.Log("GoProFragment","setUpTextForBuyOptionButton SETTINGS_IAP_ITEM text: "+text);
        }
        else if(buyOptionIapItem.equals(MainActivity.ALTERNATIVE_WORKOUT_IAP_ITEM) || buyOptionIapItem.equals(MainActivity.ADVANCED_WORKOUT_IAP_ITEM) || buyOptionIapItem.equals(MainActivity.RUNNERS_WORKOUT_IAP_ITEM)){
            text = getResources().getString(R.string.buy)+" "+ workoutName.toUpperCase() + " "+getResources().getString(R.string.workout)+" "+getResources().getString(R.string.dollar_zero_nine_nine);
            LogService.Log("GoProFragment","setUpTextForBuyOptionButton other text: "+text);
        }

        buyOptionButton.setText(text);

    }

}
