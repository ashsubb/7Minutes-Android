package com.zenlabs.sevenminuteworkout.database;

/**
 * Created by madarashunor on 12/10/15.
 */
public class Workout {

    private int id;
    private String name;
    private String unlockItem;

    public Workout() {
    }

    public Workout(int id, String name, String unlockItem) {
        this.id = id;
        this.name = name;
        this.unlockItem = unlockItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnlockItem() {
        return unlockItem;
    }

    public void setUnlockItem(String unlockItem) {
        this.unlockItem = unlockItem;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unlockItem='" + unlockItem + '\'' +
                '}';
    }

}
