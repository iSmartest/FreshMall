package com.lixin.freshmall.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/12/9
 * My mailbox is 1403241630@qq.com
 */

public class CityInfoBean {
    /**
     * result:"0" //0成功1失败
     resultNote:"失败原因"
     provinceList:[{
     province:"河南省"//省
     cityList:[{
     city:"郑州市"//市、
     townList:[{
     townId:""//县区id
     town:"金水区"//市辖区、县、县级市
     }]
     }]
     }]
     */

    private String result;
    private String resultNote;
    private List<ProvinceList> provinceList;

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

    public List<ProvinceList> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvinceList> provinceList) {
        this.provinceList = provinceList;
    }

    public static class ProvinceList{
        private String province;
        private List<CityList> cityList;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public List<CityList> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityList> cityList) {
            this.cityList = cityList;
        }

        public static class CityList implements Serializable {
            private String city;
            private List<TownList> townList;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public List<TownList> getTownList() {
                return townList;
            }

            public void setTownList(List<TownList> townList) {
                this.townList = townList;
            }

            public static class TownList implements Serializable{
                private String townId;
                private String town;

                public String getTownId() {
                    return townId;
                }

                public void setTownId(String townId) {
                    this.townId = townId;
                }

                public String getTown() {
                    return town;
                }

                public void setTown(String town) {
                    this.town = town;
                }
            }
        }
    }
}
