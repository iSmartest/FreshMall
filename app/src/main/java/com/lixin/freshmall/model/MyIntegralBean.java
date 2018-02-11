package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/11
 * My mailbox is 1403241630@qq.com
 */

public class MyIntegralBean {
    /**
     * result:"0" //0成功1失败
     resultNote:"失败原因"
     totalPage:"5"总页数
     totalMoney:""//总积分
     integralGrade:"普通会员"//积分等级
     moneyList:[{//积分明细
     shoppingPayNum:"25"//购物支付金额
     integralNum:""//积分数
     integralTime:""//获取积分的时间
     }]
     */
    private String result;
    private String resultNote;
    private int totalPage;
    private String totalMoney;
    private String integralGrade;
    private List<MoneyList> moneyList;

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

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getIntegralGrade() {
        return integralGrade;
    }

    public void setIntegralGrade(String integralGrade) {
        this.integralGrade = integralGrade;
    }

    public List<MoneyList> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(List<MoneyList> moneyList) {
        this.moneyList = moneyList;
    }

    public class MoneyList{
        private String shoppingPayNum;
        private String integralNum;
        private String integralTime;

        public String getShoppingPayNum() {
            return shoppingPayNum;
        }

        public void setShoppingPayNum(String shoppingPayNum) {
            this.shoppingPayNum = shoppingPayNum;
        }

        public String getIntegralNum() {
            return integralNum;
        }

        public void setIntegralNum(String integralNum) {
            this.integralNum = integralNum;
        }

        public String getIntegralTime() {
            return integralTime;
        }

        public void setIntegralTime(String integralTime) {
            this.integralTime = integralTime;
        }
    }
}
