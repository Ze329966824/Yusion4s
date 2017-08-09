package com.yusion.shanghai.yusion4s.bean.upload;

import java.util.List;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/20 下午3:03
 */

public class UploadFilesUrlReq {
    public String clt_id;
    public String app_id;
    public List<FileUrlBean> files;
    public String bucket;
    public String region;

    public static class FileUrlBean {
        public String label;//身份证正面,身份证反面
        public String file_id;
        public String role;//主贷人,担保人

        @Override
        public String toString() {
            return "FileUrlBean{" +
                    "label='" + label + '\'' +
                    ", file_id='" + file_id + '\'' +
                    ", role='" + role + '\'' +
                    '}';
        }
    }
}
