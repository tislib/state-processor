package net.tislib.stateprocessor.example.component;

import net.tislib.stateprocessor.example.data.DummyApiResource.DummyApiResourceEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

@RequestMapping
public class DummyApiComponent2 {
    private final DummyApiResourceEndpoint endpoint;

    public DummyApiComponent2(DummyApiResourceEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    @GetMapping
    public String hello() {
        return "world: " + endpoint.getPath();
    }

}
