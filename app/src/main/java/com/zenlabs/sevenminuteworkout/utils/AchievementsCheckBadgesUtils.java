package com.zenlabs.sevenminuteworkout.utils;

import com.zenlabs.sevenminuteworkout.database.Achievement;
import com.zenlabs.sevenminuteworkout.database.CompletedWorkout;
import com.zenlabs.sevenminuteworkout.database.PeriodTypeEnum;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by madarashunor on 05/02/16.
 */
public class AchievementsCheckBadgesUtils {

    public static final int WORKOUT_LOG_UNLOCK_BADGE_ID = 1;
    public static final int WORKOUT_REMINDER_UNLOCK_BADGE_ID = 2;
    public static final int ADJUST_WORKOUT_UNLOCK_BADGE_ID = 3;

    public static boolean showBadges(int position, ArrayList<Achievement> items, ArrayList<CompletedWorkout> completedWorkouts) {

        boolean canBeDispalyed = false;

        if(position==-1){
            return canBeDispalyed;
        }

        LogService.Log("AchievemetnsListAdapter showBadges", "position: " + "item: " + items.get(position).toString());

        if (items.get(position).getPeriodType().equals(PeriodTypeEnum.NOT_SPEC)) {
            if (completedWorkouts.size() >= items.get(position).getTime()) {
                canBeDispalyed = true;
            }
        }

        if (items.get(position).getPeriodType().equals(PeriodTypeEnum.DAY)) {

            int valueOfPeriodicality = items.get(position).getPeriodTime();

//            LogService.Log("AchievemetnsListAdapter showBadges", "DAY");

            long day = (24 * 60 * 60 * 1000);

            Calendar prevCalendar = null;

            for (int i = 0; i < completedWorkouts.size(); ++i) {

                CompletedWorkout slcCompletedWorkout = completedWorkouts.get(i);
                Calendar slcCalendar = Calendar.getInstance();
                slcCalendar.setTimeInMillis(Long.valueOf(slcCompletedWorkout.getDate()));

                if (prevCalendar == null) {
                    prevCalendar = Calendar.getInstance();
                    prevCalendar.setTimeInMillis(Long.valueOf(slcCompletedWorkout.getDate()));
                    --valueOfPeriodicality;
                }

                if (!prevCalendar.equals(slcCalendar)) {

//                    LogService.Log("AchievemetnsListAdapter showBadges", "DAY not EQUALS DATES");

                    if (prevCalendar.before(slcCalendar)) {
//                        long diff = slcCalendar.getTimeInMillis() - prevCalendar.getTimeInMillis();
                        int diff = slcCalendar.get(Calendar.DAY_OF_YEAR) - prevCalendar.get(Calendar.DAY_OF_YEAR);
//                            diff >= day && diff < (2 * day)
                        if (diff == 1) {
                            --valueOfPeriodicality;
                        } else if (diff >= 2) {
                            prevCalendar = null;
                            valueOfPeriodicality = items.get(position).getPeriodTime();
                        }
//                        LogService.Log("AchievemetnsListAdapter showBadges", "DAY IS BEFORE DATE " + " valueOfPeriodicality: " + valueOfPeriodicality + " day: " + day + " diff: " + diff);
                    }
                }

                if (valueOfPeriodicality == 0) {
                    int timeRep = checkTimeRepeticion(items.get(position).getTime(), slcCalendar, completedWorkouts);
//                    LogService.Log("AchievemetnsListAdapter showBadges", " checkTimeRepeticion: " + timeRep);
                    if (timeRep <= 0) {
                        canBeDispalyed = true;
                        break;
                    }
                }

                if (prevCalendar != null) {
                    prevCalendar.setTimeInMillis(Long.valueOf(slcCompletedWorkout.getDate()));
                }

            }
        }


        if (items.get(position).getPeriodType().equals(PeriodTypeEnum.MONTH)) {

            LogService.Log("AchievemetnsListAdapter showBadges", "IS MONTH");


            int valueOfPeriodicality = items.get(position).getPeriodTime();

            int dayOFMonth = 30 * valueOfPeriodicality;

            Calendar prevCalendar = null;

            for (int i = 0; i < completedWorkouts.size(); ++i) {

                CompletedWorkout slcCompletedWorkout = completedWorkouts.get(i);
                Calendar slcCalendar = Calendar.getInstance();
                slcCalendar.setTimeInMillis(Long.valueOf(slcCompletedWorkout.getDate()));

                if (prevCalendar == null) {
                    prevCalendar = Calendar.getInstance();
                    prevCalendar.setTimeInMillis(Long.valueOf(slcCompletedWorkout.getDate()));
                    --dayOFMonth;
                }

                int diff = slcCalendar.get(Calendar.DAY_OF_YEAR) - prevCalendar.get(Calendar.DAY_OF_YEAR);

                if (diff == 1) {
                    --dayOFMonth;
                    LogService.Log("AchievemetnsListAdapter showBadges", "MONTH valueOfPeriodicality: " + dayOFMonth);
                } else if (diff >= 2) {
                    LogService.Log("AchievemetnsListAdapter showBadges", "MONTH reset valueOfPeriodicality: " + dayOFMonth);
                    prevCalendar = null;
                    dayOFMonth = 30 * valueOfPeriodicality;
                }

                if (prevCalendar != null) {
                    prevCalendar.setTimeInMillis(Long.valueOf(slcCompletedWorkout.getDate()));
                }

                if (dayOFMonth <= 0) {
                    canBeDispalyed = true;
                    break;
                }

            }

        }

        if (items.get(position).getPeriodType().equals(PeriodTypeEnum.MONTH) || canBeDispalyed) {
            LogService.Log("AchievemetnsListAdapter showBadges", "MONTH GOOD");
        }

        return canBeDispalyed;

    }

    private static int checkTimeRepeticion(int timeValue, Calendar slcCalendar, ArrayList<CompletedWorkout> completedWorkouts) {

        for (int i = 0; i < completedWorkouts.size(); ++i) {

            Calendar tmpCalendar = Calendar.getInstance();
            tmpCalendar.setTimeInMillis(Long.valueOf(completedWorkouts.get(i).getDate()));

            if (slcCalendar.get(Calendar.YEAR) == tmpCalendar.get(Calendar.YEAR) && slcCalendar.get(Calendar.DAY_OF_YEAR) == tmpCalendar.get(Calendar.DAY_OF_YEAR)) {
                --timeValue;
            }

        }

        return timeValue;
    }


}
