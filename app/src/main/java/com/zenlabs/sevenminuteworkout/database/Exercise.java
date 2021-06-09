package com.zenlabs.sevenminuteworkout.database;

/**
 * Created by madarashunor on 12/10/15.
 */
public class Exercise {

    private int id;
    private String name;
    private String youtubeLink;
    private String descPre;
    private String descExe;
    private String bigImageName;
    private String flip1ImageName;
    private String flip2ImageName;
    private String flip3ImageName;
    private String sound;

    public Exercise() {
    }

    public Exercise(int id, String name, String youtubeLink, String descPre, String descExe) {
        this.id = id;
        this.name = name;
        this.youtubeLink = youtubeLink;
        this.descPre = descPre;
        this.descExe = descExe;
    }

    public Exercise(int id, String name, String youtubeLink, String descPre, String descExe, String bigImageName, String flip1ImageName, String flip2ImageName, String flip3ImageName, String sound) {
        this.id = id;
        this.name = name;
        this.youtubeLink = youtubeLink;
        this.descPre = descPre;
        this.descExe = descExe;
        this.bigImageName = bigImageName;
        this.flip1ImageName = flip1ImageName;
        this.flip2ImageName = flip2ImageName;
        this.flip3ImageName = flip3ImageName;
        this.sound = sound;
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

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getDescPre() {
        return descPre;
    }

    public void setDescPre(String descPre) {
        this.descPre = descPre;
    }

    public String getDescExe() {
        return descExe;
    }

    public void setDescExe(String descExe) {
        this.descExe = descExe;
    }

    public String getBigImageName() {
        return bigImageName;
    }

    public void setBigImageName(String bigImageName) {
        this.bigImageName = bigImageName;
    }

    public String getFlip1ImageName() {
        return flip1ImageName;
    }

    public void setFlip1ImageName(String flip1ImageName) {
        this.flip1ImageName = flip1ImageName;
    }

    public String getFlip2ImageName() {
        return flip2ImageName;
    }

    public void setFlip2ImageName(String flip2ImageName) {
        this.flip2ImageName = flip2ImageName;
    }

    public String getFlip3ImageName() {
        return flip3ImageName;
    }

    public void setFlip3ImageName(String flip3ImageName) {
        this.flip3ImageName = flip3ImageName;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", youtubeLink='" + youtubeLink + '\'' +
                ", descPre='" + descPre + '\'' +
                ", descExe='" + descExe + '\'' +
                ", bigImageName='" + bigImageName + '\'' +
                ", flip1ImageName='" + flip1ImageName + '\'' +
                ", flip2ImageName='" + flip2ImageName + '\'' +
                ", flip3ImageName='" + flip3ImageName + '\'' +
                ", sound='" + sound + '\'' +
                '}';
    }

}
