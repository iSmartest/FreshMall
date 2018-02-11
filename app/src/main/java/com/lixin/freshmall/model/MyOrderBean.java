package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyOrderBean {
    public String result;
    public String resultNote;
    public int totalPage;
    public List<Orders> orders;

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

    public List<MyOrderBean.Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<MyOrderBean.Orders> orders) {
        this.orders = orders;
    }

    public class Orders{
        public String orderId;
        public String oderTotalPrice;
        public String orderState;//1待付款,2待发货,3待收货,4已完成,5退款售后
        private String paytime;
        public List<orderCommodity> orderCommodity;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderState() {
            return orderState;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }

        public String getOderTotalPrice() {
            return oderTotalPrice;
        }

        public void setOderTotalPrice(String oderTotalPrice) {
            this.oderTotalPrice = oderTotalPrice;
        }

        public List<MyOrderBean.Orders.orderCommodity> getOrderCommodity() {
            return orderCommodity;
        }

        public void setOrderCommodity(List<MyOrderBean.Orders.orderCommodity> orderCommodity) {
            this.orderCommodity = orderCommodity;
        }

        public String getPaytime() {
            return paytime;
        }

        public void setPaytime(String paytime) {
            this.paytime = paytime;
        }

        public class orderCommodity{
            public String commodityid;
            public String commodityIcon;
            public String commodityNewPrice;
            public String commodityTitle;
            public String commodityBuyNum;
            public String commodityFirstParam;
            public String commoditySecondParam;
            public String commodityFreight;
            public String commodityUnit;

            public String getCommodityUnit() {
                return commodityUnit;
            }

            public void setCommodityUnit(String commodityUnit) {
                this.commodityUnit = commodityUnit;
            }

            public String getCommodityFreight() {
                return commodityFreight;
            }

            public void setCommodityFreight(String commodityFreight) {
                this.commodityFreight = commodityFreight;
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

            public String getCommodityBuyNum() {
                return commodityBuyNum;
            }

            public void setCommodityBuyNum(String commodityBuyNum) {
                this.commodityBuyNum = commodityBuyNum;
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
        }
    }
}
