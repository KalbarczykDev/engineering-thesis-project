package dev.kalbarczyk.api.core.profile;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
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
}
