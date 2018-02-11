package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/13
 * My mailbox is 1403241630@qq.com
 */

public class ShoppingBag {
    /**
     * result:"0" //0成功1失败
     resultNote:"失败原因"
     shoppingBagList[{
     shoppingBag:""//购物袋类型(用于提交订单时传参)
     shoppingBagMoney:""//购物袋金额
     }]
     */
    private String result;
    private String resultNote;
    private List<ShoppingBagList> shoppingBagList;

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

    public List<ShoppingBagList> getShoppingBagList() {
        return shoppingBagList;
    }

    public void setShoppingBagList(List<ShoppingBagList> shoppingBagList) {
        this.shoppingBagList = shoppingBagList;
    }

    public class ShoppingBagList{
        private String shoppingBag;
        private String shoppingBagMoney;
        private String shoppingBagId;

        public String getShoppingBag() {
            return shoppingBag;
        }

        public void setShoppingBag(String shoppingBag) {
            this.shoppingBag = shoppingBag;
        }

        public String getShoppingBagMoney() {
            return shoppingBagMoney;
        }

        public void setShoppingBagMoney(String shoppingBagMoney) {
            this.shoppingBagMoney = shoppingBagMoney;
        }
    }
}
