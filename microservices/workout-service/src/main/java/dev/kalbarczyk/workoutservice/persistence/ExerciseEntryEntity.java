package dev.kalbarczyk.workoutservice.persistence;

import java.util.List;

public class ExerciseEntryEntity {
    private ExerciseEntity exercise;
    private List<SeriesEntity> series;

    public ExerciseEntity getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseEntity exercise) {
        this.exercise = exercise;
    }

    public List<SeriesEntity> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesEntity> series) {
        this.series = series;
    }
}
