package r01f.services.delegates.security;

import r01f.api.interfaces.security.user.FindServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

/**
 * Service layer delegated type for CRUD (Create/Read/Update/Delete) operations
 */
public abstract class FindServicesDelegateForUser<U extends User>
			  extends FindServicesForModelObjectDelegateBase<UserOID,U>
		   implements FindServicesForUser<U> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForUser(final ServicesCoreModuleConfig coreCfg,
									   final Class<U> modelObjectType,
									   final FindServicesForUser<U> dbFind,
									   final Marshaller marshaller,
									   final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbFind,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<U> findAllUsers(final SecurityContext securityContext) {
		return this.getServiceImplAs(FindServicesForUser.class)
					.findAllUsers(securityContext);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<U> findUsersWithSystemWideRole(final SecurityContext securityContext) {
		return this.getServiceImplAs(FindServicesForUser.class)
					.findUsersWithSystemWideRole(securityContext);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<U> findByText(final SecurityContext securityContext,
									final String text) {
		return this.getServiceImplAs(FindServicesForUser.class)
				   .findByText(securityContext,
						   	   text);
	}
}
