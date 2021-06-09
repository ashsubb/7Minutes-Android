package com.zenlabs.sevenminuteworkout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.zenlabs.sevenminuteworkout.utils.LogService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static String DATABASE_NAME = "7minwork.db";
    public SQLiteDatabase database;

    //tables
    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String TABLE_EXERCISE = "exercises";
    public static final String TABLE_WORKOUTS = "workout";
    public static final String TABLE_EXERCISE_FOR_WORK = "exercises_for_workout";
    public static final String TABLE_UNLOCK_ITEMS = "unlock_items";
    public static final String TABLE_COMLETED_WORKOUT = "comleted_workout";

    //columns
    public static final String ACHIEVEMENTS_COLUMN_ID = "achievements_id";
    public static final String ACHIEVEMENTS_COLUMN_NAMNE = "name";
    public static final String ACHIEVEMENTS_COLUMN_UNLOCK_ID = "unlock_i_id";
    public static final String ACHIEVEMENTS_COLUMN_SUBTEXT = "subtext";
    public static final String ACHIEVEMENTS_COLUMN_TIME = "time";
    public static final String ACHIEVEMENTS_COLUMN_PERIOD_TIME = "period_time";
    public static final String ACHIEVEMENTS_COLUMN_PERIOD_TYPE = "period_type";
    public static final String ACHIEVEMENTS_COLUMN_IMAGE_ID = "image_id";

    private String[] achivementsAllColumns = {ACHIEVEMENTS_COLUMN_ID,
            ACHIEVEMENTS_COLUMN_NAMNE, ACHIEVEMENTS_COLUMN_UNLOCK_ID, ACHIEVEMENTS_COLUMN_SUBTEXT, ACHIEVEMENTS_COLUMN_TIME, ACHIEVEMENTS_COLUMN_PERIOD_TIME, ACHIEVEMENTS_COLUMN_PERIOD_TYPE, ACHIEVEMENTS_COLUMN_IMAGE_ID};

    public static final String EXERCISE_COLUMN_ID = "exercises_id";
    public static final String EXERCISE_COLUMN_NAME = "name";
    public static final String EXERCISE_COLUMN_YOUTUBE_LINK = "youtube_links";
    public static final String EXERCISE_COLUMN_DESC_PRE = "descripton_preparation";
    public static final String EXERCISE_COLUMN_DESC_EXE = "descripton_execution";
    public static final String EXERCISE_COLUMN_BIG_IMAGE_NAME = "big_image_name";
    public static final String EXERCISE_COLUMN_FLIP_1_IMAGE_NAME = "image_flip_1_name";
    public static final String EXERCISE_COLUMN_FLIP_2_IMAGE_NAME = "image_flip_2_name";
    public static final String EXERCISE_COLUMN_FLIP_3_IMAGE_NAME = "image_flip_3_name";
    public static final String EXERCISE_COLUMN_SOUND = "sound";

    private String[] exercisesAllColumns = {EXERCISE_COLUMN_ID, EXERCISE_COLUMN_NAME, EXERCISE_COLUMN_YOUTUBE_LINK, EXERCISE_COLUMN_DESC_PRE, EXERCISE_COLUMN_DESC_EXE, EXERCISE_COLUMN_BIG_IMAGE_NAME, EXERCISE_COLUMN_FLIP_1_IMAGE_NAME, EXERCISE_COLUMN_FLIP_2_IMAGE_NAME, EXERCISE_COLUMN_FLIP_3_IMAGE_NAME, EXERCISE_COLUMN_SOUND};

    public static final String EXERCISE_COLUMN_FOR_WORK_WORK_ID = "workout_id";
    public static final String EXERCISE_COLUMN_FOR_WORK_EXE_ID = "exercises_id";

    private String[] exercisesForWorkoutAllColumns = {EXERCISE_COLUMN_FOR_WORK_WORK_ID, EXERCISE_COLUMN_FOR_WORK_EXE_ID};

    public static final String UNLOCK_COLUMN_ITEMS_ID = "unlock_i_id";
    public static final String UNLOCK_COLUMN_ITEMS_NAME = "name";

    private String[] unlockItemAllColumns = {UNLOCK_COLUMN_ITEMS_ID, UNLOCK_COLUMN_ITEMS_NAME};

    public static final String WORKOUTS_COLUMN_ID = "workout_id";
    public static final String WORKOUTS_COLUMN_NAME = "name";
    public static final String WORKOUTS_COLUMN_UNLOCK_ITEM = "unlock_item";

    private String[] workoutAllColumns = {WORKOUTS_COLUMN_ID, WORKOUTS_COLUMN_NAME, WORKOUTS_COLUMN_UNLOCK_ITEM};

    public static final String COMPLETED_WORKOUT_COLUMN_ID = "completed_workout_id";
    public static final String COMPLETED_WORKOUT_WORKOUT_ID = "workout_id";
    public static final String COMPLETED_WORKOUT_DATE = "date";
    public static final String COMPLETED_WORKOUT_CYCLES = "cycles";

    private String[] workoutCompletedAllColumns = {COMPLETED_WORKOUT_COLUMN_ID, COMPLETED_WORKOUT_WORKOUT_ID, COMPLETED_WORKOUT_DATE, COMPLETED_WORKOUT_CYCLES};

    public DatabaseHelper(Context context) throws IOException {

        super(context, DATABASE_NAME, null, 1);
        this.context = context;

        boolean dbexist = checkDataBase();

        if (dbexist) {
            openDataBase();
        } else {
            LogService.Log("DatabaseHelper", "Database doesn't exist");
            createDataBase();
            openDataBase();
        }

        if (database != null && database.isOpen()) {
            LogService.Log("DatabaseHelper", "database loaded");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getWritableDatabase();
            SQLiteDatabase cursor = context.openOrCreateDatabase(DATABASE_NAME,
                    Context.MODE_PRIVATE, null);
            cursor.close();
            try {
                copyDataBase();
            } catch (IOException mIOException) {
                mIOException.printStackTrace();
                throw new Error("Error copying database");
            } finally {
                this.close();
            }
        }
    }

    private boolean checkDataBase() {
        try {
            final String mPath = context.getDatabasePath(DATABASE_NAME).getPath();
            final File file = new File(mPath);
            if (file.exists())
                return true;
            else
                return false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void copyDataBase() throws IOException {
        try {
            InputStream mInputStream = context.getAssets().open(
                    DATABASE_NAME);
            String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();
            LogService.Log("copyDataBase", "db path: " + outFileName);
            OutputStream mOutputStream = new FileOutputStream(new File(
                    outFileName));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean openDataBase() throws SQLException {

        String mPath = context.getDatabasePath(DATABASE_NAME).getPath();
        database = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.OPEN_READWRITE);

        return database.isOpen();

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        openDataBase();
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    public ArrayList<Achievement> getAllAchivements() {

        ArrayList<Achievement> achievements = new ArrayList<Achievement>();

        if (database != null && database.isOpen()) {

            Cursor cursor = database.query(TABLE_ACHIEVEMENTS,
                    achivementsAllColumns, null, null, null, null, null);

            LogService.Log("DatabaseHelper", "achievements cursor: " + cursor.toString());

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Achievement achievement = cursorToAchievement(cursor);
                achievements.add(achievement);
                cursor.moveToNext();
            }
            cursor.close();

        }

        return achievements;

    }

    private Achievement cursorToAchievement(Cursor cursor) {

        Achievement achievement = new Achievement();
        achievement.setId(cursor.getInt(0));
        achievement.setName(cursor.getString(1));
        achievement.setUnlockId(cursor.getInt(2));
        achievement.setSubText(cursor.getString(3));
        achievement.setTime(cursor.getInt(4));
        achievement.setPeriodTime(cursor.getInt(5));
        achievement.setPeriodType(cursor.getString(6));
        achievement.setImageId(cursor.getString(7));

        return achievement;

    }

    public ArrayList<Exercise> getAllExercises() {

        ArrayList<Exercise> exercises = new ArrayList<Exercise>();

        if (database != null && database.isOpen()) {

            Cursor cursor = database.query(TABLE_EXERCISE,
                    exercisesAllColumns, null, null, null, null, null);

            LogService.Log("DatabaseHelper", "exercises cursor: " + cursor.toString());

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Exercise exercise = cursorToExercise(cursor);
                exercises.add(exercise);
                cursor.moveToNext();
            }
            cursor.close();

        }

        return exercises;

    }

    private Exercise cursorToExercise(Cursor cursor) {

        Exercise exercise = new Exercise();
        exercise.setId(cursor.getInt(0));
        exercise.setName(cursor.getString(1));
        exercise.setYoutubeLink(cursor.getString(2));
        exercise.setDescPre(cursor.getString(3));
        exercise.setDescExe(cursor.getString(4));
        exercise.setBigImageName(cursor.getString(5));
        exercise.setFlip1ImageName(cursor.getString(6));
        exercise.setFlip2ImageName(cursor.getString(7));
        exercise.setFlip3ImageName(cursor.getString(8));
        exercise.setSound(cursor.getString(9));

        return exercise;

    }

    public ArrayList<ExerciseForWorkout> getAllExercisesForWorkout() {

        ArrayList<ExerciseForWorkout> exercisesForWorkouts = new ArrayList<ExerciseForWorkout>();

        if (database != null && database.isOpen()) {

            Cursor cursor = database.query(TABLE_EXERCISE_FOR_WORK,
                    exercisesForWorkoutAllColumns, null, null, null, null, null);

            LogService.Log("DatabaseHelper", "exercisesForWorkouts cursor: " + cursor.toString());

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ExerciseForWorkout exerciseForWorkout = cursorToExerciseForWorkout(cursor);
                exercisesForWorkouts.add(exerciseForWorkout);
                cursor.moveToNext();
            }
            cursor.close();

        }

        return exercisesForWorkouts;

    }

    private ExerciseForWorkout cursorToExerciseForWorkout(Cursor cursor) {

        ExerciseForWorkout exerciseForWorkout = new ExerciseForWorkout();
        exerciseForWorkout.setWorkoutid(cursor.getInt(0));
        exerciseForWorkout.setExerciseId(cursor.getInt(1));

        return exerciseForWorkout;

    }


    public ArrayList<UnlockItem> getAllUnlockItems() {

        ArrayList<UnlockItem> unlockItems = new ArrayList<UnlockItem>();

        if (database != null && database.isOpen()) {

            Cursor cursor = database.query(TABLE_UNLOCK_ITEMS,
                    unlockItemAllColumns, null, null, null, null, null);

            LogService.Log("DatabaseHelper", "unlockItems cursor: " + cursor.toString());

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                UnlockItem unlockItem = cursorToUnlockItem(cursor);
                unlockItems.add(unlockItem);
                cursor.moveToNext();
            }
            cursor.close();

        }

        return unlockItems;

    }

    private UnlockItem cursorToUnlockItem(Cursor cursor) {

        UnlockItem unlockItem = new UnlockItem();
        unlockItem.setId(cursor.getInt(0));
        unlockItem.setName(cursor.getString(1));

        return unlockItem;

    }

    public ArrayList<Workout> getAllWorkouts() {

        ArrayList<Workout> workouts = new ArrayList<Workout>();

        if (database != null && database.isOpen()) {

            Cursor cursor = database.query(TABLE_WORKOUTS,
                    workoutAllColumns, null, null, null, null, null);

            LogService.Log("DatabaseHelper", "workouts cursor: " + cursor.toString());

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Workout workout = cursorToWorkout(cursor);
                workouts.add(workout);
                cursor.moveToNext();
            }
            cursor.close();

        }

        return workouts;

    }

    private Workout cursorToWorkout(Cursor cursor) {

        Workout workout = new Workout();
        workout.setId(cursor.getInt(0));
        workout.setName(cursor.getString(1));
        workout.setUnlockItem(cursor.getString(2));

        return workout;

    }

    public void addCompletedWorkout(int workoutId, int cycles, String date) {

        if (database != null && database.isOpen()) {

            ContentValues initialValues = new ContentValues();

            // convert date to string
            initialValues.put(COMPLETED_WORKOUT_WORKOUT_ID, workoutId);
            initialValues.put(COMPLETED_WORKOUT_CYCLES, cycles);
            initialValues.put(COMPLETED_WORKOUT_DATE, date);

            long completedId = database.insert(TABLE_COMLETED_WORKOUT, null, initialValues);

            LogService.Log("addCompletedWorkout", "completedId: " + completedId);

        }

    }

    public ArrayList<CompletedWorkout> getAllCompletedWorkouts() {

        ArrayList<CompletedWorkout> workoutCompletedWorkouts = new ArrayList<CompletedWorkout>();

        if (database != null && database.isOpen()) {

            Cursor cursor = database.query(TABLE_COMLETED_WORKOUT,
                    workoutCompletedAllColumns, null, null, null, null, null);

            LogService.Log("DatabaseHelper", "workoutCompletedWorkouts cursor: " + cursor.toString());

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CompletedWorkout completedWorkout = cursorToCompletedWorkout(cursor);
                workoutCompletedWorkouts.add(completedWorkout);
                cursor.moveToNext();
            }
            cursor.close();

        }

        return workoutCompletedWorkouts;

    }

    private CompletedWorkout cursorToCompletedWorkout(Cursor cursor) {

        CompletedWorkout completedWorkout = new CompletedWorkout();
        completedWorkout.setCompletedWorkoutId(cursor.getInt(0));
        completedWorkout.setWorkoutId(cursor.getInt(1));
        completedWorkout.setDate(cursor.getString(2));
        completedWorkout.setCycles(cursor.getInt(3));

        return completedWorkout;

    }

}
