package com.yusion.shanghai.yusion4s.bean.upload;

import com.google.gson.Gson;

import java.util.List;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/20 下午3:03
 */

public class UploadFilesUrlReq {
    public List<FileUrlBean> files;
    public String bucket;
    public String region;

    public static class FileUrlBean {
        public String clt_id;
        public String app_id;
        public String label;//身份证正面,身份证反面
        public String file_id;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
