package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class MyEvaluateBean {
    /**
     *  result:"0" //0成功1失败
     resultNote:"失败原因"
     totalPage:5//总页数
     orderCommodity:[{
     commentId:""//评论id
     commentState:""//0待评价,1已评价
     commodityid:"12"
     commodityIcon:"http://sdsdsa.png"
     commodityNewPrice:"799"
     commodityTitle:"混和机油"
     commentContent:""//评论内容(只有已评价才有此字段)
     commodityBuyNum:"2"//购买商品的数目
     commodityFirstParam:""//第一个参数
     commoditySecondParam:""//第二个参数
     }]
     */
    private String result;
    private String resultNote;
    private int totalPage;
    private List<OrderCommodity> orderCommodity;

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

    public List<OrderCommodity> getOrderCommodity() {
        return orderCommodity;
    }

    public void setOrderCommodity(List<OrderCommodity> orderCommodity) {
        this.orderCommodity = orderCommodity;
    }

    public static class OrderCommodity{
        private String commentId;
        private String commentState;
        private String commodityid;
        private String commodityIcon;
        private String commodityNewPrice;
        private String commodityTitle;
        private String commentContent;
        private String commodityBuyNum;
        private String commodityFirstParam;
        private String commoditySecondParam;
        private String commodityUnit;

        public String getCommodityUnit() {
            return commodityUnit;
        }

        public void setCommodityUnit(String commodityUnit) {
            this.commodityUnit = commodityUnit;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentState() {
            return commentState;
        }

        public void setCommentState(String commentState) {
            this.commentState = commentState;
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

        public String getCommentContent() {
            return commentContent;
        }

        public void setCommentContent(String commentContent) {
            this.commentContent = commentContent;
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
