package dev.kalbarczyk.api.core.profile;


public class Profile {
    private int userId;
    private String displayName;
    private String avatarUrl;
    private String bio;
    private String location;

    public Profile() {
    }

    public Profile(int userId, String displayName, String avatarUrl, String bio, String location) {
        this.userId = userId;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.location = location;
    }

    public int getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }
}
