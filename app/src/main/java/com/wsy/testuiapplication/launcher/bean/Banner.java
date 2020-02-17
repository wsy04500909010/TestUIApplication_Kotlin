package com.wsy.testuiapplication.launcher.bean;

/**
 * Created by qwl on 2017/8/29.
 */

public class Banner {
    // TODO: 2019/4/17 delete
    //----------------------------------------------old---------------------------------------------
    private String banner_data_id;
    private String picture;
    private String recommend;
    private int link_type;

    @Deprecated
    public String getBanner_data_id() {
        return banner_data_id;
    }

    @Deprecated
    public void setBanner_data_id(String banner_data_id) {
        this.banner_data_id = banner_data_id;
    }

    @Deprecated
    public String getPicture() {
        return picture;
    }

    @Deprecated
    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Deprecated
    public String getRecommend() {
        return recommend;
    }

    @Deprecated
    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    @Deprecated
    public int getLink_type() {
        return link_type;
    }

    @Deprecated
    public void setLink_type(int link_type) {
        this.link_type = link_type;
    }


    //-------------------------------------------new------------------------------------------------

    private int id;
    private String title;
    private String summaries;
    private String image;
//    private DFTarget target;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummaries() {
        return summaries;
    }

    public void setSummaries(String summaries) {
        this.summaries = summaries;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public DFTarget getTarget() {
//        return target;
//    }
//
//    public void setTarget(DFTarget target) {
//        this.target = target;
//    }

    @Override
    public String toString() {
        return "Banner{" + "id=" + id + ", title='" + title + '\'' + ", summaries='" + summaries + '\'' + ", image='" +
               image + '\'' +
//                ", target=" + target +
               '}';
    }
}
