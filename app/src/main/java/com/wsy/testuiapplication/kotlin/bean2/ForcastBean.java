package com.wsy.testuiapplication.kotlin.bean2;

public class ForcastBean {

    private String cityName;
    private double high;
    private double low;

    public ForcastBean(String cityName, double high, double low) {
        this.cityName = cityName;
        this.high = high;
        this.low = low;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    @Override
    public String toString() {
        return "ForcastBean{" +
                "cityName='" + cityName + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                '}';
    }
}
