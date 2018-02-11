package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/23
 * My mailbox is 1403241630@qq.com
 */

public class ShopDecBean {
    public String commodityDescription;
    public String commodityFreight;
    public int commodityIscollotion;
    public String commodityNewPrice;
    public String commodityOriginalPrice;
    public String commodityWebLink;
    public String commoditysellerNum;
    public String commodityTitle;
    public String commodityUnit;
    public String commodityIcon;
    public String commodityMaxLimitationNum;
    public String commodityMaxBuyNum;
    public String result;
    public String resultNote;
    public List<String> rotateCommodityPics;
    public List<commoditySelectParameters> commoditySelectParameters;
    public List<CommoditySpec> commoditySpec;

    public String getCommodityMaxLimitationNum() {
        return commodityMaxLimitationNum;
    }

    public void setCommodityMaxLimitationNum(String commodityMaxLimitationNum) {
        this.commodityMaxLimitationNum = commodityMaxLimitationNum;
    }

    public String getCommodityMaxBuyNum() {
        return commodityMaxBuyNum;
    }

    public void setCommodityMaxBuyNum(String commodityMaxBuyNum) {
        this.commodityMaxBuyNum = commodityMaxBuyNum;
    }

    public String getCommodityTitle() {
        return commodityTitle;
    }

    public String getCommodityUnit() {
        return commodityUnit;
    }

    public void setCommodityUnit(String commodityUnit) {
        this.commodityUnit = commodityUnit;
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

    public String getCommodityDescription() {
        return commodityDescription;
    }

    public void setCommodityDescription(String commodityDescription) {
        this.commodityDescription = commodityDescription;
    }

    public String getCommodityFreight() {
        return commodityFreight;
    }

    public void setCommodityFreight(String commodityFreight) {
        this.commodityFreight = commodityFreight;
    }

    public int getCommodityIscollotion() {
        return commodityIscollotion;
    }

    public void setCommodityIscollotion(int commodityIscollotion) {
        this.commodityIscollotion = commodityIscollotion;
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

    public String getCommodityWebLink() {
        return commodityWebLink;
    }

    public void setCommodityWebLink(String commodityWebLink) {
        this.commodityWebLink = commodityWebLink;
    }

    public String getCommoditysellerNum() {
        return commoditysellerNum;
    }

    public void setCommoditysellerNum(String commoditysellerNum) {
        this.commoditysellerNum = commoditysellerNum;
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

    public List<String> getRotateCommodityPics() {
        return rotateCommodityPics;
    }

    public void setRotateCommodityPics(List<String> rotateCommodityPics) {
        this.rotateCommodityPics = rotateCommodityPics;
    }

    public List<ShopDecBean.commoditySelectParameters> getCommoditySelectParameters() {
        return commoditySelectParameters;
    }

    public void setCommoditySelectParameters(List<ShopDecBean.commoditySelectParameters> commoditySelectParameters) {
        this.commoditySelectParameters = commoditySelectParameters;
    }

    public List<CommoditySpec> getCommoditySpec() {
        return commoditySpec;
    }

    public void setCommoditySpec(List<CommoditySpec> commoditySpec) {
        this.commoditySpec = commoditySpec;
    }

    public class commoditySelectParameters{
        public String parameterTitle;
        public List<String> parameters;

        public String getParameterTitle() {
            return parameterTitle;
        }

        public void setParameterTitle(String parameterTitle) {
            this.parameterTitle = parameterTitle;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public void setParameters(List<String> parameters) {
            this.parameters = parameters;
        }
    }
    public class CommoditySpec{
        private String specTitle;
        private String specParameter;

        public String getSpecTitle() {
            return specTitle;
        }

        public void setSpecTitle(String specTitle) {
            this.specTitle = specTitle;
        }

        public String getSpecParameter() {
            return specParameter;
        }

        public void setSpecParameter(String specParameter) {
            this.specParameter = specParameter;
        }
    }
}
