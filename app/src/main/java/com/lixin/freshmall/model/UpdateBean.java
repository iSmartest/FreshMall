package com.lixin.freshmall.model;

/**
 * Created by 小火
 * Create time on  2017/11/9
 * My mailbox is 1403241630@qq.com
 */

public class UpdateBean {
    private int versionNumber;//版本号
    private String updataAddress; //下载地址
    private String versionName; //版本名字
    private String descc; //简介
    private String result;
    private String resultNote;

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getUpdataAddress() {
        return updataAddress;
    }

    public void setUpdataAddress(String updataAddress) {
        this.updataAddress = updataAddress;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDescc() {
        return descc;
    }

    public void setDescc(String descc) {
        this.descc = descc;
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
}
