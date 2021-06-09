package com.zenlabs.sevenminuteworkout.database;

/**
 * Created by madarashunor on 12/10/15.
 */
public class ExerciseForWorkout {

    private int workoutid;
    private int exerciseId;

    public ExerciseForWorkout() {
    }

    public ExerciseForWorkout(int workoutid, int exerciseId) {
        this.workoutid = workoutid;
        this.exerciseId = exerciseId;
    }

    public int getWorkoutid() {
        return workoutid;
    }

    public void setWorkoutid(int workoutid) {
        this.workoutid = workoutid;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    @Override
    public String toString() {
        return "ExerciseForWorkout{" +
                "workoutid=" + workoutid +
                ", exerciseId=" + exerciseId +
                '}';
    }

}
