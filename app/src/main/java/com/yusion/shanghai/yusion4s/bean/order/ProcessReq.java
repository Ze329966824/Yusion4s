package com.yusion.shanghai.yusion4s.bean.order;

import java.util.List;

/**
 * Created by aa on 2017/12/5.
 */

public class ProcessReq {

    /**
     * clt_nm : xxx
     * app_id : ttt
     * create_time : 2017-12-06 12:12:12
     * list : [{"st":"pass","time":"","title":"提交申请","seriesList":[],"parellelList":[]},{"st":"ns","time":"","title":"征信审核","seriesList":[],"parellelList":[]},{"st":"ns","time":"","title":"确认金融方案","seriesList":[],"parellelList":[]},{"st":"ns","time":"","title":"贷后追踪","seriesList":[],"parellelList":[]},{"st":"ns","time":"","title":"放款","seriesList":[],"parellelList":[]},{"st":"ns","time":"","title":"放款审核","seriesList":[],"parellelList":[]}]
     */

    public String clt_nm;
    public String app_id;
    public String create_time;
    public List<ListBean> list;

    public String getClt_nm() {
        return clt_nm;
    }

    public void setClt_nm(String clt_nm) {
        this.clt_nm = clt_nm;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * st : pass
         * time :
         * title : 提交申请
         * seriesList : []
         * parellelList : []
         */

        public String st;
        public String time;
        public String title;
        public List<?> seriesList;
        public List<?> parellelList;

        public String getSt() {
            return st;
        }

        public void setSt(String st) {
            this.st = st;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<?> getSeriesList() {
            return seriesList;
        }

        public void setSeriesList(List<?> seriesList) {
            this.seriesList = seriesList;
        }

        public List<?> getParellelList() {
            return parellelList;
        }

        public void setParellelList(List<?> parellelList) {
            this.parellelList = parellelList;
        }
    }
}
