package com.lixin.freshmall.model;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2018/01/22
 * My mailbox is 1403241630@qq.com
 */

public class MyAddressBean {
    private String result;
    private String resultNote;
    private List<AddressList> addressList;

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

    public List<AddressList> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressList> addressList) {
        this.addressList = addressList;
    }

    public static class AddressList{
        private String addressId;
        private String userName;
        private String phone;
        private String address;
        private String longitude;
        private String latitude;
        private String inGround;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getInGround() {
            return inGround;
        }

        public void setInGround(String inGround) {
            this.inGround = inGround;
        }
    }
}

