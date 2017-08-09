package com.yusion.shanghai.yusion4s.bean.upload;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ice on 2017/7/17.
 */

public class UploadLabelItemBean implements Serializable {

    /**
     * name : "主贷人"
     * value : lender
     * img_list : []
     * label_list : [{"name":"征信授权书","value":"auth_credit","img_list":[],"label_list":[]},{"name":"身份证正面","value":"id_card_front","img_list":[],"label_list":[]},{"name":"身份证反面","value":"id_card_back","img_list":[],"label_list":[]},{"name":"结婚证","value":"marriage_proof","img_list":[],"label_list":[]},{"name":"户口簿","value":"res_booklet","img_list":[],"label_list":[]}]
     */
    public String name;
    public String value;
    public List<UploadImgItemBean> img_list;
    public List<UploadLabelItemBean> label_list;

    public boolean hasError = false;
    public String errorInfo = "";
    public boolean hasGetImgsFromServer = false;

    @Override
    public String toString() {
        return "UploadLabelItemBean{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", img_list=" + img_list +
                ", label_list=" + label_list +
                ", hasError=" + hasError +
                ", errorInfo='" + errorInfo + '\'' +
                ", hasGetImgsFromServer=" + hasGetImgsFromServer +
                '}';
    }
}
