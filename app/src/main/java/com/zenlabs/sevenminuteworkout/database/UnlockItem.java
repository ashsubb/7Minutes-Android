package com.zenlabs.sevenminuteworkout.database;

/**
 * Created by madarashunor on 12/10/15.
 */
public class UnlockItem {

    private int id;
    private String name;

    public UnlockItem() {
    }

    public UnlockItem(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "UnlockItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
