package com.example.kevin.baidumusic.musiclibrary.mv;

/**
 * Created by kevin on 16/6/2.
 */
public class MvPlayBean {

    /**
     * error_code : 22000
     * result : {"video_info":{"video_id":"266020577","mv_id":"249468281","provider":"12","sourcepath":"http://www.yinyuetai.com/video/2584758","thumbnail":"http://qukufile2.qianqian.com/data2/pic/aeb84b960313312798e26d5441704133/266058621/266058621.jpg","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/758c292781bd426c58953c1935bf3a2c/266058601/266058601.jpg","del_status":"0","distribution":"0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000"},"files":{"31":{"video_file_id":"266020579","video_id":"266020577","definition":"31","file_link":"http://www.yinyuetai.com/mv/video-url/2584758","file_format":"","file_extension":"mp4","file_duration":"199","file_size":"0","source_path":"http://www.yinyuetai.com/mv/video-url/2584758"}},"min_definition":"31","max_definition":"31","mv_info":{"mv_id":"249468281","all_artist_id":"18594136","title":"AhYeah","aliastitle":"","subtitle":"中文字幕","play_nums":"0","publishtime":"2015-09-29","del_status":"0","artist_id":"18594136","thumbnail":"http://qukufile2.qianqian.com/data2/pic/aeb84b960313312798e26d5441704133/266058621/266058621.jpg","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/758c292781bd426c58953c1935bf3a2c/266058601/266058601.jpg","artist":"EXID","provider":"12"}}
     */

    private int error_code;
    /**
     * video_info : {"video_id":"266020577","mv_id":"249468281","provider":"12","sourcepath":"http://www.yinyuetai.com/video/2584758","thumbnail":"http://qukufile2.qianqian.com/data2/pic/aeb84b960313312798e26d5441704133/266058621/266058621.jpg","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/758c292781bd426c58953c1935bf3a2c/266058601/266058601.jpg","del_status":"0","distribution":"0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000"}
     * files : {"31":{"video_file_id":"266020579","video_id":"266020577","definition":"31","file_link":"http://www.yinyuetai.com/mv/video-url/2584758","file_format":"","file_extension":"mp4","file_duration":"199","file_size":"0","source_path":"http://www.yinyuetai.com/mv/video-url/2584758"}}
     * min_definition : 31
     * max_definition : 31
     * mv_info : {"mv_id":"249468281","all_artist_id":"18594136","title":"AhYeah","aliastitle":"","subtitle":"中文字幕","play_nums":"0","publishtime":"2015-09-29","del_status":"0","artist_id":"18594136","thumbnail":"http://qukufile2.qianqian.com/data2/pic/aeb84b960313312798e26d5441704133/266058621/266058621.jpg","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/758c292781bd426c58953c1935bf3a2c/266058601/266058601.jpg","artist":"EXID","provider":"12"}
     */

    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * video_id : 266020577
         * mv_id : 249468281
         * provider : 12
         * sourcepath : http://www.yinyuetai.com/video/2584758
         * thumbnail : http://qukufile2.qianqian.com/data2/pic/aeb84b960313312798e26d5441704133/266058621/266058621.jpg
         * thumbnail2 : http://qukufile2.qianqian.com/data2/pic/758c292781bd426c58953c1935bf3a2c/266058601/266058601.jpg
         * del_status : 0
         * distribution : 0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000
         */

        private VideoInfoBean video_info;

        public VideoInfoBean getVideo_info() {
            return video_info;
        }

        public void setVideo_info(VideoInfoBean video_info) {
            this.video_info = video_info;
        }

        public static class VideoInfoBean {
            private String video_id;
            private String mv_id;
            private String provider;
            private String sourcepath;
            private String thumbnail;
            private String thumbnail2;
            private String del_status;
            private String distribution;

            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
            }

            public String getMv_id() {
                return mv_id;
            }

            public void setMv_id(String mv_id) {
                this.mv_id = mv_id;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            public String getSourcepath() {
                return sourcepath;
            }

            public void setSourcepath(String sourcepath) {
                this.sourcepath = sourcepath;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getThumbnail2() {
                return thumbnail2;
            }

            public void setThumbnail2(String thumbnail2) {
                this.thumbnail2 = thumbnail2;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public String getDistribution() {
                return distribution;
            }

            public void setDistribution(String distribution) {
                this.distribution = distribution;
            }
        }
    }
}
