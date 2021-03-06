package com.example.kevin.baidumusic.kmusic;

import java.util.List;

/**
 * Created by kevin on 16/6/1.
 */
public class KMusicBean {

    /**
     * artist : [{"ting_uid":"2517","name":"薛之谦","firstchar":"X","gender":"0","area":"0","country":"中国","avatar_big":"http://musicdata.baidu.com/data2/pic/3ff5e1d37c04feb6d6bd3e6fe47f6c8b/246669446/246669446.jpg","avatar_middle":"http://musicdata.baidu.com/data2/pic/74312948b16e42bfa63e59994ec98981/246669450/246669450.jpg","avatar_small":"http://musicdata.baidu.com/data2/pic/0f626d22f80c97139dde60f9dd914723/246669481/246669481.jpg","avatar_mini":"http://musicdata.baidu.com/data2/pic/8779f839c9e01d78d1e1cf285ebda7fc/246669482/246669482.jpg","albums_total":"10","songs_total":"85","artist_id":"88","piao_id":"0"},{"ting_uid":"1091","name":"邓丽君","firstchar":"D","gender":"1","area":"1","country":"台湾","avatar_big":"http://musicdata.baidu.com/data2/pic/abae1f3dcefbf02c87fa350fd5f5f47b/246707410/246707410.jpg","avatar_middle":"http://musicdata.baidu.com/data2/pic/ddfd1c520e3bd5b33723831320c792c4/246707419/246707419.jpg","avatar_small":"http://musicdata.baidu.com/data2/pic/286595af53e1e658a72422b91766588f/246707442/246707442.jpg","avatar_mini":"http://musicdata.baidu.com/data2/pic/2cc9bae8e51f8fa4089176bdc21ad5fc/246707444/246707444.jpg","albums_total":"328","songs_total":"5763","artist_id":"116","piao_id":"0"},{"ting_uid":"45561","name":"王菲","firstchar":"W","gender":"1","area":"1","country":"香港","avatar_big":"http://musicdata.baidu.com/data2/pic/9fcb900ee396df1c29f2d75685efb9ae/246668445/246668445.jpg","avatar_middle":"http://musicdata.baidu.com/data2/pic/81c1a40725ec8fcab65c66e6dbe4a85d/246668451/246668451.jpg","avatar_small":"http://musicdata.baidu.com/data2/pic/56014dda2ddc382d9276ee52bbdc97ef/246668478/246668478.jpg","avatar_mini":"http://musicdata.baidu.com/data2/pic/ebcc27f0ae138e0af14d7d81f1fb10b8/246668479/246668479.jpg","albums_total":"110","songs_total":"1692","artist_id":"15","piao_id":"0"},{"ting_uid":"2507","name":"张学友","firstchar":"Z","gender":"0","area":"1","country":"香港","avatar_big":"http://musicdata.baidu.com/data2/pic/f05f1ffa06ed236f798cd8fdc00cb6da/246669554/246669554.jpg","avatar_middle":"http://musicdata.baidu.com/data2/pic/261946a4157956ff3fb1758bdc8953f5/246669558/246669558.jpg","avatar_small":"http://musicdata.baidu.com/data2/pic/17f3857e41cf7d2bdee29c996ddc57c9/246669567/246669567.jpg","avatar_mini":"http://musicdata.baidu.com/data2/pic/2c422abd1ad27de78889a0bf5d7bae38/246669572/246669572.jpg","albums_total":"179","songs_total":"2606","artist_id":"23","piao_id":"0"},{"ting_uid":"7898","name":"G.E.M.邓紫棋","firstchar":"G","gender":"1","area":"1","country":"香港","avatar_big":"http://musicdata.baidu.com/data2/pic/5983ac4a66671ae68b2b48cd54987c05/261932110/261932110.jpg","avatar_middle":"http://musicdata.baidu.com/data2/pic/a69c1f256eca3bd109cf9100c670ff53/261932113/261932113.jpg","avatar_small":"http://musicdata.baidu.com/data2/pic/d3dc4bed5f4e28518a1e80363f1be02c/261932117/261932117.jpg","avatar_mini":"http://musicdata.baidu.com/data2/pic/916f5a9ce271a7e745245c953e43a9c6/261932118/261932118.jpg","albums_total":"27","songs_total":"300","artist_id":"1814","piao_id":"0"},{"ting_uid":"245815","name":"祁隆","firstchar":"Q","gender":"0","area":"0","country":"中国","avatar_big":"http://musicdata.baidu.com/data2/pic/246668352/246668352.jpg","avatar_middle":"http://musicdata.baidu.com/data2/pic/246668356/246668356.jpg","avatar_small":"http://musicdata.baidu.com/data2/pic/246668362/246668362.jpg","avatar_mini":"http://musicdata.baidu.com/data2/pic/246668363/246668363.jpg","albums_total":"35","songs_total":"274","artist_id":"57297","piao_id":"0"}]
     * nums : 2000
     * noFirstChar :
     * havemore : 1
     */

