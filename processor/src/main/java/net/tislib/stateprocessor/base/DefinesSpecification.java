package net.tislib.stateprocessor.base;

import net.tislib.stateprocessor.model.ResourceSpecification;

public interface DefinesSpecification<T extends ResourceSpecification> {

    Class<T> getSpecificationClass();

}
