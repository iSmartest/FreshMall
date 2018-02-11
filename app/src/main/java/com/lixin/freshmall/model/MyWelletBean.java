package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/27
 * My mailbox is 1403241630@qq.com
 */

public class MyWelletBean {

    public String result;
    public String resultNote;
    public int totalPage;
    public String totalMoney;//钱包总钱数
    public String sendAtmTime;
    public String sendPtmTime;
    public List<moneyList> moneyList;

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

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getSendAtmTime() {
        return sendAtmTime;
    }

    public void setSendAtmTime(String sendAtmTime) {
        this.sendAtmTime = sendAtmTime;
    }

    public String getSendPtmTime() {
        return sendPtmTime;
    }

    public void setSendPtmTime(String sendPtmTime) {
        this.sendPtmTime = sendPtmTime;
    }

    public List<MyWelletBean.moneyList> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(List<MyWelletBean.moneyList> moneyList) {
        this.moneyList = moneyList;
    }

    public class moneyList{
        public String moneySoure;//钱的来源：0补差、1退款、2消费
        public String moneyNum;//钱的金额
        public String moneyTime;//返零钱的时间
        public String moneyType;//0代表系统返给用户的，1代表用户使用的零钱

        public String getMoneySoure() {
            return moneySoure;
        }

        public void setMoneySoure(String moneySoure) {
            this.moneySoure = moneySoure;
        }

        public String getMoneyNum() {
            return moneyNum;
        }

        public void setMoneyNum(String moneyNum) {
            this.moneyNum = moneyNum;
        }

        public String getMoneyTime() {
            return moneyTime;
        }

        public void setMoneyTime(String moneyTime) {
            this.moneyTime = moneyTime;
        }

        public String getMoneyType() {
            return moneyType;
        }

        public void setMoneyType(String moneyType) {
            this.moneyType = moneyType;
        }
    }
}
