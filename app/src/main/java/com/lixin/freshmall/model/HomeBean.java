package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/22
 * My mailbox is 1403241630@qq.com
 */

public class HomeBean {
    private String result;
    private String resultNote ;
    private List<rotateTopCommoditys> rotateTopCommoditys;
    private List<classifyBottom> classifyBottom;
    private List<ThemeList> themeList;

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

    public List<HomeBean.rotateTopCommoditys> getRotateTopCommoditys() {
        return rotateTopCommoditys;
    }

    public void setRotateTopCommoditys(List<HomeBean.rotateTopCommoditys> rotateTopCommoditys) {
        this.rotateTopCommoditys = rotateTopCommoditys;
    }

    public List<HomeBean.classifyBottom> getClassifyBottom() {
        return classifyBottom;
    }

    public void setClassifyBottom(List<HomeBean.classifyBottom> classifyBottom) {
        this.classifyBottom = classifyBottom;
    }

    public List<ThemeList> getThemeList() {
        return themeList;
    }

    public void setThemeList(List<ThemeList> themeList) {
        this.themeList = themeList;
    }

    public class classifyBottom {
        private String classifyIcon;
        private String classifyType;
        private String meunid;

        public String getClassifyIcon() {
            return classifyIcon;
        }

        public void setClassifyIcon(String classifyIcon) {
            this.classifyIcon = classifyIcon;
        }

        public String getClassifyType() {
            return classifyType;
        }

        public void setClassifyType(String classifyType) {
            this.classifyType = classifyType;
        }

        public String getMeunid() {
            return meunid;
        }

        public void setMeunid(String meunid) {
            this.meunid = meunid;
        }
    }

    public class ThemeList{
        private String themeId;
        private String themeTitle;
        private String themeLogo;
        private String themePicture;
        private List<CommodityList> commodityList;

        public String getThemeId() {
            return themeId;
        }

        public void setThemeId(String themeId) {
            this.themeId = themeId;
        }

        public String getThemeTitle() {
            return themeTitle;
        }

        public void setThemeTitle(String themeTitle) {
            this.themeTitle = themeTitle;
        }

        public String getThemeLogo() {
            return themeLogo;
        }

        public void setThemeLogo(String themeLogo) {
            this.themeLogo = themeLogo;
        }

        public String getThemePicture() {
            return themePicture;
        }

        public void setThemePicture(String themePicture) {
            this.themePicture = themePicture;
        }

        public List<CommodityList> getCommodityList() {
            return commodityList;
        }

        public void setCommodityList(List<CommodityList> commodityList) {
            this.commodityList = commodityList;
        }

        public class CommodityList{
            private String commodityIcon;
            private String commodityNewPrice;
            private String commodityOriginalPrice;
            private String commodityTitle;
            private String commodityid;
            private String commodityUnit;

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

            public String getCommodityOriginalPrice() {
                return commodityOriginalPrice;
            }

            public void setCommodityOriginalPrice(String commodityOriginalPrice) {
                this.commodityOriginalPrice = commodityOriginalPrice;
            }

            public String getCommodityTitle() {
                return commodityTitle;
            }

            public void setCommodityTitle(String commodityTitle) {
                this.commodityTitle = commodityTitle;
            }

            public String getCommodityid() {
                return commodityid;
            }

            public void setCommodityid(String commodityid) {
                this.commodityid = commodityid;
            }

            public String getCommodityUnit() {
                return commodityUnit;
            }

            public void setCommodityUnit(String commodityUnit) {
                this.commodityUnit = commodityUnit;
            }
        }

    }

    public class rotateTopCommoditys{
        private String rotateIcon;
        private int rotateType;
        private String rotateid;

        public String getRotateIcon() {
            return rotateIcon;
        }

        public void setRotateIcon(String rotateIcon) {
            this.rotateIcon = rotateIcon;
        }

        public int getRotateType() {
            return rotateType;
        }

        public void setRotateType(int rotateType) {
            this.rotateType = rotateType;
        }

        public String getRotateid() {
            return rotateid;
        }

        public void setRotateid(String rotateid) {
            this.rotateid = rotateid;
        }
    }
}


