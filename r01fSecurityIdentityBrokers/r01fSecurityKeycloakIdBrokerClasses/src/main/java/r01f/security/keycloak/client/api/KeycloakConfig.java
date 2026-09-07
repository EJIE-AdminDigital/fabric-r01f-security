package r01f.security.keycloak.client.api;

import jakarta.inject.Named;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Named("keycloakConfig")
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface KeycloakConfig {
	//
}