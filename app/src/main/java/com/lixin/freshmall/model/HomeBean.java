package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/22
 * My mailbox is 1403241630@qq.com
 */

public class HomeBean {
    public String result;
    public String resultNote ;
    public List<classifyBottom> classifyBottom;
    public List<hotCommoditys> hotCommoditys;
    public List<newsCommoditys> newsCommoditys;
    public List<promoteCommoditys> promoteCommoditys;
    public List<rotateAdvertisement> rotateAdvertisement;
    public List<rotateTopCommoditys> rotateTopCommoditys;

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

    public List<HomeBean.classifyBottom> getClassifyBottom() {
        return classifyBottom;
    }

    public void setClassifyBottom(List<HomeBean.classifyBottom> classifyBottom) {
        this.classifyBottom = classifyBottom;
    }

    public List<HomeBean.hotCommoditys> getHotCommoditys() {
        return hotCommoditys;
    }

    public void setHotCommoditys(List<HomeBean.hotCommoditys> hotCommoditys) {
        this.hotCommoditys = hotCommoditys;
    }

    public List<HomeBean.newsCommoditys> getNewsCommoditys() {
        return newsCommoditys;
    }

    public void setNewsCommoditys(List<HomeBean.newsCommoditys> newsCommoditys) {
        this.newsCommoditys = newsCommoditys;
    }

    public List<HomeBean.promoteCommoditys> getPromoteCommoditys() {
        return promoteCommoditys;
    }

    public void setPromoteCommoditys(List<HomeBean.promoteCommoditys> promoteCommoditys) {
        this.promoteCommoditys = promoteCommoditys;
    }

    public List<HomeBean.rotateAdvertisement> getRotateAdvertisement() {
        return rotateAdvertisement;
    }

    public void setRotateAdvertisement(List<HomeBean.rotateAdvertisement> rotateAdvertisement) {
        this.rotateAdvertisement = rotateAdvertisement;
    }

    public List<HomeBean.rotateTopCommoditys> getRotateTopCommoditys() {
        return rotateTopCommoditys;
    }

    public void setRotateTopCommoditys(List<HomeBean.rotateTopCommoditys> rotateTopCommoditys) {
        this.rotateTopCommoditys = rotateTopCommoditys;
    }

    public class classifyBottom {
        public String classifyIcon;
        public String classifyType;
        public String meunid;

        public String getClassifyIcon() {
            return classifyIcon;
        }

        public void setClassifyIcon(String classifyIcon) {
            this.classifyIcon = classifyIcon;
        }

        public String getClassifyType() {
            return classifyType;
        }

        public void setClassifyType(String classifyType) {
            this.classifyType = classifyType;
        }

        public String getMeunid() {
            return meunid;
        }

        public void setMeunid(String meunid) {
            this.meunid = meunid;
        }
    }
    public class hotCommoditys{
        public String commodityIcon;
        public String commodityNewPrice;
        public String commodityOriginalPrice;
        public String commodityTitle;
        public String commodityid;
        public String commodityUnit;

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

        public String getCommodityOriginalPrice() {
            return commodityOriginalPrice;
        }

        public void setCommodityOriginalPrice(String commodityOriginalPrice) {
            this.commodityOriginalPrice = commodityOriginalPrice;
        }

        public String getCommodityTitle() {
            return commodityTitle;
        }

        public void setCommodityTitle(String commodityTitle) {
            this.commodityTitle = commodityTitle;
        }

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }
    }
    public class newsCommoditys{
        public String commodityIcon;
        public String commodityNewPrice;
        public String commodityOriginalPrice;
        public String commodityTitle;
        public String commodityid;
        public String commodityUnit;

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

        public String getCommodityOriginalPrice() {
            return commodityOriginalPrice;
        }

        public void setCommodityOriginalPrice(String commodityOriginalPrice) {
            this.commodityOriginalPrice = commodityOriginalPrice;
        }

        public String getCommodityTitle() {
            return commodityTitle;
        }

        public void setCommodityTitle(String commodityTitle) {
            this.commodityTitle = commodityTitle;
        }

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }
    }
    public class promoteCommoditys{
        public String commodityIcon;
        public String commodityNewPrice;
        public String commodityOriginalPrice;
        public String commodityTitle;
        public String commodityid;
        public String commodityUnit;

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

        public String getCommodityOriginalPrice() {
            return commodityOriginalPrice;
        }

        public void setCommodityOriginalPrice(String commodityOriginalPrice) {
            this.commodityOriginalPrice = commodityOriginalPrice;
        }

        public String getCommodityTitle() {
            return commodityTitle;
        }

        public void setCommodityTitle(String commodityTitle) {
            this.commodityTitle = commodityTitle;
        }

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }
    }
    public class rotateAdvertisement{
        public String advertisementIcon;
        public String advertisementid;

        public String getAdvertisementIcon() {
            return advertisementIcon;
        }

        public void setAdvertisementIcon(String advertisementIcon) {
            this.advertisementIcon = advertisementIcon;
        }

        public String getAdvertisementid() {
            return advertisementid;
        }

        public void setAdvertisementid(String advertisementid) {
            this.advertisementid = advertisementid;
        }
    }
    public class rotateTopCommoditys{
        public String rotateIcon;
        public int rotateType;
        public String rotateid;

        public String getRotateIcon() {
            return rotateIcon;
        }

        public void setRotateIcon(String rotateIcon) {
            this.rotateIcon = rotateIcon;
        }

        public int getRotateType() {
            return rotateType;
        }

        public void setRotateType(int rotateType) {
            this.rotateType = rotateType;
        }

        public String getRotateid() {
            return rotateid;
        }

        public void setRotateid(String rotateid) {
            this.rotateid = rotateid;
        }
    }
}


