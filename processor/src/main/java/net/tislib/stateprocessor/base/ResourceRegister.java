package net.tislib.stateprocessor.base;

import com.fasterxml.jackson.core.type.TypeReference;
import net.tislib.stateprocessor.model.ResourceDefinition;
import net.tislib.stateprocessor.model.ResourceSpecification;

public interface ResourceRegister<T extends ResourceSpecification> {

    void register(ResourceDefinition<T> resource);

    TypeReference<ResourceDefinition<T>> getTypeReference();
}
