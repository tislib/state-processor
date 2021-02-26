package net.tislib.stateprocessor.example.component;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import net.tislib.stateprocessor.base.Processor;
import net.tislib.stateprocessor.base.ResourceRegister;
import net.tislib.stateprocessor.example.data.DummyApiResource;
import net.tislib.stateprocessor.model.ResourceDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.HashMap;
import java.util.Map;

@Component
@Processor(kind = "DummyApi/v1")
@RequiredArgsConstructor
public class DummyApiResourceHandlerComponent implements ResourceRegister<DummyApiResource> {

    private final GenericApplicationContext applicationContext;

    @Override
    public void register(ResourceDefinition<DummyApiResource> resource) {
        Map<String, Object> urlMap = new HashMap<>();

        resource.getSpec().getEndpoints()
                .forEach(endpoint -> urlMap.put(endpoint.getPath(), new DummyApiComponent(endpoint)));

        applicationContext.registerBean(resource.getMetadata().getName() + "Mapping",
                SimpleUrlHandlerMapping.class,
                urlMap,
                Ordered.HIGHEST_PRECEDENCE
        );
    }

    @Override
    public TypeReference<ResourceDefinition<DummyApiResource>> getTypeReference() {
        return new TypeReference<>() {
        };
    }

}
