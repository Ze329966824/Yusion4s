package com.yusion.shanghai.yusion4s.bean.amap;

import com.google.gson.Gson;

import java.util.List;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */
public class PoiResp {


    /**
     * status : 1
     * count : 133
     * info : OK
     * infocode : 10000
     * pois : [{"id":"B023B08SI2","name":"肯德基(杭州吴山店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"延安路1号吴山名楼1号楼","location":"120.164376,30.241119","tel":"0571-87552574;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"上城区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B09GEO","name":"肯德基(杭州永安店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"解放路199号(中山中路口)","location":"120.170285,30.250342","tel":"0571-87132253;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"上城区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B0BIU1","name":"肯德基(星光大道店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"江南大道228号星光大道商业街1楼","location":"120.209210,30.207292","tel":"0571-88831751;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"滨江区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B09LTL","name":"肯德基(武林服饰城店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"凤起路519号3幢","location":"120.160933,30.263085","tel":"0571-87039487;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"下城区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B07GCB","name":"肯德基(泰富店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"通惠中路1号绿都泰富广场1层","location":"120.284609,30.164462","tel":"0571-82700177;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"萧山区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B09LU5","name":"肯德基(北山路店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"北山街85-87号","location":"120.134515,30.251594","tel":"0571-87972657","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"西湖区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B08WGP","name":"肯德基(教工路店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"教工路23号百脑汇科技大厦1层","location":"120.135982,30.274051","tel":"0571-89922520;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"西湖区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B0A6FY","name":"肯德基(B航站楼)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"机场路T3航站楼16号门口","location":"120.437058,30.234345","tel":[],"distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"萧山区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B0A6KD","name":"肯德基(莱蒙商业中心)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"临平街道藕花洲大街303号莱蒙商业中心F1层","location":"120.297941,30.415401","tel":"0571-89269190;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"余杭区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B08W26","name":"肯德基(杭州文三店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"文三路478号华星时代广场C座1层","location":"120.125656,30.276765","tel":"0571-86689651;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"西湖区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B07GCA","name":"肯德基(杭州浦沿店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"浦沿镇浦沿路765号","location":"120.146911,30.171643","tel":"0571-86616173;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"滨江区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B0A7P6","name":"肯德基(九堡店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"杭海路878号金海城购物中心","location":"120.267072,30.308611","tel":"0571-86909117","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"江干区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B0BEY8","name":"肯德基(四季青店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"杭海路98-108号意法服饰城2层","location":"120.196597,30.247478","tel":"0571-86504770;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"江干区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B07GC0","name":"肯德基(千岛湖店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"千岛湖镇新安大街78号","location":"119.044674,29.604092","tel":"0571-64827777","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"淳安县","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B08YQF","name":"肯德基(杭州庆春店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"庆春路70号(近庆春电影大世界)","location":"120.176960,30.258200","tel":"0571-87218955;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"下城区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B02N8Q","name":"肯德基(上乘店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"近江路1号1层(沃尔玛超市旁)","location":"120.192083,30.240232","tel":"0571-86512865","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"上城区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B0BE3K","name":"肯德基(杭州北站店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"莫干山路1165号汽车北站对面(月星家居1层)","location":"120.111692,30.316058","tel":"0571-85354031;4008823823","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"拱墅区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B02ORN","name":"肯德基(岳王店)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"北山路88号(近岳王庙)","location":"120.133310,30.252238","tel":"0571-87962446","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"西湖区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B07GC6","name":"肯德基(新美商城)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"下沙4号大街15号新美商城F1层","location":"120.347194,30.306522","tel":"0571-86725795","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"江干区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]},{"id":"B023B0BP9R","name":"肯德基(福雷德广场)","type":"餐饮服务;快餐厅;肯德基","typecode":"050301","biz_type":"diner","address":"文泽路99号福雷德广场2F层","location":"120.347354,30.316277","tel":"0571-86628209","distance":[],"biz_ext":[],"pname":"浙江省","cityname":"杭州市","adname":"江干区","importance":[],"shopid":[],"shopinfo":"0","poiweight":[]}]
     */

    public String status;
    public String count;
    public String info;
    public String infocode;
    public List<PoisBean> pois;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class PoisBean {
        /**
         * id : B023B08SI2
         * name : 肯德基(杭州吴山店)
         * type : 餐饮服务;快餐厅;肯德基
         * typecode : 050301
         * biz_type : diner
         * address : 延安路1号吴山名楼1号楼
         * location : 120.164376,30.241119
         * tel : 0571-87552574;4008823823
         * distance : []
         * biz_ext : []
         * pname : 浙江省
         * cityname : 杭州市
         * adname : 上城区
         * importance : []
         * shopid : []
         * shopinfo : 0
         * poiweight : []
         */

        public String name;
        public String type;
        public Object address;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}
