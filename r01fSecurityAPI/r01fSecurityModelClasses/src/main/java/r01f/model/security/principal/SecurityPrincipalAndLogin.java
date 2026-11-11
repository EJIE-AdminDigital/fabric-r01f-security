package r01f.model.security.principal;

import lombok.Getter;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.ModelObject;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.login.Login;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;

/**
 * Combines {@link IsSecurityPrincipal} info with login info
 * @param <P>
 */
@MarshallType(as="securityPrincipalAndLogin")
@ConvertToDirtyStateTrackable
public record SecurityPrincipalAndLogin<P extends IsSecurityPrincipal>(@MarshallField(as="securityPrincipal")
														  			   @Getter P securityPrincipal,
														  			   
																	   @MarshallField(as="login")
														  			   @Getter Login login)
   implements ModelObject {

	
}
