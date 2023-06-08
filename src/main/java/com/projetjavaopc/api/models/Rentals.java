package com.projetjavaopc.api.models;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "rentals")
public class Rentals {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surface", nullable = false)
    private Double surface;
    
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "picture", nullable = false)
    private String picture;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long owner_id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }
    
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getOwnerId() {
        return owner_id;
    }

    public void setOwnerId(Long owner_id) {
        this.owner_id = owner_id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}