package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/27
 * My mailbox is 1403241630@qq.com
 */

public class CouponBean {
    public String result;
    public String resultNote;
    public List<securitiesList> securitiesList;

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

    public List<CouponBean.securitiesList> getSecuritiesList() {
        return securitiesList;
    }

    public void setSecuritiesList(List<CouponBean.securitiesList> securitiesList) {
        this.securitiesList = securitiesList;
    }

    public class securitiesList{
        public String securitiesid;//红包id
        public String securitiesAllPrice;//多少才免减
        public String securitiesReducePrice; //免减多少
        public String securitiesTimeZone;//红包截止时间,只有未使用券才有该字段

        public String getSecuritiesid() {
            return securitiesid;
        }

        public void setSecuritiesid(String securitiesid) {
            this.securitiesid = securitiesid;
        }

        public String getSecuritiesAllPrice() {
            return securitiesAllPrice;
        }

        public void setSecuritiesAllPrice(String securitiesAllPrice) {
            this.securitiesAllPrice = securitiesAllPrice;
        }

        public String getSecuritiesReducePrice() {
            return securitiesReducePrice;
        }

        public void setSecuritiesReducePrice(String securitiesReducePrice) {
            this.securitiesReducePrice = securitiesReducePrice;
        }

        public String getSecuritiesTimeZone() {
            return securitiesTimeZone;
        }

        public void setSecuritiesTimeZone(String securitiesTimeZone) {
            this.securitiesTimeZone = securitiesTimeZone;
        }
    }
}
