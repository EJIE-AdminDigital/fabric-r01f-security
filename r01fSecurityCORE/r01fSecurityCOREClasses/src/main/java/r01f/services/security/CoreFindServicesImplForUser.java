package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.user.FindServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForUser<U extends User>
			  extends CoreFindServicesForModelObjectBase<UserOID,U>
		   implements FindServicesForUser<U>,
			 		  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForUser(final ServicesCoreModuleConfig coreCfg,
									   final Marshaller modelObjectsMarshaller,
									   final EventBus eventBus,
									   final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES EXTENSION
// 	IMPORTANT!!! Do NOT put any logic in these methods ONLY DELEGATE!!!
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<U> findAllUsers(final SecurityContext securityContext) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForUser.class)
				   .findAllUsers(securityContext);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<U> findUsersWithSystemWideRole(final SecurityContext securityContext) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForUser.class)
				   .findUsersWithSystemWideRole(securityContext);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<U> findByText(final SecurityContext securityContext,
									final String text) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForUser.class)
				   .findByText(securityContext,
						   	   text);
	}
}
