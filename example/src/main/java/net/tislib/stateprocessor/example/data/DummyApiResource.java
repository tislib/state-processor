package net.tislib.stateprocessor.example.data;

import lombok.Data;
import net.tislib.stateprocessor.model.ResourceSpecification;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class DummyApiResource implements ResourceSpecification {

    @NotNull
    private List<DummyApiResourceEndpoint> endpoints;

    @Data
    public static class DummyApiResourceEndpoint {
        @NotBlank
        private String path;
        private String body;
        private Map<String, String> headers;
        private int status = 200;
    }
}
