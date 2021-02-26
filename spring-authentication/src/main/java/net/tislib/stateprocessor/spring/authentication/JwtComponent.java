package net.tislib.stateprocessor.spring.authentication;

import lombok.RequiredArgsConstructor;
import net.tislib.stateprocessor.base.ProvideResource;
import net.tislib.stateprocessor.model.ResourceDefinition;
import net.tislib.stateprocessor.spring.base.ProcessorComponent;
import net.tislib.stateprocessor.spring.rest.ApiComponentProvider;

@ProcessorComponent(kind = "spring/authentication/jwt/v1",
        specClass = JwtAuthenticationConfigResource.class)
@RequiredArgsConstructor
public class JwtComponent implements ProvideResource<JwtAuthenticationConfigResource>, ApiComponentProvider {

    @Override
    public void setResource(ResourceDefinition<JwtAuthenticationConfigResource> resourceDefinition) {
        System.out.println("configuring jwt resource");
    }
}
