package r01f.services.security.otp.persistence.base.hz;

import java.time.Instant;


import lombok.experimental.Accessors;
import r01f.cache.DistributedCacheService;
import r01f.eventbus.EventBus;
import r01f.model.PersistableModelObject;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceOperationResult;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.PersistableObjectOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.callback.spec.COREServiceMethodCallbackSpec;
import r01f.services.interfaces.CRUDServicesForModelObject;


@Accessors(prefix="_")
public abstract class CoreCRUDServicesForModelObjectBase<O extends PersistableObjectOID,M extends PersistableModelObject<O>>
			  extends CoreHazelcastPersistenceServicesBase
		   implements CRUDServicesForModelObject<O,M> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Constructor
	 * @param cfg core config
	 * @param modelObjectsMarshaller annotated with @ModelObjectsMarshaller
	 * @param eventBus
	 * @param entityManagerProvider
	 */
	public CoreCRUDServicesForModelObjectBase(final ServicesCoreModuleConfig cfg,
									   		  final Marshaller modelObjectsMarshaller,
									   		  final EventBus eventBus,
									   		  final DistributedCacheService cacheService) {
		super(cfg,
			  modelObjectsMarshaller,
			  eventBus,
			  cacheService);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CRUD
/////////////////////////////////////////////////////////////////////////////////////////
	
	@Override @SuppressWarnings("unchecked")
	public PersistenceOperationResult<Boolean> exists(final SecurityContext securityContext,
													  final O oid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(CRUDServicesForModelObject.class)
							.exists(securityContext,
									oid);
	}
	
	@Override @SuppressWarnings("unchecked")
	public PersistenceOperationResult<Instant> getLastUpdateDate(final SecurityContext securityContext,
														   	  final O oid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(CRUDServicesForModelObject.class)
							.getLastUpdateDate(securityContext,
											   oid);
	}
	
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<M> load(final SecurityContext securityContext,
							  final O oid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(CRUDServicesForModelObject.class)
							.load(securityContext,
								  oid);
	}
	
	@Override 
	public CRUDResult<M> create(final SecurityContext securityContext,
								final M record) {
		return this.create(securityContext,
						   record,
						   null);	// no callback
	}
	
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<M> create(final SecurityContext securityContext,
								final M record,
								final COREServiceMethodCallbackSpec callbackSpec) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(CRUDServicesForModelObject.class)
							.create(securityContext,
									record,
									callbackSpec);
	}
	
	@Override
	public CRUDResult<M> update(final SecurityContext securityContext,
								final M record) {
		return this.update(securityContext,
						   record,
						   null);		// no callback
	}
	
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<M> update(final SecurityContext securityContext,
								final M record,
								final COREServiceMethodCallbackSpec callbackSpec) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(CRUDServicesForModelObject.class)
							.update(securityContext,
									record,
									callbackSpec);
	}
	
	@Override @SuppressWarnings("unchecked")
	public PersistenceOperationResult<Boolean> touch(final SecurityContext securityContext,
													 final O oid,final Instant date) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(CRUDServicesForModelObject.class)
							.touch(securityContext,
								   oid,date);
	}
	
	@Override 
	public CRUDResult<M> delete(final SecurityContext securityContext,
							    final O oid) {
		return this.delete(securityContext,
						   oid,
						   null);	// no callback
	}
	
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<M> delete(final SecurityContext securityContext,
							    final O oid,
							    final COREServiceMethodCallbackSpec callbackSpec) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(CRUDServicesForModelObject.class)
							.delete(securityContext,
									oid,
									callbackSpec);
	}
}