package com.lixin.freshmall.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/26
 * My mailbox is 1403241630@qq.com
 */

public class GenerateOrderBean {
    public String cmd;
    public String uid;
    public String townId;
    public List<commoditys> commoditys;
    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public List<GenerateOrderBean.commoditys> getCommoditys() {
        return commoditys;
    }
    public void setCommoditys(List<GenerateOrderBean.commoditys> commoditys) {
        this.commoditys = commoditys;
    }
    public GenerateOrderBean(String cmd, String uid,String townId ,List<GenerateOrderBean.commoditys> list) {
        this.cmd = cmd;
        this.uid = uid;
        this.townId = townId;
        this.commoditys = list;
    }
    public static class commoditys implements Serializable{
        public String commodityid;
        public String commodityTitle;
        public String commodityIcon;
        public String commodityFirstParam;
        public String commoditySecondParam;
        public String commodityNewPrice;
        public String buyNum;
        public String commodityUnit;

        public commoditys(String commodityid, String commodityTitle, String commodityIcon, String commodityFirstParam, String commoditySecondParam, String commodityNewPrice, String commodityShooCarNum,String commodityUnit) {
            this.commodityid = commodityid;
            this.commodityTitle = commodityTitle;
            this.commodityIcon = commodityIcon;
            this.commodityFirstParam = commodityFirstParam;
            this.commoditySecondParam = commoditySecondParam;
            this.commodityNewPrice = commodityNewPrice;
            this.buyNum = commodityShooCarNum;
            this.commodityUnit = commodityUnit;
        }
        public String getCommodityTitle() {
            return commodityTitle;
        }

        public void setCommodityTitle(String commodityTitle) {
            this.commodityTitle = commodityTitle;
        }

        public String getCommodityUnit() {
            return commodityUnit;
        }

        public void setCommodityUnit(String commodityUnit) {
            this.commodityUnit = commodityUnit;
        }

        public String getCommodityIcon() {
            return commodityIcon;
        }

        public void setCommodityIcon(String commodityIcon) {
            this.commodityIcon = commodityIcon;
        }

        public String getCommodityNewPrice() {
            return commodityNewPrice;
        }

        public void setCommodityNewPrice(String commodityNewPrice) {
            this.commodityNewPrice = commodityNewPrice;
        }

        public String getCommodityShooCarNum() {
            return buyNum;
        }

        public void setCommodityShooCarNum(String commodityShooCarNum) {
            this.buyNum = commodityShooCarNum;
        }

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }


        public String getCommodityFirstParam() {
            return commodityFirstParam;
        }

        public void setCommodityFirstParam(String commodityFirstParam) {
            this.commodityFirstParam = commodityFirstParam;
        }

        public String getCommoditySecondParam() {
            return commoditySecondParam;
        }

        public void setCommoditySecondParam(String commoditySecondParam) {
            this.commoditySecondParam = commoditySecondParam;
        }

        public String getBuyNum() {
            return buyNum;
        }

        public void setBuyNum(String buyNum) {
            this.buyNum = buyNum;
        }
    }
}
