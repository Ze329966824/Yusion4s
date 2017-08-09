package com.yusion.shanghai.yusion4s.bean.upload;

import java.util.List;

/**
 * Created by ice on 2017/7/31.
 */

public class ListImgsResp {

    /**
     * list : [{"role":"lender","label":"id_card_back","file_id":"CUSTOMER/18356097679///1501226908103.png","id":37,"bucket":"yusiontech-test","region":"oss-cn-hangzhou.aliyuncs.com","s_url":"https://yusiontech-test.oss-cn-hangzhou.aliyuncs.com/CUSTOMER/18356097679///1501226908103.png?OSSAccessKeyId=STS.J8n5WkkBU3JqXeidfiEQU2x1A&security-token=CAIS9AF1q6Ft5B2yfSjIq%2Fvbfu3fhp10hIiaWkPYgGY8Sd553b2aozz2IHxOfnBtAekbsPo%2FlWBZ6PYclqN6U4cATkjFYM1sthnod9QQINivgde8yJBZor%2FHcDHhJnyW9cvWZPqDP7G5U%2FyxalfCuzZuyL%2FhD1uLVECkNpv74vwOLK5gPG%2BCYCFBGc1dKyZ7tcYeLgGxD%2Fu2NQPwiWeiZygB%2BCgE0Dwjt%2Fnun5LCsUOP0AGrkdV4%2FdqhfsKWCOB3J4p6XtuP2%2Bh7S7HMyiY46WIRqP0s1fEcoGqZ4IHMUwgPs0ucUOPM6phoNxQ8aaUmCzu4ZDBEbRgTGoABhTxWvmhFld5BjjwRYmXpoxxkDPiP58QzXQWzhToz%2FEYYA5SrVZFYou4blPF9NGKTfL9etMNR3qGEun0AWXTKdKv1tJkCPfNpVXHNdrKAEvrKrRctLJx67o6mz1MQkWlV9n6oxwLpUIkCSYqryjKdgnRlWv4HYkJJZ4gRKpnbYoM%3D&Expires=1501492599&Signature=OV0BBZftuhTNBoDFH3X55hFkrvo%3D"}]
     * error : xxx
     * has_err : true
     */

    public String error;
    public boolean has_err;
    public List<UploadImgItemBean> list;

    @Override
    public String toString() {
        return "ListImgsResp{" +
                "error='" + error + '\'' +
                ", has_err=" + has_err +
                ", list=" + list +
                '}';
    }
}
