package com.example.yancey.dailyapp;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by yancey on 2017/8/24.
 */

public class CostBean extends DataSupport implements Serializable {
    public String costTitle;
    public String costDate;
    public String costMoney;
    public int year;
    public int month;
    public int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getCostTitle() {
        return costTitle;
    }

    public void setCostTitle(String costTitle) {
        this.costTitle = costTitle;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public String getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney;
    }
}