    private int nums;
    private String noFirstChar;
    private int havemore;
    /**
     * ting_uid : 2517
     * name : 薛之谦
     * firstchar : X
     * gender : 0
     * area : 0
     * country : 中国
     * avatar_big : http://musicdata.baidu.com/data2/pic/3ff5e1d37c04feb6d6bd3e6fe47f6c8b/246669446/246669446.jpg
     * avatar_middle : http://musicdata.baidu.com/data2/pic/74312948b16e42bfa63e59994ec98981/246669450/246669450.jpg
     * avatar_small : http://musicdata.baidu.com/data2/pic/0f626d22f80c97139dde60f9dd914723/246669481/246669481.jpg
     * avatar_mini : http://musicdata.baidu.com/data2/pic/8779f839c9e01d78d1e1cf285ebda7fc/246669482/246669482.jpg
     * albums_total : 10
     * songs_total : 85
     * artist_id : 88
     * piao_id : 0
     */

    private List<ArtistBean> artist;

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getNoFirstChar() {
        return noFirstChar;
    }

    public void setNoFirstChar(String noFirstChar) {
        this.noFirstChar = noFirstChar;
    }

    public int getHavemore() {
        return havemore;
    }

    public void setHavemore(int havemore) {
        this.havemore = havemore;
    }

    public List<ArtistBean> getArtist() {
        return artist;
    }

    public void setArtist(List<ArtistBean> artist) {
        this.artist = artist;
    }

    public static class ArtistBean {
        private String ting_uid;
        private String name;
        private String firstchar;
        private String gender;
        private String area;
        private String country;
        private String avatar_big;
        private String avatar_middle;
        private String avatar_small;
        private String avatar_mini;
        private String albums_total;
        private String songs_total;
        private String artist_id;
        private String piao_id;

        public String getTing_uid() {
            return ting_uid;
        }

        public void setTing_uid(String ting_uid) {
            this.ting_uid = ting_uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirstchar() {
            return firstchar;
        }

        public void setFirstchar(String firstchar) {
            this.firstchar = firstchar;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getAvatar_big() {
            return avatar_big;
        }

        public void setAvatar_big(String avatar_big) {
            this.avatar_big = avatar_big;
        }

        public String getAvatar_middle() {
            return avatar_middle;
        }

        public void setAvatar_middle(String avatar_middle) {
            this.avatar_middle = avatar_middle;
        }

        public String getAvatar_small() {
            return avatar_small;
        }

        public void setAvatar_small(String avatar_small) {
            this.avatar_small = avatar_small;
        }

        public String getAvatar_mini() {
            return avatar_mini;
        }

        public void setAvatar_mini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
        }

        public String getAlbums_total() {
            return albums_total;
        }

        public void setAlbums_total(String albums_total) {
            this.albums_total = albums_total;
        }

        public String getSongs_total() {
            return songs_total;
        }

        public void setSongs_total(String songs_total) {
            this.songs_total = songs_total;
        }

        public String getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(String artist_id) {
            this.artist_id = artist_id;
        }

        public String getPiao_id() {
            return piao_id;
        }

        public void setPiao_id(String piao_id) {
            this.piao_id = piao_id;
        }
    }
}
