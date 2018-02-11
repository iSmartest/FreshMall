package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2018/1/10
 * My mailbox is 1403241630@qq.com
 */

public class StoreTimeBean {
    private String result;
    private String resultNote;
    private int storeEndTime;
    private String issend; //是否送货 0 送货 1不送
    private double sendMoney;//配送费
    private double sendAllMoney; //满多少免配送费
    private int distance;
    private double longitude;
    private double latitude;
    private List<TimeList> timeList;
    private List<SendTimeList> sendTimeList;
    public static class TimeList {
        private String stime;

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }
    }

    public static class SendTimeList{
        private String stime;

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }

    public int getStoreEndTime() {
        return storeEndTime;
    }

    public void setStoreEndTime(int storeEndTime) {
        this.storeEndTime = storeEndTime;
    }

    public List<TimeList> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<TimeList> timeList) {
        this.timeList = timeList;
    }

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    public double getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(double sendMoney) {
        this.sendMoney = sendMoney;
    }

    public double getSendAllMoney() {
        return sendAllMoney;
    }

    public void setSendAllMoney(double sendAllMoney) {
        this.sendAllMoney = sendAllMoney;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<SendTimeList> getSendTimeList() {
        return sendTimeList;
    }

    public void setSendTimeList(List<SendTimeList> sendTimeList) {
        this.sendTimeList = sendTimeList;
    }
}
