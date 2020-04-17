package com.mobilefirst.honestherd.HHModel;

public class HHDateModel {

    private String day="" , month= "" , year = "",coins = "0",sdate = "";

    private boolean isActive;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public HHDateModel() {
        this.day = day;
        this.month = month;
        this.year = year;
        this.isActive = isActive;
    }

  /*  public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }*/

}
