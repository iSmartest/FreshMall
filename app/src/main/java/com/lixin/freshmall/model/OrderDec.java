package com.lixin.freshmall.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/13
 * My mailbox is 1403241630@qq.com
 */

public class OrderDec {
    /**
     * {
     result:"0" //0成功 1失败
     resultNote:"失败原因"
     getGoodsStore:""//取货门店
     getGoodsAddress:""//取货地址
     getGoodsStorePhone:""//取货门店电话
     placeOrderTime:""//下单时间
     wantGetGoodsTime:""//预约取货时间
     actualGetGoodsTime:""//实际取货时间
     shoppingBag:""//购物袋， 无 ，小， 大
     coupon:""//优惠金额
     PaymentMethod:""//支付方式
     giveChange:""//找零
     amountPaid:""//实付金额
     stype:""//0取货，1送货
     sendMoney:""//配送费
     address:""//收货地址
     userName:""//收货人
     phone:""//收货人电话
     commoditys:[{//要购买的商品信息
     commodityid:"12"
     commodityIcon:"http://sdsdsa.png"
     commodityNewPrice:"799"
     commodityTitle:""
     commodityFirstParameter:""
     commoditySecondParameter:""
     commodityBuyNum:"3"
     }]
     orderDetailed:[{//订单明细

     }]
     }
     */
    private String result;
    private String resultNote;
    private String getGoodsStore;
    private String getGoodsAddress;
    private String getGoodsStorePhone;
    private String placeOrderTime;
    private String wantGetGoodsTime;
    private String actualGetGoodsTime;
    private String shoppingBag;
    private String coupon;
    private String paymentMethod;
    private String giveChange;
    private String amountPaid;
    private String stype;
    private String sendMoney;
    private String address;
    private String userName;
    private String phone;
    private String oderPayPrice;
    private String oderTotalPrice;
    private List<Commoditys> commoditys;
    private List<OrderDetailed> orderDetailed;

    public String getOderTotalPrice() {
        return oderTotalPrice;
    }

    public void setOderTotalPrice(String oderTotalPrice) {
        this.oderTotalPrice = oderTotalPrice;
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

    public String getGetGoodsStore() {
        return getGoodsStore;
    }

    public void setGetGoodsStore(String getGoodsStore) {
        this.getGoodsStore = getGoodsStore;
    }

    public String getGetGoodsAddress() {
        return getGoodsAddress;
    }

    public void setGetGoodsAddress(String getGoodsAddress) {
        this.getGoodsAddress = getGoodsAddress;
    }

    public String getGetGoodsStorePhone() {
        return getGoodsStorePhone;
    }

    public void setGetGoodsStorePhone(String getGoodsStorePhone) {
        this.getGoodsStorePhone = getGoodsStorePhone;
    }

    public String getPlaceOrderTime() {
        return placeOrderTime;
    }

    public void setPlaceOrderTime(String placeOrderTime) {
        this.placeOrderTime = placeOrderTime;
    }

    public String getWantGetGoodsTime() {
        return wantGetGoodsTime;
    }

    public void setWantGetGoodsTime(String wantGetGoodsTime) {
        this.wantGetGoodsTime = wantGetGoodsTime;
    }

    public String getActualGetGoodsTime() {
        return actualGetGoodsTime;
    }

    public void setActualGetGoodsTime(String actualGetGoodsTime) {
        this.actualGetGoodsTime = actualGetGoodsTime;
    }

    public String getShoppingBag() {
        return shoppingBag;
    }

    public void setShoppingBag(String shoppingBag) {
        this.shoppingBag = shoppingBag;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getGiveChange() {
        return giveChange;
    }

    public void setGiveChange(String giveChange) {
        this.giveChange = giveChange;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(String sendMoney) {
        this.sendMoney = sendMoney;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOderPayPrice() {
        return oderPayPrice;
    }

    public void setOderPayPrice(String oderPayPrice) {
        this.oderPayPrice = oderPayPrice;
    }

    public List<Commoditys> getCommoditys() {
        return commoditys;
    }

    public void setCommoditys(List<Commoditys> commoditys) {
        this.commoditys = commoditys;
    }

    public List<OrderDetailed> getOrderDetailed() {
        return orderDetailed;
    }

    public void setOrderDetailed(List<OrderDetailed> orderDetailed) {
        this.orderDetailed = orderDetailed;
    }

    public class Commoditys{

        private String commodityid;
        private String commodityIcon;
        private String commodityNewPrice;
        private String commodityTitle;
        private String commodityFirstParameter;
        private String commoditySecondParameter;
        private String commodityBuyNum;
        private String commodityUnit;

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

        public String getCommodityTitle() {
            return commodityTitle;
        }

        public void setCommodityTitle(String commodityTitle) {
            this.commodityTitle = commodityTitle;
        }

        public String getCommodityFirstParameter() {
            return commodityFirstParameter;
        }

        public void setCommodityFirstParameter(String commodityFirstParameter) {
            this.commodityFirstParameter = commodityFirstParameter;
        }

        public String getCommoditySecondParameter() {
            return commoditySecondParameter;
        }

        public void setCommoditySecondParameter(String commoditySecondParameter) {
            this.commoditySecondParameter = commoditySecondParameter;
        }

        public String getCommodityBuyNum() {
            return commodityBuyNum;
        }

        public void setCommodityBuyNum(String commodityBuyNum) {
            this.commodityBuyNum = commodityBuyNum;
        }
    }
    public static class OrderDetailed implements Serializable{
       private String commodityTitle;
       private String commodityIcon;
       private String commodityFirstParameter;
       private String commoditySecondParameter;
       private String buyWeight;
       private String buyPrice;
       private String actualWeight;
       private String actualPrice;
       private String commodityBuyNum;
       private String ispiece;
       private String actualBuyNum;

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

        public String getCommodityFirstParameter() {
            return commodityFirstParameter;
        }

        public void setCommodityFirstParameter(String commodityFirstParameter) {
            this.commodityFirstParameter = commodityFirstParameter;
        }

        public String getCommoditySecondParameter() {
            return commoditySecondParameter;
        }

        public void setCommoditySecondParameter(String commoditySecondParameter) {
            this.commoditySecondParameter = commoditySecondParameter;
        }

        public String getBuyWeight() {
            return buyWeight;
        }

        public void setBuyWeight(String buyWeight) {
            this.buyWeight = buyWeight;
        }

        public String getBuyPrice() {
            return buyPrice;
        }

        public void setBuyPrice(String buyPrice) {
            this.buyPrice = buyPrice;
        }

        public String getActualWeight() {
            return actualWeight;
        }

        public void setActualWeight(String actualWeight) {
            this.actualWeight = actualWeight;
        }

        public String getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(String actualPrice) {
            this.actualPrice = actualPrice;
        }

        public String getCommodityBuyNum() {
            return commodityBuyNum;
        }

        public void setCommodityBuyNum(String commodityBuyNum) {
            this.commodityBuyNum = commodityBuyNum;
        }

        public String getIspiece() {
            return ispiece;
        }

        public void setIspiece(String ispiece) {
            this.ispiece = ispiece;
        }

        public String getActualBuyNum() {
            return actualBuyNum;
        }

        public void setActualBuyNum(String actualBuyNum) {
            this.actualBuyNum = actualBuyNum;
        }
    }
}
