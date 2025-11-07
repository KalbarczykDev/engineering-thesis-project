package dev.kalbarczyk.api.core.composite.workout;

import dev.kalbarczyk.api.core.workout.WorkoutSummary;

import java.util.List;

public interface WorkoutCompositeService {

    List<WorkoutSummary> getWorkoutsForUser(final Long userId);
}
