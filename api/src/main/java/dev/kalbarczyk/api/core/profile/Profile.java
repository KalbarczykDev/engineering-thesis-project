package dev.kalbarczyk.api.core.profile;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Profile {
    private int userId;
    private String displayName;
    private String avatarUrl;
    private String bio;
    private String location;
}
