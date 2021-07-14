package com.springboot.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

/**
 * @author Zhouzf
 * @date 2021/7/12/012 16:00
 */
public class Blog {
    private Integer id;
    @JsonIgnore
    private Integer userId;
    private String title;
    private String description;
    private String content;
    private Instant updatedAt;
    private Instant createdAt;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Blog(Integer id, Integer userId, User user) {
        this.id = id;
        this.userId = userId;
        this.user = user;
    }

    public Blog(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }


}
