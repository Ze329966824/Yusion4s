package com.yusion.shanghai.yusion4s.bean.oss;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/8/4.
 */

public class GetOssTokenBean {
    /**
     * AccessKeySecret : 6oNxVLRDvdyyNH4UfdFbT3a4ygisc13ESxxtHVfKn6fL
     * AccessKeyId : STS.G1thjXvFt9SafBP3WGXfzCAPp
     * Expiration : 2017-04-10T04:21:29Z
     * SecurityToken : CAIS9AF1q6Ft5B2yfSjIpvLBI9Dsm5lVjpGKZGTh11cSVOlWrIT7kjz2IH9Ee3dtBuEetPkxlG9Z7fYclqN6U4cATkjFYM1stnP/eNt8INivgde8yJBZor/HcDHhJnyW9cvWZPqDP7G5U/yxalfCuzZuyL/hD1uLVECkNpv74vwOLK5gPG+CYCFBGc1dKyZ7tcYeLgGxD/u2NQPwiWeiZygB+CgE0DoutvnhnZTEs0uP3QWlk9V4/dqhfsKWCOB3J4p6XtuP2+h7S7HMyiY46WIRq/cp0vEbqG+d44/NXAgKs0ucUOPM6phoNxQ8frUmCxmDEh1xWAUSGoABDuXwVnZ9O4I18VMZzMhomUbmt+Qk+G9J3wAgBmJXTP16YRJH64QI/tyTTZRbNRYtyBAVe99PHuQaebXir4eQ+KkghIiyyfy2G+v/QWwYarWj8ryqKhUSuQ90ThcBOidZ2zZnhYnIgCoTEXH7WZy4vPvV5J6PKHS8b8W8Rfhkz3E=
     * Url : https://wzfinance-test.oss-cn-shanghai.aliyuncs.com
     */

    public String AccessKeySecret;
    public String AccessKeyId;
    public String Expiration;
    public String SecurityToken;
    public FidDetailBean FidDetail;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public class FidDetailBean {
        public String Bucket;
        public String Region;
    }
}
