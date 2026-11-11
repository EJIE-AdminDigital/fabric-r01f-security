package r01f.client.api.security;

import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityPrincipal;

@Accessors(prefix="_")
public interface  SecurityAPI<U extends IsSecurityPrincipal> {
	// nothing
}
