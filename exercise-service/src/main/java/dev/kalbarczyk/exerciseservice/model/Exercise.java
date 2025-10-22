package dev.kalbarczyk.exerciseservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "muscle_group")
    private String muscleGroup;

    @Column(name = "equipment")
    private String equipment;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    public Exercise(
            final String name,
            final String category,
            final String muscleGroup,
            final String equipment,
            final String description,
            final String imageUrl
    ) {
        setName(name);
        setCategory(category);
        setMuscleGroup(muscleGroup);
        setEquipment(equipment);
        setDescription(description);
        setImageUrl(imageUrl);
    }
}
