package net.tislib.stateprocessor.example.component;

import net.tislib.stateprocessor.example.data.DummyApiResource;
import net.tislib.stateprocessor.example.data.DummyApiResource.DummyApiResourceEndpoint;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class DummyApiComponent implements Controller {
    private final DummyApiResourceEndpoint endpoint;

    public DummyApiComponent(DummyApiResourceEndpoint endpoint) {
        this.endpoint = endpoint;
        System.out.println("constructor called");
    }

    @Override
    public ModelAndView handleRequest(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {
        System.out.println("request handled for" + endpoint.getPath());
        return null;
    }
}
