package dev.kalbarczyk.usercompositeservice.services.tracing;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.info.BuildProperties;

public class BuildInfoObservationFilter implements ObservationFilter {

    private final BuildProperties buildProperties;

    public BuildInfoObservationFilter(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @NotNull
    @Override
    public Observation.Context map(final Observation.Context context) {
        KeyValue buildVersion = KeyValue.of("build.version", buildProperties.getVersion());
        return context.addLowCardinalityKeyValue(buildVersion);
    }
}
