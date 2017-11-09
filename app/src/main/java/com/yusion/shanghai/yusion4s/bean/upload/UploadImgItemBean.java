package com.yusion.shanghai.yusion4s.bean.upload;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by ice on 2017/7/17.
 */

public class UploadImgItemBean implements Serializable {



    public String local_path;
    public String type; //照片类型
    public String role; //照片所属角色
    public String objectKey;

    //            获取图片列表接口
    //            "file_id": "CUSTOMER/1500443528900.png",
    //            "role": "lender",
    //            "s_url": "https://yusiontech-test.oss-cn-hangzhou.aliyuncs.com/CUSTOMER/1500443528900.png?x-oss-process=image%2Fresize%2Cm_fill%2Ch_300%2Cw_300&OSSAccessKeyId=STS.E62ZH32HT7ojFDUC8kfqXAK5r&security-token=CAIS9AF1q6Ft5B2yfSjIpPWHEfKH35d1gK2BRGLkpzg%2Bav50ro6ekDz2IHxOfnBtAekbsPo%2FlWBZ6PYclqN6U4cATkjFYM1sthWedb4cINivgde8yJBZor%2FHcDHhJnyW9cvWZPqDP7G5U%2FyxalfCuzZuyL%2FhD1uLVECkNpv74vwOLK5gPG%2BCYCFBGc1dKyZ7tcYeLgGxD%2Fu2NQPwiWeiZygB%2BCgE0Dwjt%2Fnun5LCsUOP0AGrkdV4%2FdqhfsKWCOB3J4p6XtuP2%2Bh7S7HMyiY46WIRqP0s1fEcoGqZ4IHMUwgPs0ucUOPM6phoNxQ8aaUmCzu4ZDBEbRgTGoABrkLg%2FnoJ8M%2B1yi1nhHZ2e%2F%2Bvw2Lso%2Bx1E7jT9JY8iyHjPlyS1wyc84AR52Z4NvPm%2FkWo6lSyrE7Fw7aOzWWK56yL8dXVwBzUy1Bi6DRu3p2TqVPoeJLnQcjiOr%2B0cpcn1QxTZvRRwkdqwB6bolC%2BSMywAVPlETovDfcdn9dGTJk%3D&Expires=1500464955&Signature=rzGKfqkctEayoQ%2BdFE2EJWTRXDU%3D",
    //            "bucket": "yusiontech-test",
    //            "id": 1,
    //            "region": "oss-cn-hangzhou.aliyuncs.com",
    //            "label": "auth_credit"
    public String s_url;
    public boolean hasChoose;
    public String id;
    public String raw_url;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
