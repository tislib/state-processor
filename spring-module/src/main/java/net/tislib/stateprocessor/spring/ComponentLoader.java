package net.tislib.stateprocessor.spring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import net.tislib.stateprocessor.base.Processor;
import net.tislib.stateprocessor.base.ProvideResource;
import net.tislib.stateprocessor.base.ResourceRegister;
import net.tislib.stateprocessor.model.Metadata;
import net.tislib.stateprocessor.model.ResourceDefinition;
import net.tislib.stateprocessor.model.ResourceSpecification;
import net.tislib.stateprocessor.spring.base.ProcessorComponent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentLoader {

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    private final GenericApplicationContext context;
    private final Map<String, Class<?>> kindMap;
    private final Map<String, Object> kindBeanMap;
    private final Set<String> loadedResources = new HashSet<>();

    public ComponentLoader(GenericApplicationContext context) {
        this.context = context;

        Map<String, Object> componentProcessors = context.getBeansWithAnnotation(ProcessorComponent.class);
        Map<String, Object> processors = context.getBeansWithAnnotation(Processor.class);

        kindMap = new HashMap<>();
        kindBeanMap = new HashMap<>();

        kindMap.putAll(componentProcessors.values().stream().collect(Collectors.toMap(item -> {
            ProcessorComponent processor = item.getClass().getAnnotation(ProcessorComponent.class);
            return processor.kind();
        }, Object::getClass)));

        kindMap.putAll(processors.values().stream().collect(Collectors.toMap(item -> {
            Processor processor = item.getClass().getAnnotation(Processor.class);
            return processor.kind();
        }, Object::getClass)));

        kindBeanMap.putAll(componentProcessors.values().stream().collect(Collectors.toMap(item -> {
            ProcessorComponent processor = item.getClass().getAnnotation(ProcessorComponent.class);
            return processor.kind();
        }, item -> item)));

        kindBeanMap.putAll(processors.values().stream().collect(Collectors.toMap(item -> {
            Processor processor = item.getClass().getAnnotation(Processor.class);
            return processor.kind();
        }, item -> item)));

    }

    public void load(Path resourceContent) {
        try {
            JsonNode jsonNode = mapper.readTree(resourceContent.toUri().toURL());

            String kind = jsonNode.get("kind").asText();

            if (kindMap.containsKey(kind)) {
                Object beanInstance = kindBeanMap.get(kind);
                loadResource(jsonNode, kindMap.get(kind), beanInstance);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadResource(JsonNode jsonNode, Class<?> beanClass, Object beanInstance) {
        // locate annotations
        ProcessorComponent processorComponent = beanClass.getAnnotation(ProcessorComponent.class);

        // check generic bean processor configuration
        if (processorComponent != null) {
            loadProcessorComponent(jsonNode, beanClass, processorComponent);
        } else {
            loadProcessor(jsonNode, beanInstance);
        }

    }

    private void loadProcessor(JsonNode jsonNode, Object beanInstance) {
        if (beanInstance instanceof ResourceRegister<?>) {
            ResourceRegister<?> register = (ResourceRegister<?>) beanInstance;

            register(jsonNode, register);
        } else {
            throw new UnsupportedOperationException("processor bean is not implementing resource registration logic");
        }
    }

    @SneakyThrows
    private <T extends ResourceSpecification> void register(JsonNode jsonNode, ResourceRegister<T> register) {
        TypeReference<ResourceDefinition<T>> typeReference = register.getTypeReference();

        ResourceDefinition<T> resourceDefinition = mapper.readValue(jsonNode.toString(), typeReference);

        if (!StringUtils.hasText(resourceDefinition.getMetadata().getName())) {
            throw new IllegalStateException("metadata -> name should not be null");
        }

        registerName(resourceDefinition.getMetadata());

        register.register(resourceDefinition);
    }

    @SneakyThrows
    private void loadProcessorComponent(JsonNode jsonNode, Class<?> beanClass, ProcessorComponent processorComponent) {
        ResourceDefinition<ResourceSpecification> resourceDefinition = new ResourceDefinition<>();
        resourceDefinition.setKind(jsonNode.get("kind").toString());
        resourceDefinition.setMetadata(mapper.readValue(jsonNode.get("metadata").toString(), Metadata.class));

        if (!StringUtils.hasText(resourceDefinition.getMetadata().getName())) {
            throw new IllegalStateException("metadata -> name should not be null");
        }

        registerName(resourceDefinition.getMetadata());

        resourceDefinition.setSpec(mapper.readValue(jsonNode.get("spec").toString(), processorComponent.specClass()));

        String beanName = resourceDefinition.getKind() + "/" + resourceDefinition.getMetadata().getName();
        context.registerBean(beanName, beanClass);

        Object beanInstance = context.getBean(beanName);

        if (beanInstance instanceof ProvideResource) {
            ProvideResource provideResource = (ProvideResource) beanInstance;

            provideResource.setResource(resourceDefinition);
        }
    }

    private void registerName(Metadata metadata) {
        if (loadedResources.contains(metadata.getName())) {
            throw new IllegalStateException("duplicate resource definition: " + metadata.getName());
        }

        loadedResources.add(metadata.getName());
    }
}
