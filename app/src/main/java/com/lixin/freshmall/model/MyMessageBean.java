package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/6/9
 * My mailbox is 1403241630@qq.com
 */

public class MyMessageBean {
    public String result;
    public String resultNote;
    public List<messageList> messageList;

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

    public List<MyMessageBean.messageList> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MyMessageBean.messageList> messageList) {
        this.messageList = messageList;
    }

    public class messageList{
        public String commodityid;
        public String messageContent;
        public String messageTitle;
        public String messageType;
        public String messgaeTime;

        public String getCommodityid() {
            return commodityid;
        }

        public void setCommodityid(String commodityid) {
            this.commodityid = commodityid;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public String getMessageTitle() {
            return messageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getMessgaeTime() {
            return messgaeTime;
        }

        public void setMessgaeTime(String messgaeTime) {
            this.messgaeTime = messgaeTime;
        }
    }
}
