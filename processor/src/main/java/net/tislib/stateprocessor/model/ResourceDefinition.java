package net.tislib.stateprocessor.model;

import lombok.Data;

@Data
public class ResourceDefinition<T extends ResourceSpecification> {
    private String kind;
    private Metadata metadata;

    private T spec;
}
