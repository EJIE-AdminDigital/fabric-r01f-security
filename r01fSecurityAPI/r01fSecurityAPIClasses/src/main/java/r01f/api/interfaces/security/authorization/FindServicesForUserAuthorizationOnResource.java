package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
		 extends FindServicesForModelObject<UserAuthorizationOnResourceOID,A>,
				 SecurityServiceInterface {

/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENDED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns all authorizations for a given user
	 * @param securityContext the user auth data & context info
	 * @param userOid
	 * @return  
	 */
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid);
	/**
	 * Returns all authorizations for a given user
	 * @param securityContext the user auth data & context info
	 * @param userOid
	 * @param appCode
	 * @param appComp
	 * @return  
	 */
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AppCode appCode,final AppComponent appComp);
	/**
	 * Returns all authorizations for a given target
	 * @param securityContext the user auth data & context info
	 * @param authResOid
	 * @return  
	 */
	public FindResult<A> findBy(final SecurityContext securityContext,
								final AuthorizationTargetResourceOID authResOid);
	/**
	 * Returns all authorizations for a given user on a given target
	 * @param securityContext the user auth data & context info
	 * @param userOid
	 * @param resOid
	 * @return  
	 */
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid,final AuthorizationTargetResourceOID resOid);
	
}
