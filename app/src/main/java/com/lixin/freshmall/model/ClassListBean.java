package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/24
 * My mailbox is 1403241630@qq.com
 */

public class ClassListBean {
    public String result;
    public String resultNote;
    public int totalPage;
    public List<commoditys> commoditys;

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

    public List<ClassListBean.commoditys> getCommoditys() {
        return commoditys;
    }

    public void setCommoditys(List<ClassListBean.commoditys> commoditys) {
        this.commoditys = commoditys;
    }

    public class commoditys{
        public String commodityid;
        public String commodityTitle;
        public String commodityIcon;
        public String commodityOriginalPrice;
        public String commodityNewPrice;
        public String commoditySeller;
        public String commodityUnit;

        public String getCommodityUnit() {
            return commodityUnit;
        }

        public void setCommodityUnit(String commodityUnit) {
            this.commodityUnit = commodityUnit;
        }

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }

        public String getCommodityTitle() {
            return commodityTitle;
        }

        public void setCommodityTitle(String commodityTitle) {
            this.commodityTitle = commodityTitle;
        }

        public String getCommodityIcon() {
            return commodityIcon;
        }

        public void setCommodityIcon(String commodityIcon) {
            this.commodityIcon = commodityIcon;
        }

        public String getCommodityOriginalPrice() {
            return commodityOriginalPrice;
        }

        public void setCommodityOriginalPrice(String commodityOriginalPrice) {
            this.commodityOriginalPrice = commodityOriginalPrice;
        }

        public String getCommodityNewPrice() {
            return commodityNewPrice;
        }

        public void setCommodityNewPrice(String commodityNewPrice) {
            this.commodityNewPrice = commodityNewPrice;
        }

        public String getCommoditySeller() {
            return commoditySeller;
        }

        public void setCommoditySeller(String commoditySeller) {
            this.commoditySeller = commoditySeller;
        }
    }
}

