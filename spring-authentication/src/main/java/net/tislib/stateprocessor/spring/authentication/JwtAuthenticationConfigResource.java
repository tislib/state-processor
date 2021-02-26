package net.tislib.stateprocessor.spring.authentication;

import lombok.Data;
import net.tislib.stateprocessor.model.ResourceSpecification;

@Data
public class JwtAuthenticationConfigResource implements ResourceSpecification {
    private boolean enable;

    public static class TokenConfig {
        private String key;
    }

    public static class Authentication {
        private String authPageUrl;
    }
}
