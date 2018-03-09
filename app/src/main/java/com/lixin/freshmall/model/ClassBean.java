package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class ClassBean {
    /**
     * {
     result:"0" //0成功1失败
     resultNote:"失败原因"
     totalPage:5//总页数
     commoditys:[{//
     commodityid:"12"//用于跳转到商品详情得到具体商品
     commodityTitle::"进口天然海贝" //商品名
     commodityDescription:"物美价廉"//商品描述
     commodityIcon:"http://sdsdsa.png"
     commodityOriginalPrice:"999" //商品的原价
     commodityNewPrice:"799" //商品售价
     commoditySeller:""//商品销量
     commodityLimitNum:""//商品限购数

     }]
     }
     */

    private String result;
    private String resultNote;
    private int totalPage;
    private List<Commoditys> commoditys;

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

    public List<Commoditys> getCommoditys() {
        return commoditys;
    }

    public void setCommoditys(List<Commoditys> commoditys) {
        this.commoditys = commoditys;
    }

    public static class Commoditys{
        private String commodityid;
        private String commodityTitle;
        private String commodityDescription;
        private String commodityIcon;
        private String commodityOriginalPrice;
        private String commodityNewPrice;
        private String commoditySeller;
        private String commodityLimitNum;
        private String commodityUnit;
        private String isSoldOut;

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

        public String getCommodityDescription() {
            return commodityDescription;
        }

        public void setCommodityDescription(String commodityDescription) {
            this.commodityDescription = commodityDescription;
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

        public String getCommodityLimitNum() {
            return commodityLimitNum;
        }

        public void setCommodityLimitNum(String commodityLimitNum) {
            this.commodityLimitNum = commodityLimitNum;
        }

        public String getIsSoldOut() {
            return isSoldOut;
        }

        public void setIsSoldOut(String isSoldOut) {
            this.isSoldOut = isSoldOut;
        }
    }
}
