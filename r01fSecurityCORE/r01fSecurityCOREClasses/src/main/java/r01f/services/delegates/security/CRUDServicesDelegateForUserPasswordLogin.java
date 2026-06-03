package r01f.services.delegates.security;


import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.callback.spec.COREServiceMethodCallbackSpec;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;

public abstract class CRUDServicesDelegateForUserPasswordLogin<L extends UserPasswordLogin>
			  extends CRUDServicesForModelObjectDelegateBase<LoginOID,L>
		   implements CRUDServicesForUserPasswordLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForUserPasswordLogin(final ServicesCoreModuleConfig coreCfg,
												 	final Class<L> modelObjectType,
												 	final CRUDServicesForUserPasswordLogin<L> dbCrud,
												 	final Marshaller marshaller,
												 	final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	OVERRIDDEN
///////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> load(final SecurityContext securityContext, 
							  final LoginOID oid) {
		CRUDResult<L> loadResult = super.load(securityContext, 
											  oid);
		if (loadResult.hasSucceeded()) _removePasswordInfoFrom(loadResult.getOrThrow());	// ensure no password is sent to the upper layers!!!
		return loadResult;
	}
	
	@Override
	protected CRUDResult<L> doUpdateOrCreate(final SecurityContext securityContext,
											 final L modelObj,
											 final PersistenceRequestedOperation requestedOperation,
											 final COREServiceMethodCallbackSpec callbackSpec) {
		CRUDResult<L> outResult = null;
		if (requestedOperation == PersistenceRequestedOperation.CREATE) {
			// downstream the DAO layer, the password MUST be hashed
			modelObj.setPasswordHash(modelObj.getPassword().hash());	// ... what's saved is the hash
			modelObj.setPassword(null);									// the password is NEVER saved
			CRUDResult<L> createResult = super.doUpdateOrCreate(securityContext,
															    modelObj, 
															    requestedOperation,
															    callbackSpec);
			if (createResult.hasSucceeded()) _removePasswordInfoFrom(createResult.getOrThrow());	// ensure no password is sent to the upper layers!!!
			outResult = createResult;
		} 
		else if (requestedOperation == PersistenceRequestedOperation.UPDATE) {
			// The password is NOT stored either at a DB table column, neither at the descriptor
			// ... the ONLY DB-stored 'thing' is the password hash at a DB table column
			// ... and it CANNOT be updated if any other data is updated (to update the password use the #updatePassword() method)
			// so:
			// [1] - Load the already stored login (to get the hash)
			L entityToUpdate = super.load(securityContext,
										  modelObj.getOid()).getOrThrow();
			
			// [2] - Set the password hash to the [model object] that contains the data to be updated
			modelObj.setPasswordHash(entityToUpdate.getPasswordHash());
			modelObj.setPassword(null);		// BEWARE!!
			
			// [3] - Usual update
			CRUDResult<L> updateResult = super.doUpdateOrCreate(securityContext,
															    modelObj, 
															    requestedOperation,
															    callbackSpec);
			// [4] - Ensure NO password hash is returned
			if (updateResult.hasSucceeded()) _removePasswordInfoFrom(updateResult.getOrThrow());	// ensure no password is sent to the upper layers!!!	
			outResult = updateResult;
		} else {
			throw new IllegalArgumentException(requestedOperation + " is NOT suitable for doUpdateOrCreate() method!");
		}
		return outResult;
	}
///////////////////////////////////////////////////////////////////////////////////////
// 	EXTENSION
///////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext, 
							  	final UserOID oid) {
		if (oid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The userOid MUST not be null")
									.build();
		}
		@SuppressWarnings("unchecked")
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForUserPasswordLogin.class)
												.loadBy(securityContext,
														oid);
		if (outResult.hasSucceeded()) _removePasswordInfoFrom(outResult.getOrThrow());	// ensure no password is sent to the upper layers!!!	
		return outResult;
	}
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login) {
		if (login == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The login MUST not be null")
									.build();
		}
		@SuppressWarnings("unchecked")
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForUserPasswordLogin.class)
												.loadBy(securityContext,
														login);
		if (outResult.hasSucceeded()) _removePasswordInfoFrom(outResult.getOrThrow());	// ensure no password is sent to the upper layers!!!	
		return outResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings({ "unchecked" })
	public CRUDResult<L> updatePassword(final SecurityContext securityContext,
										final LoginID loginId,
										final Password password) {
		if (loginId == null 
		 || password == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notUpdated()
									.becauseClientBadRequest("The user loginId and password MUST not be null")
									.build();
		}
		// Load the login (this layer's loadBy() method DOES NOT returns neither the password hash, either the password)
		L entityToUpdate = this.loadBy(securityContext,			
						   	  		   loginId)
							   .getOrThrow();
		// cannot call this.update() since it DOES NOT updates the password
		// ... the downstream DAO layer must be used
		entityToUpdate.setPasswordHash(password.hash());
		entityToUpdate.setPassword(null);
		CRUDResult<L> updateResult = this.getServiceImplAs(CRUDServicesForUserPasswordLogin.class)		// DO NOT call this.update since it DOES NOT updates the password!!!
										 .update(securityContext, 										// call the lower layer (DAO layer)	
												  entityToUpdate);
		if (updateResult.hasSucceeded()) _removePasswordInfoFrom(updateResult.getOrThrow());	// ensure no password is sent to the upper layers!!!	
		return updateResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> updatePassword(final SecurityContext securityContext,
										final LoginID loginId,
										final Password oldPassword,final Password newPassword) {
		if (loginId == null 
		 || oldPassword == null
		 || newPassword == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The user loginId and password MUST not be null")
									.build();
		}
		// [1] - Check the old password
		// Load the login
		// BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//				the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		CRUDResult<L> loginLoad = this.getServiceImplAs(CRUDServicesForUserPasswordLogin.class)	// DO NOT call this.load since it removes the password hash
									  .loadBy(securityContext,
											  loginId);
		L login = loginLoad.getOrThrow();
		if (!login.getPasswordHash()		// the stored login only contains the hash!
				  .matches(oldPassword)) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notUpdated()
									.becauseClientBadRequest("The old password DOES NOT MATCH the stored hash!!")
									.build();
		}
		// [2] - Update the password
		return this.updatePassword(securityContext,
								   loginId,
								   newPassword);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private void _removePasswordInfoFrom(final L login) {
		login.setPassword(null);
		login.setPasswordHash(null);
	}
}
