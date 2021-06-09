package com.zenlabs.sevenminuteworkout.database;

import java.util.Calendar;

/**
 * Created by madarashunor on 06/11/15.
 */
public class CompletedWorkout implements Comparable<CompletedWorkout> {

    private int completedWorkoutId;
    private int workoutId;
    private String date;
    private int cycles;

    public CompletedWorkout(){
    }

    public CompletedWorkout(int completedWorkoutId, int workoutId, String date, int cycles) {
        this.completedWorkoutId = completedWorkoutId;
        this.workoutId = workoutId;
        this.date = date;
        this.cycles = cycles;
    }

    public int getCompletedWorkoutId() {
        return completedWorkoutId;
    }

    public void setCompletedWorkoutId(int completedWorkoutId) {
        this.completedWorkoutId = completedWorkoutId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    @Override
    public String toString() {
        return "CompletedWorkout{" +
                "completedWorkoutId=" + completedWorkoutId +
                ", workoutId=" + workoutId +
                ", date='" + date + '\'' +
                ", cycles=" + cycles +
                '}';
    }

    @Override
    public int compareTo(CompletedWorkout another) {

        Calendar lhsCalendar = Calendar.getInstance();
        lhsCalendar.setTimeInMillis(Long.valueOf(getDate()));

        Calendar rhsCalendar = Calendar.getInstance();
        rhsCalendar.setTimeInMillis(Long.valueOf(another.getDate()));

        return lhsCalendar.compareTo(rhsCalendar);

    }
}
