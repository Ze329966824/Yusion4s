package com.yusion.shanghai.yusion4s.bean.dlr;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by aa on 2017/8/25.
 */


public class GetproductResp {

    public List<SupportAreaBean> support_area;
    public List<ProductListBean> product_list;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class SupportAreaBean {
        /**
         * name : 江苏
         * cityList : [{"name":"苏州","districtList":[{"name":"工业园区"},{"name":"吴中区"}]}]
         */
        public String che_300_id;
        public String name;
        public List<CityListBean> cityList;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

        public static class CityListBean {
            /**
             * name : 苏州
             * districtList : [{"name":"工业园区"},{"name":"吴中区"}]
             */
            public String che_300_id;
            public String name;
            public List<DistrictListBean> districtList;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }

            public static class DistrictListBean {
                /**
                 * name : 工业园区
                 */

                public String name;
                public String che_300_id;

                @Override
                public String toString() {
                    return new Gson().toJson(this);
                }
            }
        }
    }

    public static class ProductListBean {
        /**
         * name : 测试
         * product_id : 1
         */

        public String name;
        public int product_id;
        public List<Integer> nper_list;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
