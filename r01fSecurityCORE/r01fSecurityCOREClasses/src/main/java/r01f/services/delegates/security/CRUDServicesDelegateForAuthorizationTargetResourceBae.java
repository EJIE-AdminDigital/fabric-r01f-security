package r01f.services.delegates.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.patterns.Memoized;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.callback.spec.COREServiceMethodCallbackSpec;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.config.DBModuleConfigBuilderForHibernate;
//import r01f.services.core.persistence.db.config.DBModuleConfigBuilderForHibernate;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class CRUDServicesDelegateForAuthorizationTargetResourceBae
			  extends CRUDServicesForModelObjectDelegateBase<AuthorizationTargetResourceOID,AuthorizationTargetResource>
		   implements CRUDServicesForAuthorizationTargetResource {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final transient Memoized<FindServicesForAuthorizationOnResource> _authOnResourceFind;
	private final transient Memoized<CRUDServicesForAuthorizationOnResource> _authOnResourceCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForAuthorizationTargetResourceBae(final ServicesCoreModuleConfig coreCfg,
																 final CRUDServicesForAuthorizationTargetResource dbCrud,
																 final EntityManager entityManager,
																 final Marshaller marshaller,
																 final EventBus eventBus) {
		super(coreCfg,
			  AuthorizationTargetResource.class,
			  dbCrud,
			  eventBus);
		_authOnResourceFind = new Memoized<FindServicesForAuthorizationOnResource>() {
									@Override
									public FindServicesForAuthorizationOnResource supply() {
										return _createFindServicesForAuthorizationOnResource(DBModuleConfigBuilderForHibernate.dbModuleConfigFrom(coreCfg),
																							 entityManager,
																							 marshaller);
									}
							  };
		_authOnResourceCrud = new Memoized<CRUDServicesForAuthorizationOnResource>() {
									@Override
									public CRUDServicesForAuthorizationOnResource supply() {
										return _createCRUDServicesForAuthorizationOnResource(DBModuleConfigBuilderForHibernate.dbModuleConfigFrom(coreCfg),
																							 entityManager,
																							 marshaller);
									}
							  };
	}
	protected abstract <FIND extends FindServicesForAuthorizationOnResource> FIND _createFindServicesForAuthorizationOnResource(final DBModuleConfig dbCfg,
																																final EntityManager entityManager,
																																final Marshaller marshaller);

	protected abstract <CRUD extends CRUDServicesForAuthorizationOnResource> CRUD _createCRUDServicesForAuthorizationOnResource(final DBModuleConfig dbCfg,
																																final EntityManager entityManager,
																																final Marshaller marshaller);
/////////////////////////////////////////////////////////////////////////////////////////
//	CASCADE DELETE [auths]
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<AuthorizationTargetResource> delete(final SecurityContext securityContext,
														  final AuthorizationTargetResourceOID oid,
														  final COREServiceMethodCallbackSpec callbackSpec) {
		CRUDResult<AuthorizationTargetResource> delete = super.delete(securityContext,
											oid,
											callbackSpec);
		if (delete.hasSucceeded()) {
			// delete all [auths] on this [auth resource]
			_cascadeDeleteAuthsOnResource(securityContext,
									      oid);
		}
		return delete;
	}
	/**
	 * Cascade-deletes all [auths] on resource
	 * @param securityContext
	 * @param authResOid
	 * @return
	 */
	protected void _cascadeDeleteAuthsOnResource(final SecurityContext securityContext,
												 final AuthorizationTargetResourceOID authResOid) {
		// [1] - Get the [auths on resource]
		FindServicesForAuthorizationOnResource authFind = _authOnResourceFind.get();
		Collection<SecurityPrincipalAuthorizationOnResource> authsOnRes = authFind.findBy(securityContext,
																			 			  authResOid)
														   		     			  .getOrThrow();
		// [2] - delete every [auth on resource]
		if (CollectionUtils.hasData(authsOnRes)) {
			CRUDServicesForAuthorizationOnResource authCrud = _authOnResourceCrud.get();
			authsOnRes.forEach(authOnRes -> {
									authCrud.delete(securityContext,
													authOnRes.getOid())
											.getOrThrow();
							   });
			log.info("...deleted {} [auths on resource] oid={}",
					 authsOnRes.size(),authResOid);
		}
	}
}
