package net.tislib.stateprocessor.spring;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class ConfigLoader {

    @Value("${stateprocessor.configdir}")
    private String stateProcessorConfigDir;

    private final GenericApplicationContext applicationContext;

    @SneakyThrows
    public void load() {
        ComponentLoader componentLoader = new ComponentLoader(applicationContext);

        Files.walk(Paths.get(stateProcessorConfigDir))
                .filter(Files::isRegularFile)
                .filter(item -> item.toString().endsWith(".yml") || item.toString().endsWith(".yaml"))
                .forEach(componentLoader::load);
    }

}
