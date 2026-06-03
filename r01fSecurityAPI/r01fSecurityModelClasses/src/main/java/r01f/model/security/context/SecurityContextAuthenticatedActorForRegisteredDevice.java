package r01f.model.security.context;

import lombok.experimental.Accessors;
import r01f.guids.OID;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;

@MarshallType(as="authenticatedActorForRegisteredDevice")
@Accessors(prefix="_")
public class SecurityContextAuthenticatedActorForRegisteredDevice<O extends OID> 
     extends SecurityContextAuthenticatedActorBase {

	private static final long serialVersionUID = 2358174130244125302L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @see SecurityContextAuthenticatedActorBuilder
	 * @param deviceOidType
	 * @param deviceOid
	 */
	public SecurityContextAuthenticatedActorForRegisteredDevice(final O deviceOid) {
		super(SecurityProviderID.REGISTERED_DEVICE,LoginID.forId(deviceOid.asString()));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public O getDeviceOidUsing(final FactoryFrom<LoginID,O> deviceOidFactory) {
		return deviceOidFactory.from(_loginId);
	}
}
