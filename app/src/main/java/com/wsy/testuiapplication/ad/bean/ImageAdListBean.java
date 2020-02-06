package com.wsy.testuiapplication.ad.bean;

import java.util.List;

/**
 * Created by WSY on 2020/2/5.
 */
public class ImageAdListBean {

    private int displayTime;
    private List<String> images;

    public int getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
