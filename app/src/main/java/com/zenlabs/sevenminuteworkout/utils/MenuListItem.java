package com.zenlabs.sevenminuteworkout.utils;

/**
 * Created by madarashunor on 06/10/15.
 */
public class MenuListItem {

    private int id;
    private int icon;
    private String title;

    public MenuListItem() {
    }

    public MenuListItem(int id, int icon, String title) {
        this.id = id;
        this.icon = icon;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MenuListItem{" +
                "id=" + id +
                ", icon=" + icon +
                ", title='" + title + '\'' +
                '}';
    }
}
