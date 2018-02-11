package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class StoreBean {
    /**
     *  result:"0" //0成功1失败
     resultNote:"失败原因"
     storeList:[{
     storeId:"12"//
     storeName:""//门店名字
     storeAderss:""//门店地址
     storeDistance:""//门店距离
     storeTime:""//门店营业时间
     storePhone:""//门店电话
     }]
     */
    private String result;
    private String resultNote;
    private List<StoreList> storeList;

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

    public List<StoreList> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<StoreList> storeList) {
        this.storeList = storeList;
    }

    public static class StoreList{
        private String storeId;
        private String storeName;
        private String storeAderss;
        private String storeDistance;
        private String storeTime;
        private String storePhone;
        private String issend;

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreAderss() {
            return storeAderss;
        }

        public void setStoreAderss(String storeAderss) {
            this.storeAderss = storeAderss;
        }

        public String getStoreDistance() {
            return storeDistance;
        }

        public void setStoreDistance(String storeDistance) {
            this.storeDistance = storeDistance;
        }

        public String getStoreTime() {
            return storeTime;
        }

        public void setStoreTime(String storeTime) {
            this.storeTime = storeTime;
        }

        public String getStorePhone() {
            return storePhone;
        }

        public void setStorePhone(String storePhone) {
            this.storePhone = storePhone;
        }

        public String getIssend() {
            return issend;
        }

        public void setIssend(String issend) {
            this.issend = issend;
        }
    }

}
