package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/5
 * My mailbox is 1403241630@qq.com
 */

public class CodeBean {
    private String result;
    private String resultNote;
    private List<CodeList> codeList;

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

    public List<CodeList> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<CodeList> codeList) {
        this.codeList = codeList;
    }

    public static class CodeList{
        private String orderNum;
        private String qrCode;
        private String getGoodsTime;
        private String userIcon;
        private String userName;

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getQRCode() {
            return qrCode;
        }

        public void setQRCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getGetGoodsTime() {
            return getGoodsTime;
        }

        public void setGetGoodsTime(String getGoodsTime) {
            this.getGoodsTime = getGoodsTime;
        }

        public String getUserIcon() {
            return userIcon;
        }

        public void setUserIcon(String userIcon) {
            this.userIcon = userIcon;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
