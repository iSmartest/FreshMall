package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2018/4/9
 * My mailbox is 1403241630@qq.com
 */

public class RechargeBean {
    private String result;
    private String resultNote;
    private List<ChargeList> chargeList;

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

    public List<ChargeList> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<ChargeList> chargeList) {
        this.chargeList = chargeList;
    }

    public class ChargeList{
        private String chargeId;
        private String money;
        private String sendmoney;

        public String getChargeId() {
            return chargeId;
        }

        public void setChargeId(String chargeId) {
            this.chargeId = chargeId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSendmoney() {
            return sendmoney;
        }

        public void setSendmoney(String sendmoney) {
            this.sendmoney = sendmoney;
        }
    }

}
