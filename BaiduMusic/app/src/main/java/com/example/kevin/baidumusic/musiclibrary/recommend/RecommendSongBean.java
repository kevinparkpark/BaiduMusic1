package com.example.kevin.baidumusic.musiclibrary.recommend;

import java.util.List;

/**
 * Created by kevin on 16/6/8.
 */
public class RecommendSongBean {

    /**
     * error_code : 22000
     * content : {"title":"热门歌单","list":[{"listid":"6661","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_48d86af8f04bd613ec403bc219940034.jpg","listenum":"22608","collectnum":"503","title":"前奏控！抓住你的耳朵","tag":"好听,国语,欧美","type":"gedan"},{"listid":"6663","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_e85bcff13efadaeb9671cb18f8b7c6cb.jpg","listenum":"11165","collectnum":"427","title":"经典灵魂乐合集（一）","tag":"欧美,灵魂乐,节奏布鲁斯,好听,经典","type":"gedan"},{"listid":"6204","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_91dd69ff7e0b8480733f8c940e37fb06.jpg","listenum":"97551","collectnum":"976","title":"节奏太强！欧美乐迷的抖腿福音","tag":"劲爆,热歌,欧美","type":"gedan"},{"listid":"5822","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_18db3c2612e3d482807ecd8038b4b2a6.jpg","listenum":"93288","collectnum":"832","title":"那些令人回味的电影原声","tag":"原声,伤感,好听,开心","type":"gedan"},{"listid":"6662","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_88b164b7558468fe3c1b857b3605d660.jpg","listenum":"5425","collectnum":"400","title":"悉数香港乐队最值得聆听的作品","tag":"粤语,经典,好听,80后","type":"gedan"},{"listid":"6660","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_7a250452ca29dc25b46e67c22dd63a3f.jpg","listenum":"24000","collectnum":"371","title":"那些创作才子们的音乐梦想","tag":"国语,原创,好听","type":"gedan"}]}
     */

    private int error_code;
    /**
     * title : 热门歌单
     * list : [{"listid":"6661","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_48d86af8f04bd613ec403bc219940034.jpg","listenum":"22608","collectnum":"503","title":"前奏控！抓住你的耳朵","tag":"好听,国语,欧美","type":"gedan"},{"listid":"6663","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_e85bcff13efadaeb9671cb18f8b7c6cb.jpg","listenum":"11165","collectnum":"427","title":"经典灵魂乐合集（一）","tag":"欧美,灵魂乐,节奏布鲁斯,好听,经典","type":"gedan"},{"listid":"6204","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_91dd69ff7e0b8480733f8c940e37fb06.jpg","listenum":"97551","collectnum":"976","title":"节奏太强！欧美乐迷的抖腿福音","tag":"劲爆,热歌,欧美","type":"gedan"},{"listid":"5822","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_18db3c2612e3d482807ecd8038b4b2a6.jpg","listenum":"93288","collectnum":"832","title":"那些令人回味的电影原声","tag":"原声,伤感,好听,开心","type":"gedan"},{"listid":"6662","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_88b164b7558468fe3c1b857b3605d660.jpg","listenum":"5425","collectnum":"400","title":"悉数香港乐队最值得聆听的作品","tag":"粤语,经典,好听,80后","type":"gedan"},{"listid":"6660","pic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_7a250452ca29dc25b46e67c22dd63a3f.jpg","listenum":"24000","collectnum":"371","title":"那些创作才子们的音乐梦想","tag":"国语,原创,好听","type":"gedan"}]
     */

    private ContentBean content;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        private String title;
        /**
         * listid : 6661
         * pic : http://business.cdn.qianqian.com/qianqian/pic/bos_client_48d86af8f04bd613ec403bc219940034.jpg
         * listenum : 22608
         * collectnum : 503
         * title : 前奏控！抓住你的耳朵
         * tag : 好听,国语,欧美
         * type : gedan
         */

        private List<ListBean> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String listid;
            private String pic;
            private String listenum;
            private String collectnum;
            private String title;
            private String tag;
            private String type;

            public String getListid() {
                return listid;
            }

            public void setListid(String listid) {
                this.listid = listid;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getListenum() {
                return listenum;
            }

            public void setListenum(String listenum) {
                this.listenum = listenum;
            }

            public String getCollectnum() {
                return collectnum;
            }

            public void setCollectnum(String collectnum) {
                this.collectnum = collectnum;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
