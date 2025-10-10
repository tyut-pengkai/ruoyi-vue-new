package com.common.utils;


/**
 * @author TRS
 * @date 2023-12-22
 */
public enum HybaseMediaType {

    News("新闻", "1"),
    Forum("论坛", "2"),
    Blog("博客", "3"),
    Weibo("微博", "4"),
    Client("客户端", "5"),
    WeiXin("微信", "6"),
    Newsletter("电子报", "7"),
    Abroad("境外", "8"),
    Video("视频网站", "9"),
    WeMedia("自媒体", "10"),
    ShortVideo("短视频", "11"),
    Facebook("facebook", "12"),
    Twitter("twitter", "13"),
    Vk("vk", "14"),
    Youtube("youtube", "15"),
    Instagram("instagram", "16"),
    Linked("linked", "17");


    // 媒体
    private String media;
    // 媒体值
    private String value;

    HybaseMediaType(String media, String number) {
        this.media = media;
        this.value = number;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 通过媒体值查媒体名
     *
     * @param value
     * @return
     */
    public static String getMediaName(String value) {
        for (HybaseMediaType c : HybaseMediaType.values()) {
            if (c.getValue().equals(value)) {
                return c.media;
            }
        }
        return News.media;
    }

    /**
     * 通过媒体查媒体值
     *
     * @param name
     * @return
     */
    public static String getMediaValue(String name) {
        for (HybaseMediaType mediaType : HybaseMediaType.values()) {
            if (mediaType.getMedia().equals(name)) {
                return mediaType.value;
            }
        }
        return News.value;
    }
}

