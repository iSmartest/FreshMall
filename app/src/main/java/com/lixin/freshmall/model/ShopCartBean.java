package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/11/30
 * My mailbox is 1403241630@qq.com
 */

public class ShopCartBean {
    /**
     *  result:"0" //0成功1失败
     resultNote:"失败原因"
     totalPage:5//总页数
     shop:[{
     commodityid:"12"//用于跳转到商品详情得到具体商品
     commodityTitle::"进口天然海贝" //商品名
     commodityIcon:"http://sdsdsa.png"
     commodityFirstParam:""//第一个参数
     commoditySecondParam:""//第二个参数
     commodityNewPrice:"799" //商品售价
     commodityShooCarNum:""//商品个数
     }]
     */
    public String result;
    public String resultNote;
    public int totalPage;
    public List<Shop> shop;

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

    public List<ShopCartBean.Shop> getShop() {
        return shop;
    }

    public void setShop(List<ShopCartBean.Shop> shop) {
        this.shop = shop;
    }

    public class Shop{
        public String commodityid;
        public String commodityTitle;
        public String commodityIcon;
        public String commodityFirstParam;
        public String commoditySecondParam;
        public String commodityNewPrice;
        public String commodityShooCarNum;
        public String commodityMaxBuyNum;
        public String commodityFreight;
        public String commodityUnit;
        public boolean isChoosed;
        public boolean isCheck = false;

        public String getCommodityUnit() {
            return commodityUnit;
        }

        public void setCommodityUnit(String commodityUnit) {
            this.commodityUnit = commodityUnit;
        }

        public String getCommodityMaxBuyNum() {
            return commodityMaxBuyNum;
        }

        public void setCommodityMaxBuyNum(String commodityMaxBuyNum) {
            this.commodityMaxBuyNum = commodityMaxBuyNum;
        }

        public String getCommodityFreight() {
            return commodityFreight;
        }

        public void setCommodityFreight(String commodityFreight) {
            this.commodityFreight = commodityFreight;
        }

        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
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

        public String getCommodityNewPrice() {
            return commodityNewPrice;
        }

        public void setCommodityNewPrice(String commodityNewPrice) {
            this.commodityNewPrice = commodityNewPrice;
        }

        public String getCommodityShooCarNum() {
            return commodityShooCarNum;
        }

        public void setCommodityShooCarNum(String commodityShooCarNum) {
            this.commodityShooCarNum = commodityShooCarNum;
        }
    }
}
