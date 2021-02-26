package net.tislib.stateprocessor.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("net.tislib.stateprocessor.spring")
@RequiredArgsConstructor
public class ProcessorConfiguration implements InitializingBean {

    private final ConfigLoader configLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
        configLoader.load();
    }
}
