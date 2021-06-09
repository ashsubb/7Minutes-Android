package com.zenlabs.sevenminuteworkout.database;

/**
 * Created by madarashunor on 12/10/15.
 */
public class Achievement {

    private int id;
    private String name;
    private int unlockId;
    private String subText;
    private int time;
    private int periodTime;
    private String periodType;
    private String imageId;

    public Achievement() {
    }

    public Achievement(int id, String name, int unlockId, String subText, int time, int periodTime, String periodType, String imageId) {
        this.id = id;
        this.name = name;
        this.unlockId = unlockId;
        this.subText = subText;
        this.time = time;
        this.periodTime = periodTime;
        this.periodType = periodType;
        this.imageId = imageId;
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

    public int getUnlockId() {
        return unlockId;
    }

    public void setUnlockId(int unlockId) {
        this.unlockId = unlockId;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(int periodTime) {
        this.periodTime = periodTime;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unlockId=" + unlockId +
                ", subText='" + subText + '\'' +
                ", time=" + time +
                ", periodTime=" + periodTime +
                ", periodType='" + periodType + '\'' +
                ", imageId='" + imageId + '\'' +
                '}';
    }
}
