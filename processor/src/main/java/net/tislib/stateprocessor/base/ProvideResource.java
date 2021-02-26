package net.tislib.stateprocessor.base;

import net.tislib.stateprocessor.model.ResourceDefinition;
import net.tislib.stateprocessor.model.ResourceSpecification;

public interface ProvideResource<T extends ResourceSpecification> {

     void setResource(ResourceDefinition<T> resourceDefinition);

}
