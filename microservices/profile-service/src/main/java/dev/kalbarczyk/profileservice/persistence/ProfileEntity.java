package dev.kalbarczyk.profileservice.persistence;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import static java.lang.String.format;

@Document(collection = "profiles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ProfileEntity {

    @Id
    private Long userId;

    @Version
    @EqualsAndHashCode.Exclude
    private Long version;

    private String displayName;


    private String bio;

    private String location;

    @CreatedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;

    @LastModifiedDate
    @EqualsAndHashCode.Exclude
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return format("ProfileEntity: %s", userId);
    }

}
