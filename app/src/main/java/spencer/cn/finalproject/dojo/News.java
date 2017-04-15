package spencer.cn.finalproject.dojo;

import java.io.Serializable;

public class News implements Serializable{
    private String title;
    private String date;
    private String category;
    private String author_name;
    private String thumbnail_pic_s;
    private String uniquekey;
    private String url;
    private String thumbnail_pic_s03;

    public News() {
    }

    public News(String title, String date, String category, String author_name, String thumbnail_pic_s, String uniquekey, String url, String thumbnail_pic_s03) {
        this.title = title;
        this.date = date;
        this.category = category;
        this.author_name = author_name;
        this.thumbnail_pic_s = thumbnail_pic_s;
        this.uniquekey = uniquekey;
        this.url = url;
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail_pic_s03() {
        return thumbnail_pic_s03;
    }

    public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }
}
