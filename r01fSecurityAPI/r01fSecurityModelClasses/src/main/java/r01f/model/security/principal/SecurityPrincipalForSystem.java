package r01f.model.security.principal;

import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.security.IsSecurityPrincipal;
import r01f.objectstreamer.annotations.MarshallType;

@ConvertToDirtyStateTrackable
@MarshallType(as="securityPrincipalForSystem")
@Accessors(prefix="_")
public class SecurityPrincipalForSystem 
	 extends SecurityPrincipalBase<SecurityPrincipalForSystem>
  implements IsSecurityPrincipal {

	private static final long serialVersionUID = 2722186014024195908L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getDisplayName() {
		return "[security principal] for system";
	}
}
