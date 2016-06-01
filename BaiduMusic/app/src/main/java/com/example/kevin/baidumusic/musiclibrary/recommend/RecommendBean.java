package com.example.kevin.baidumusic.musiclibrary.recommend;

import java.util.List;

/**
 * Created by kevin on 16/5/31.
 */
public class RecommendBean {

    /**
     * pic : [{"randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1464682834ee7774835d35cc033699aa2180835e49.jpg","randpic_ios5":"","randpic_desc":"初夏小清新","randpic_ipad":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_146468296729e1e935f78375343bd1b09c35068b0b.jpg","randpic_qq":"","randpic_2":"bos_client_1464682988fcec9a5f124ac08a1c2e2f26e9949a55","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1464682988fcec9a5f124ac08a1c2e2f26e9949a55.jpg","special_type":0,"ipad_desc":"初夏小清新","is_publish":"111100","mo_type":"5","type":7,"code":"6641"},{"randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1464606356da2ee36794146c7ccf7f4cde8b4c4788.jpg","randpic_ios5":"","randpic_desc":"K歌大赛","randpic_ipad":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14646063634a8ef1f828fb348a14e5a6196f61bf14.jpg","randpic_qq":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14646063680be37f03b6b98995ebc45e803791a662.jpg","randpic_2":"bos_client_14646063741e7ec371016f3219b0050aec178e3325","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14646063741e7ec371016f3219b0050aec178e3325.jpg","special_type":0,"ipad_desc":"K歌大赛","is_publish":"111100","mo_type":"7","type":9,"code":"13042594_13093215"},{"randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_146457676993ff7b36fe7d19254863cea34b6cbce3.jpg","randpic_ios5":"","randpic_desc":"蜜蜂少女队","randpic_ipad":"","randpic_qq":"","randpic_2":"bos_client_1464576777626433a6a92501407b820fbb625110e6","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1464576777626433a6a92501407b820fbb625110e6.jpg","special_type":0,"ipad_desc":"蜜蜂少女队","is_publish":"110000","mo_type":"4","type":6,"code":"http://music.baidu.com/cms/webview/beeGirls/eachTopic11/index.html"},{"randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14644932561c3f99fdeea3475777a87fb2c5ffbcb9.jpg","randpic_ios5":"","randpic_desc":"歌单精选","randpic_ipad":"","randpic_qq":"","randpic_2":"bos_client_14644932617a76d7986b0760d00e3c18c46b57e34e","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_14644932617a76d7986b0760d00e3c18c46b57e34e.jpg","special_type":0,"ipad_desc":"歌单精选","is_publish":"110000","mo_type":"4","type":6,"code":"http://music.baidu.com/cms/webview/mobile-temp-special/b18/index.html"},{"randpic":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1464320733e20998fd84ed6597ef9936cbdcb9915c.jpg","randpic_ios5":"","randpic_desc":"新歌榜vol.21","randpic_ipad":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_146432073861df86c67305219f1c95141605984f58.jpg","randpic_qq":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_146433660860af553a9d690afa8957c0daaabbc794.jpg","randpic_2":"bos_client_1464320754fb32ef1d4458da2fc8ae45627d7837e4","randpic_iphone6":"http://business.cdn.qianqian.com/qianqian/pic/bos_client_1464320754fb32ef1d4458da2fc8ae45627d7837e4.jpg","special_type":0,"ipad_desc":"新歌榜vol.21","is_publish":"111100","mo_type":"5","type":7,"code":"6630"}]
     * error_code : 22000
     */

    private int error_code;
    /**
     * randpic : http://business.cdn.qianqian.com/qianqian/pic/bos_client_1464682834ee7774835d35cc033699aa2180835e49.jpg
     * randpic_ios5 :
     * randpic_desc : 初夏小清新
     * randpic_ipad : http://business.cdn.qianqian.com/qianqian/pic/bos_client_146468296729e1e935f78375343bd1b09c35068b0b.jpg
     * randpic_qq :
     * randpic_2 : bos_client_1464682988fcec9a5f124ac08a1c2e2f26e9949a55
     * randpic_iphone6 : http://business.cdn.qianqian.com/qianqian/pic/bos_client_1464682988fcec9a5f124ac08a1c2e2f26e9949a55.jpg
     * special_type : 0
     * ipad_desc : 初夏小清新
     * is_publish : 111100
     * mo_type : 5
     * type : 7
     * code : 6641
     */

    private List<PicBean> pic;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<PicBean> getPic() {
        return pic;
    }

    public void setPic(List<PicBean> pic) {
        this.pic = pic;
    }

    public static class PicBean {
        private String randpic;
        private String randpic_ios5;
        private String randpic_desc;
        private String randpic_ipad;
        private String randpic_qq;
        private String randpic_2;
        private String randpic_iphone6;
        private int special_type;
        private String ipad_desc;
        private String is_publish;
        private String mo_type;
        private int type;
        private String code;

        public String getRandpic() {
            return randpic;
        }

        public void setRandpic(String randpic) {
            this.randpic = randpic;
        }

        public String getRandpic_ios5() {
            return randpic_ios5;
        }

        public void setRandpic_ios5(String randpic_ios5) {
            this.randpic_ios5 = randpic_ios5;
        }

        public String getRandpic_desc() {
            return randpic_desc;
        }

        public void setRandpic_desc(String randpic_desc) {
            this.randpic_desc = randpic_desc;
        }

        public String getRandpic_ipad() {
            return randpic_ipad;
        }

        public void setRandpic_ipad(String randpic_ipad) {
            this.randpic_ipad = randpic_ipad;
        }

        public String getRandpic_qq() {
            return randpic_qq;
        }

        public void setRandpic_qq(String randpic_qq) {
            this.randpic_qq = randpic_qq;
        }

        public String getRandpic_2() {
            return randpic_2;
        }

        public void setRandpic_2(String randpic_2) {
            this.randpic_2 = randpic_2;
        }

        public String getRandpic_iphone6() {
            return randpic_iphone6;
        }

        public void setRandpic_iphone6(String randpic_iphone6) {
            this.randpic_iphone6 = randpic_iphone6;
        }

        public int getSpecial_type() {
            return special_type;
        }

        public void setSpecial_type(int special_type) {
            this.special_type = special_type;
        }

        public String getIpad_desc() {
            return ipad_desc;
        }

        public void setIpad_desc(String ipad_desc) {
            this.ipad_desc = ipad_desc;
        }

        public String getIs_publish() {
            return is_publish;
        }

        public void setIs_publish(String is_publish) {
            this.is_publish = is_publish;
        }

        public String getMo_type() {
            return mo_type;
        }

        public void setMo_type(String mo_type) {
            this.mo_type = mo_type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
