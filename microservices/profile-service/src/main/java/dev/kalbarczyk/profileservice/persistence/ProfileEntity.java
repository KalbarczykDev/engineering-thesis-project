package dev.kalbarczyk.profileservice.persistence;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.String.format;

@Document(collection = "profiles")
public class ProfileEntity {

    public ProfileEntity() {
    }

    public ProfileEntity(Long userId, Long version, String displayName, String bio, String location, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.version = version;
        this.displayName = displayName;
        this.bio = bio;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Id
    private Long userId;

    @Version
    private Long version;

    private String displayName;


    private String bio;

    private String location;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProfileEntity that = (ProfileEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(version, that.version) && Objects.equals(displayName, that.displayName) && Objects.equals(bio, that.bio) && Objects.equals(location, that.location) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, version, displayName, bio, location, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return format("ProfileEntity: %s", userId);
    }

}
