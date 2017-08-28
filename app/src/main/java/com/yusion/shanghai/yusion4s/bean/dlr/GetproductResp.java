package com.yusion.shanghai.yusion4s.bean.dlr;

import java.util.List;

/**
 * Created by aa on 2017/8/25.
 */


public class GetproductResp {


    public List<SupportAreaBean> support_area;
    public List<ProductListBean> product_list;

    public List<SupportAreaBean> getSupport_area() {
        return support_area;
    }

    public void setSupport_area(List<SupportAreaBean> support_area) {
        this.support_area = support_area;
    }

    public List<ProductListBean> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<ProductListBean> product_list) {
        this.product_list = product_list;
    }

    public static class SupportAreaBean {
        /**
         * province : 江苏
         * city : 苏州
         * district : 工业园区
         */

        private String province;
        private String city;
        private String district;

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

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }
    }

    public static class
    ProductListBean {
        /**
         * name : 测试
         * product_id : 1
         */

        private String name;
        private int product_id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }
    }
}
