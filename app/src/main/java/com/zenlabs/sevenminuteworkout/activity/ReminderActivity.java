package com.zenlabs.sevenminuteworkout.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wefika.horizontalpicker.HorizontalPicker;
import com.zenlabs.sevenminuteworkout.R;
import com.zenlabs.sevenminuteworkout.utils.LogService;
import com.zenlabs.sevenminuteworkout.utils.ReminderBroadcastReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ReminderActivity extends Activity {

    private ImageView closeImageView;
    private boolean isAm = false;
    private ArrayList<String> hoursArrayList, minutesArrayList;
    private HorizontalPicker hoursHorizontalPicker;
    private HorizontalPicker minutesHorizontalPicker;
    private TextView amTextView, pmTextView;
    private Button deleteButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        initialiseViews();

    }

    private void initialiseViews(){

        closeImageView = (ImageView) findViewById(R.id.reminderScreenActionBarBackImageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        hoursArrayList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.hour_values)));
        minutesArrayList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.minute_values)));

        hoursHorizontalPicker = (HorizontalPicker) findViewById(R.id.reminderActivityHourSpinner);
        minutesHorizontalPicker = (HorizontalPicker) findViewById(R.id.reminderActivityMinutesSpinner);

        amTextView = (TextView) findViewById(R.id.reminderActivityAmTextView);
        amTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAm = true;
                setAmPm();
            }
        });

        pmTextView = (TextView) findViewById(R.id.reminderActivityPmTextView);
        pmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAm = false;
                setAmPm();
            }
        });

        initTime(Calendar.getInstance());

        deleteButton = (Button) findViewById(R.id.reminderActivityDeleteButton);
        deleteButton.setOnClickListener(deleteButtonOnClickListener);
        saveButton = (Button) findViewById(R.id.reminderActivitySaveButton);
        saveButton.setOnClickListener(saveButtonOnClickListener);

    }

    private void initTime(Calendar datetime){

        if (datetime.get(Calendar.AM_PM) == Calendar.AM) {
            isAm = true;
        }
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM) {
            isAm = false;
        }

        int hour = datetime.get(Calendar.HOUR);
        int minute = datetime.get(Calendar.MINUTE);

        setPicker(hour,hoursArrayList,hoursHorizontalPicker);
        setPicker(minute,minutesArrayList,minutesHorizontalPicker);

        setAmPm();

    }

    private void setAmPm(){

        if(isAm){
            amTextView.setBackgroundColor(getResources().getColor(R.color.blue_button_color));
            pmTextView.setBackgroundColor(getResources().getColor(R.color.lighter_gray_blue_menu_background_color));
        }
        else{
            pmTextView.setBackgroundColor(getResources().getColor(R.color.blue_button_color));
            amTextView.setBackgroundColor(getResources().getColor(R.color.lighter_gray_blue_menu_background_color));
        }

    }

    private void setPicker(int setValue, ArrayList<String> arrayValues, HorizontalPicker picker){
        for(int i=0;i<arrayValues.size();++i){
            if(arrayValues.get(i).equals(setValue+"")){
                picker.setSelectedItem(i);
                break;
            }
        }
    }

    private View.OnClickListener deleteButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ReminderBroadcastReceiver.cancelAlarm(ReminderActivity.this);
            onBackPressed();
        }
    };

    private View.OnClickListener saveButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Calendar calendar = Calendar.getInstance();

            if(isAm){
                calendar.set(Calendar.AM_PM,Calendar.AM);
            }
            else{
                calendar.set(Calendar.AM_PM,Calendar.PM);
            }

            //bug library
            String hour = hoursArrayList.get(hoursHorizontalPicker.getSelectedItem());
            String minute = minutesArrayList.get(minutesHorizontalPicker.getSelectedItem());

            calendar.set(Calendar.HOUR, Integer.valueOf(hour));
            calendar.set(Calendar.MINUTE, Integer.valueOf(minute));




            LogService.Log("saveButtonOnClickListener","hour: "+hour+ " minute: "+minute);

            LogService.Log("saveButtonOnClickListener","calendar: "+calendar.toString());

            ReminderBroadcastReceiver.setAlarm(ReminderActivity.this, calendar);

            onBackPressed();

        }
    };


}
