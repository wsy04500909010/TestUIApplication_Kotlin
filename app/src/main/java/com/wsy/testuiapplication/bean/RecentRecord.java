package com.wsy.testuiapplication.bean;

import java.util.List;

/**
 * Created by WangSiYe on 2019/4/3.
 */
public class RecentRecord {

    /**
     * numFound : 1
     * "startAt":15020302039
     * list : [{"rootId":3,"seriesId":5,"seriesName":"权力的游戏","programId":"1-3",
     * "programName":"权力的游戏第3集","updatedTo":"8","position":80,"duration":120,
     * "portraitPoster":"http://www.baidu.com","landscapePoster":"http://www.baidu.com",
     * "clientType":1001,"accId":"100923510","updatedAt":"1556198974\u2028"}]
     */

    private int numFound;
    private int startAt;
    private List<ListBean> list;

    public int getNumFound() {
        return numFound;
    }

    public void setNumFound(int numFound) {
        this.numFound = numFound;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * rootId : 3
         * seriesId : 5
         * seriesName : 权力的游戏
         * programId : 1-3
         * programName : 权力的游戏第3集
         * updatedTo : 8
         * position : 80
         * duration : 120
         * portraitPoster : http://www.baidu.com
         * landscapePoster : http://www.baidu.com
         * clientType : 1001
         * accId : 100923510
         * updatedAt : 1556198974
         */

        private int rootId;
        private int seriesId;
        private String seriesName;
        private String programId;
        private String programName;
        private String updatedTo;
        private int position;
        private int duration;
        private String portraitPoster;
        private String landscapePoster;
        private int clientType;
        private String accId;
        private String updatedAt;

        public int getRootId() {
            return rootId;
        }

        public void setRootId(int rootId) {
            this.rootId = rootId;
        }

        public int getSeriesId() {
            return seriesId;
        }

        public void setSeriesId(int seriesId) {
            this.seriesId = seriesId;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        public String getProgramId() {
            return programId;
        }

        public void setProgramId(String programId) {
            this.programId = programId;
        }

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }

        public String getUpdatedTo() {
            return updatedTo;
        }

        public void setUpdatedTo(String updatedTo) {
            this.updatedTo = updatedTo;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getPortraitPoster() {
            return portraitPoster;
        }

        public void setPortraitPoster(String portraitPoster) {
            this.portraitPoster = portraitPoster;
        }

        public String getLandscapePoster() {
            return landscapePoster;
        }

        public void setLandscapePoster(String landscapePoster) {
            this.landscapePoster = landscapePoster;
        }

        public int getClientType() {
            return clientType;
        }

        public void setClientType(int clientType) {
            this.clientType = clientType;
        }

        public String getAccId() {
            return accId;
        }

        public void setAccId(String accId) {
            this.accId = accId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    @Override
    public String toString() {
        return "RecentRecord{" +
                "numFound=" + numFound +
                ", startAt=" + startAt +
                ", list=" + list +
                '}';
    }
}