package r01f.services.security;



import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.UserPasswordLoginServices;
import r01f.db.crud.security.DBCRUDForUserPasswordLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;
import r01f.services.delegates.security.CRUDServicesDelegateForUserPasswordLogin;
import r01f.services.delegates.security.UserPasswordLoginServicesDelegate;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForUserPasswordLogin<L extends UserPasswordLogin>
			  extends CoreCRUDServicesForModelObjectBase<LoginOID,L>
		   implements CRUDServicesForUserPasswordLogin<L>,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForUserPasswordLogin(final ServicesCoreModuleConfig coreCfg,
													final Marshaller modelObjectsMarshaller,
													final EventBus eventBus,
													final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForUserPasswordLogin.class)
					.loadBy(securityContext,
							userOid);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForUserPasswordLogin.class)
					.loadBy(securityContext,
							login);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> updatePassword(final SecurityContext securityContext,
										final LoginID login,
										final Password password) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForUserPasswordLogin.class)
						.updatePassword(securityContext,
										login,
										password);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> updatePassword(final SecurityContext securityContext,
										final LoginID login,
										final Password oldPassword,final Password newPassword) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForUserPasswordLogin.class)
						.updatePassword(securityContext,
										login,
										oldPassword,newPassword);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the deepest layer (the DAO layer)
	 * This is NOT usual BUT it's needed since the DELEGATE layer removes the PASSWORD from the returned object
	 * ... BUT the PASSWORD is needed at {@link UserPasswordLoginServices} to check the user / password login
	 *     (see {@link UserPasswordLoginServicesDelegate})
	 *     So, instead of calling SERVICE LAYER (1st layer) #load method, the deepest layer (DAO layer) method
	 *     is called
	 * @param securityContext
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DBCRUDForUserPasswordLogin<L,?> createDBCRUDFor(final SecurityContext securityContext) {
		CRUDServicesDelegateForUserPasswordLogin<L> delegate = (CRUDServicesDelegateForUserPasswordLogin<L>)this.forSecurityContext(securityContext)
																												.createDelegateAs(CRUDServicesForUserPasswordLogin.class);
		return (DBCRUDForUserPasswordLogin<L,?>)delegate.getServiceImpl();
	}
}

