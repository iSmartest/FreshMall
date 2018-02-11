package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/7
 * My mailbox is 1403241630@qq.com
 */

public class RedPackagesBean {
    /**
     *
     返回格式
     {
     result:"0" //0成功 1失败
     resultNote:"失败原因"
     redPacketTotalMoney:""//收到红包总金额
     redPacketList:[{
     friendsName:""邀请的好友名字
     openRedPacketTime:""//获得红包的时间
     money:""//金额
     }]

     }
     */
    private String result;
    private String resultNote;
    private String redPacketTotalMoney;
    private List<RedPacketList> redPacketList;

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

    public String getRedPacketTotalMoney() {
        return redPacketTotalMoney;
    }

    public void setRedPacketTotalMoney(String redPacketTotalMoney) {
        this.redPacketTotalMoney = redPacketTotalMoney;
    }

    public List<RedPacketList> getRedPacketList() {
        return redPacketList;
    }

    public void setRedPacketList(List<RedPacketList> redPacketList) {
        this.redPacketList = redPacketList;
    }

    public static class RedPacketList{
        private String friendsName;
        private String openRedPacketTime;
        private String money;

        public String getFriendsName() {
            return friendsName;
        }

        public void setFriendsName(String friendsName) {
            this.friendsName = friendsName;
        }

        public String getOpenRedPacketTime() {
            return openRedPacketTime;
        }

        public void setOpenRedPacketTime(String openRedPacketTime) {
            this.openRedPacketTime = openRedPacketTime;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
