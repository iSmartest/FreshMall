package com.lixin.freshmall.model;

/**
 * Created by 小火
 * Create time on  2017/5/25
 * My mailbox is 1403241630@qq.com
 */

public class SkuBean {
//    :"0" //0成功1失败
//    :"失败原因"
//    ::"混和机油" //商品名 生成订单用
//    :"799"//商品现价
//    :"544" //库存
//    :"http://sdsdsa.png"
    public String result;
    public String resultNote;
    public String commodityTitle;
    public String commodityNewPrice;
    public String commodityInventory;
    public String commodityIcon;
    public String commodityUnit;

    public String getCommodityUnit() {
        return commodityUnit;
    }

    public void setCommodityUnit(String commodityUnit) {
        this.commodityUnit = commodityUnit;
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

    public String getCommodityNewPrice() {
        return commodityNewPrice;
    }

    public void setCommodityNewPrice(String commodityNewPrice) {
        this.commodityNewPrice = commodityNewPrice;
    }

    public String getCommodityTitle() {
        return commodityTitle;
    }

    public void setCommodityTitle(String commodityTitle) {
        this.commodityTitle = commodityTitle;
    }

    public String getCommodityInventory() {
        return commodityInventory;
    }

    public void setCommodityInventory(String commodityInventory) {
        this.commodityInventory = commodityInventory;
    }

    public String getCommodityIcon() {
        return commodityIcon;
    }

    public void setCommodityIcon(String commodityIcon) {
        this.commodityIcon = commodityIcon;
    }
}
