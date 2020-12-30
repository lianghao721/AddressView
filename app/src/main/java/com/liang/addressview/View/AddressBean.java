package com.liang.addressview.View;

import java.io.Serializable;
import java.util.List;

/*
 * 地址选择器数据实体模型
 * Author:Lianghao
 * CreateTime:2020/9/28
 */

public class AddressBean implements Serializable {

    private List<AddressItemBean> province;//省
    private List<AddressItemBean> city;//市
    private List<AddressItemBean> district;//县
    private List<AddressItemBean> town;//镇

    public List<AddressItemBean> getProvince() {
        return province;
    }

    public void setProvince(List<AddressItemBean> province) {
        this.province = province;
    }

    public List<AddressItemBean> getCity() {
        return city;
    }

    public void setCity(List<AddressItemBean> city) {
        this.city = city;
    }

    public List<AddressItemBean> getDistrict() {
        return district;
    }

    public void setDistrict(List<AddressItemBean> district) {
        this.district = district;
    }

    public List<AddressItemBean> getTown() {
        return town;
    }

    public void setTown(List<AddressItemBean> town) {
        this.town = town;
    }

    public static class AddressItemBean implements Serializable {
        private String code;
        private String name;
        private String province;
        private String city;
        private String area;
        private String town;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }
    }
}
