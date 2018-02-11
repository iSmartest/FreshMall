package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/1
 * My mailbox is 1403241630@qq.com
 */

public class LifeBean {
    /**
     *  result:"0" //0成功 1失败
     resultNote:"失败原因"
     totalPage:5//总页数
     lifeList:[{
     lifeIcon:"http://dsdf"//生活文章照片
     lifeTitle:""//生活文章标题
     lifeDec:""//精美语句
     lifeUrl:"http://"生活文章链接
     }]
     */
    private String result;
    private String resultNote;
    private int totalPage;
    private List<LifeList> lifeList;

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

    public List<LifeList> getLifeList() {
        return lifeList;
    }

    public void setLifeList(List<LifeList> lifeList) {
        this.lifeList = lifeList;
    }

    public static class LifeList{
        private String lifeIcon;
        private String lifeTitle;
        private String lifeDec;
        private String lifeUrl;

        public String getLifeIcon() {
            return lifeIcon;
        }

        public void setLifeIcon(String lifeIcon) {
            this.lifeIcon = lifeIcon;
        }

        public String getLifeTitle() {
            return lifeTitle;
        }

        public void setLifeTitle(String lifeTitle) {
            this.lifeTitle = lifeTitle;
        }

        public String getLifeDec() {
            return lifeDec;
        }

        public void setLifeDec(String lifeDec) {
            this.lifeDec = lifeDec;
        }

        public String getLifeUrl() {
            return lifeUrl;
        }

        public void setLifeUrl(String lifeUrl) {
            this.lifeUrl = lifeUrl;
        }
    }
}
