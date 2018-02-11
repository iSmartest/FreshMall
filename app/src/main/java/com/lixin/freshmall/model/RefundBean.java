package com.lixin.freshmall.model;

/**
 * Created by 小火
 * Create time on  2017/6/6
 * My mailbox is 1403241630@qq.com
 */

public class RefundBean {
    public String result;
    public String resultNote;
    public String refundMoney;//退款金额
    public String refundSucessTime;//后台处理退款时间（通过或拒绝）
    public String reviewTime;//客户发起退款时间
    public String refundNewState;//最新退款状态，0代表等待平台审核，1代表平台审核通过已完成退款，2代表平台未审核通过不能退款
    public String refundReason;//平台未审核通过给出解释
    public String userIcon;//用户头像
    public String userNickName;//用户昵称
    public String refundTitle;//退款理由
    public String refundDetail;//退款描述

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(String refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getRefundSucessTime() {
        return refundSucessTime;
    }

    public void setRefundSucessTime(String refundSucessTime) {
        this.refundSucessTime = refundSucessTime;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getRefundNewState() {
        return refundNewState;
    }

    public void setRefundNewState(String refundNewState) {
        this.refundNewState = refundNewState;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getRefundTitle() {
        return refundTitle;
    }

    public void setRefundTitle(String refundTitle) {
        this.refundTitle = refundTitle;
    }

    public String getRefundDetail() {
        return refundDetail;
    }

    public void setRefundDetail(String refundDetail) {
        this.refundDetail = refundDetail;
    }
}
