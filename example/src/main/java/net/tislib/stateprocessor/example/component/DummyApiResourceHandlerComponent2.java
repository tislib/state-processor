package net.tislib.stateprocessor.example.component;

import lombok.RequiredArgsConstructor;
import net.tislib.stateprocessor.base.ProvideResource;
import net.tislib.stateprocessor.model.ResourceDefinition;
import net.tislib.stateprocessor.spring.base.ProcessorComponent;
import net.tislib.stateprocessor.example.data.DummyApiResource;
import net.tislib.stateprocessor.spring.rest.ApiComponentProvider;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ProcessorComponent(kind = "DummyApi/v2", specClass = DummyApiResource.class)
@RequiredArgsConstructor
public class DummyApiResourceHandlerComponent2 implements ProvideResource<DummyApiResource>, ApiComponentProvider {

    private final GenericApplicationContext applicationContext;

    @Override
    public void setResource(ResourceDefinition<DummyApiResource> resource) {
        Set<String> registeredControllers = new HashSet<>();

        for (DummyApiResource.DummyApiResourceEndpoint endpoint : resource.getSpec().getEndpoints()) {
            String controllerName = resource.getMetadata().getName() + "[" + endpoint.getPath() + "]";

            registeredControllers.add(controllerName);

            applicationContext.registerBean(controllerName,
                    DummyApiComponent2.class,
                    resource.getSpec().getEndpoints().get(0));
        }

        applicationContext.registerBean(resource.getMetadata().getName() + "Mapping",
                DynamicXRequestMappingHandlerMapping.class,
                resource,
                registeredControllers,
                Ordered.HIGHEST_PRECEDENCE
        );
    }

    @RequiredArgsConstructor
    public static class DynamicXRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        private final ResourceDefinition<DummyApiResource> resource;
        private final Set<String> registeredControllers;

        @Override
        protected String[] getCandidateBeanNames() {
            return registeredControllers.toArray(new String[0]);
        }
    }

}
