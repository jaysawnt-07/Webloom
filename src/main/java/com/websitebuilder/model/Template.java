package com.websitebuilder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "templates")
public class Template {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String thumbnailUrl;

    private String badge;
    private Double rating;
    private Integer downloads;

    private String primaryColor;
    private String secondaryColor;
    private String fontFamily;
    private String buttonRadius;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String contentJson;

    public Template() {
    }

    public Template(String id, String name, String category, String description, String thumbnailUrl,
                    String badge, Double rating, Integer downloads, String primaryColor,
                    String secondaryColor, String fontFamily, String buttonRadius, String contentJson) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.badge = badge;
        this.rating = rating;
        this.downloads = downloads;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.fontFamily = fontFamily;
        this.buttonRadius = buttonRadius;
        this.contentJson = contentJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getButtonRadius() {
        return buttonRadius;
    }

    public void setButtonRadius(String buttonRadius) {
        this.buttonRadius = buttonRadius;
    }

    public String getContentJson() {
        return contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }
}
