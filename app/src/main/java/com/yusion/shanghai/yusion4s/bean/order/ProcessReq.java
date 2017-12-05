package com.yusion.shanghai.yusion4s.bean.order;

import java.util.List;

/**
 * Created by aa on 2017/12/5.
 */

public class ProcessReq {

    /**
     * st : null
     * title : null
     * time : null
     * seriesList : [{"st":"pass","title":"初级信审审批","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"高级信审审批","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"高级信审复审","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"初级信审审批","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"提交申请","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"DW UW 1st","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"提车资料审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"抵押资料审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"提车资料审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"抵押资料审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":null,"title":null,"time":null,"seriesList":[],"parallelList":[]},{"st":null,"title":null,"time":null,"seriesList":[],"parallelList":[]},{"st":"pass","title":"基本资料审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"基本资料审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"放款","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"合同影像件预审","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"返审审批","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":null,"title":null,"time":null,"seriesList":[],"parallelList":[{"st":"pass","title":"电话审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"征信影像件审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]}]},{"st":"pass","title":"电话审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"征信影像件审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"征信影像件审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"央行征信查询","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":null,"title":null,"time":null,"seriesList":[],"parallelList":[]},{"st":null,"title":null,"time":null,"seriesList":[],"parallelList":[]},{"st":"pass","title":"DW uw 2nd","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"家访影像件审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"用户确认金融方案","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"家访影像件审核","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"用户确认金融方案","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]},{"st":"pass","title":"人脸识别 电子签约","time":"2017-12-05 11:27:49","seriesList":[],"parallelList":[]}]
     * parallelList : []
     */

    private Object st;
    private Object title;
    private Object time;
    private List<SeriesListBean> seriesList;
    private List<?> parallelList;

    public Object getSt() {
        return st;
    }

    public void setSt(Object st) {
        this.st = st;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public List<SeriesListBean> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<SeriesListBean> seriesList) {
        this.seriesList = seriesList;
    }

    public List<?> getParallelList() {
        return parallelList;
    }

    public void setParallelList(List<?> parallelList) {
        this.parallelList = parallelList;
    }

    public static class SeriesListBean {
        /**
         * st : pass
         * title : 初级信审审批
         * time : 2017-12-05 11:27:49
         * seriesList : []
         * parallelList : []
         */

        private String st;
        private String title;
        private String time;
        private List<?> seriesList;
        private List<?> parallelList;

        public String getSt() {
            return st;
        }

        public void setSt(String st) {
            this.st = st;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<?> getSeriesList() {
            return seriesList;
        }

        public void setSeriesList(List<?> seriesList) {
            this.seriesList = seriesList;
        }

        public List<?> getParallelList() {
            return parallelList;
        }

        public void setParallelList(List<?> parallelList) {
            this.parallelList = parallelList;
        }
    }
}
