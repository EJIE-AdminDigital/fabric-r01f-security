package r01f.services.delegates.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.CRUDServicesForUserAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.FindServicesForUserAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Memoized;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.callback.spec.COREServiceMethodCallbackSpec;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.config.DBModuleConfigBuilderForEclipseLink;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class CRUDServicesDelegateForAuthorizationTargetResource<R extends AuthorizationTargetResource>
			  extends CRUDServicesForModelObjectDelegateBase<AuthorizationTargetResourceOID,R>
		   implements CRUDServicesForAuthorizationTargetResource<R> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final transient Memoized<FindServicesForUserAuthorizationOnResource<?>> _userAuthOnResourceFind;
	private final transient Memoized<CRUDServicesForUserAuthorizationOnResource<?>> _userAuthOnResourceCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForAuthorizationTargetResource(final ServicesCoreModuleConfig coreCfg,
															  final Class<R> modelObjectType,
															  final CRUDServicesForAuthorizationTargetResource<R> dbCrud,
															  final EntityManager entityManager,
															  final Marshaller marshaller,
															  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
		_userAuthOnResourceFind = new Memoized<FindServicesForUserAuthorizationOnResource<?>>() {
											@Override
											public FindServicesForUserAuthorizationOnResource<?> supply() {
												return _createFindServicesForUserAuthorizationOnResource(DBModuleConfigBuilderForEclipseLink.dbModuleConfigFrom(coreCfg),
																										 entityManager,
																										 marshaller);
											}
								  };
		_userAuthOnResourceCrud = new Memoized<CRUDServicesForUserAuthorizationOnResource<?>>() {
											@Override
											public CRUDServicesForUserAuthorizationOnResource<?> supply() {
												return _createCRUDServicesForUserAuthorizationOnResource(DBModuleConfigBuilderForEclipseLink.dbModuleConfigFrom(coreCfg),
																										 entityManager,
																										 marshaller);
											}
								  };
	}
	protected abstract <A extends UserAuthorizationOnResource,
			   		    FIND extends FindServicesForUserAuthorizationOnResource<A>> FIND _createFindServicesForUserAuthorizationOnResource(final DBModuleConfig dbCfg,
																																		   final EntityManager entityManager,
																																		   final Marshaller marshaller);

	protected abstract <A extends UserAuthorizationOnResource,
					    CRUD extends CRUDServicesForUserAuthorizationOnResource<A>> CRUD _createCRUDServicesForUserAuthorizationOnResource(final DBModuleConfig dbCfg,
																																		   final EntityManager entityManager,
																																		   final Marshaller marshaller);
/////////////////////////////////////////////////////////////////////////////////////////
//	CASCADE DELETE [user auths]
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<R> delete(final SecurityContext securityContext,
								final AuthorizationTargetResourceOID oid,
								final COREServiceMethodCallbackSpec callbackSpec) {
		CRUDResult<R> delete = super.delete(securityContext,
											oid,
											callbackSpec);
		if (delete.hasSucceeded()) {
			// delete all [user auths] on this [auth resource]
			_cascadeDeleteUserAuthsOnResource(securityContext,
											  oid);
		}
		return delete;
	}
	/**
	 * Cascade-deletes all [user auths] on resource
	 * @param securityContext
	 * @param authResOid
	 * @return
	 */
	protected void _cascadeDeleteUserAuthsOnResource(final SecurityContext securityContext,
												     final AuthorizationTargetResourceOID authResOid) {
		// [1] - Get the [user auths on resource]
		FindServicesForUserAuthorizationOnResource<?> userAuthFind = _userAuthOnResourceFind.get();
		Collection<? extends UserAuthorizationOnResource> userAuthsOnRes = userAuthFind.findBy(securityContext,
																							   authResOid)
																					   .getOrThrow();
		// [2] - delete every [user auth on resource]
		if (CollectionUtils.hasData(userAuthsOnRes)) {
			CRUDServicesForUserAuthorizationOnResource<?> userAuthCrud = _userAuthOnResourceCrud.get();
			userAuthsOnRes.forEach(userAuthOnRes -> {
										userAuthCrud.delete(securityContext,
															userAuthOnRes.getOid())
													.getOrThrow();
								   });
			log.info("...deleted {} [user auths on resource] oid={}",
					 userAuthsOnRes.size(),authResOid);
		}
	}
}
