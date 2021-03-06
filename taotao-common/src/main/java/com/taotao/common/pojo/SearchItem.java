package com.taotao.common.pojo;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

public class SearchItem  implements Serializable{
    @Field
    private String id;
    @Field("item_title")
    private String title;
    @Field("item_sell_point")
    private String sell_point;
    @Field("item_price")
    private long price;
    @Field("item_image")
    private String image;
    @Field("item_category_name")
    private String category_name;
    @Field("item_desc")
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", sell_point='" + sell_point + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", category_name='" + category_name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
